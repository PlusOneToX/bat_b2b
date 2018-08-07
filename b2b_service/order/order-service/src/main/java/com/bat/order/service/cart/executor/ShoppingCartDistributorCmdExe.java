package com.bat.order.service.cart.executor;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.bat.order.service.cart.convertor.ShoppingCartConvertor;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.dao.cart.ShoppingCartDistributorMapper;
import com.bat.order.dao.cart.ShoppingCartDiyDistributorMapper;
import com.bat.order.dao.cart.dataobject.ShoppingCartDistributorDO;

@Component
public class ShoppingCartDistributorCmdExe {

    @Resource
    private ShoppingCartDistributorMapper shoppingCartDistributorMapper;
    @Resource
    private ShoppingCartDiyDistributorMapper shoppingCartDiyDistributorMapper;
    @Resource
    private CommonValidator commonValidator;

    /**
     * 新增购物车商品(分销商)
     * 
     * @param cmd
     * @param userId
     */
    public BaseId create(ShoppingCartCmd cmd, String userId, String language) {
        commonValidator.checkUserId(userId);
        ShoppingCartDistributorDO beforeDistributorDO = null;
        // 非定制商品，考虑合并商品和活动一样的数据
        if (cmd.getGoodsType().equals(Constant.GOODS_TYPE_1)) {
            Map map = new HashMap();
            map.put("itemId", cmd.getItemId());
            map.put("distributorId", userId);
            if (cmd.getGoodsPromotionId() != null) {
                map.put("goodsPromotionId", cmd.getGoodsPromotionId());
            }
            if (cmd.getOrderPromotionId() != null) {
                map.put("orderPromotionId", cmd.getOrderPromotionId());
            }
            if (cmd.getGroupSeckillId() != null) {
                map.put("groupSeckillId", cmd.getGroupSeckillId());
            }
            if (cmd.getItemType() != null) {
                map.put("itemType", cmd.getItemType());
            }
            beforeDistributorDO = getBeforeDistributorDO(map);
        }
        ShoppingCartDistributorDO afterDistributorDO =
            ShoppingCartConvertor.toShoppingCartDistributorDO(cmd, userId, language);
        if (beforeDistributorDO != null) {
            afterDistributorDO.setId(beforeDistributorDO.getId());
            afterDistributorDO.setItemCount(
                beforeDistributorDO.getItemCount().intValue() + afterDistributorDO.getItemCount().intValue());
            shoppingCartDistributorMapper.updateByPrimaryKey(afterDistributorDO);
        } else {
            shoppingCartDistributorMapper.insert(afterDistributorDO);
            saveDiyData(cmd, afterDistributorDO.getId());
        }
        BaseId id = new BaseId();
        id.setId(afterDistributorDO.getId());
        return id;
    }

    /**
     * 获取已添加一样的商品数据
     * 
     * @return
     */
    private ShoppingCartDistributorDO getBeforeDistributorDO(Map map) {
        AtomicReference<ShoppingCartDistributorDO> beforeDistributorDO = new AtomicReference();
        List<ShoppingCartDistributorDO> shoppingCartDistributorDOS = shoppingCartDistributorMapper.listByMap(map);
        if (!CollectionUtils.isEmpty(shoppingCartDistributorDOS)) {
            List<Integer> deleteIds = new ArrayList<>();
            shoppingCartDistributorDOS.forEach(shoppingCartDistributorDO -> {
                if (beforeDistributorDO.get() == null) {
                    ShoppingCartDistributorDO distributorDO = new ShoppingCartDistributorDO();
                    BeanUtils.copyProperties(shoppingCartDistributorDO, distributorDO);
                    beforeDistributorDO.set(distributorDO);
                } else {
                    deleteIds.add(shoppingCartDistributorDO.getId());
                    beforeDistributorDO.get().setItemCount(beforeDistributorDO.get().getItemCount().intValue()
                        + shoppingCartDistributorDO.getItemCount().intValue());
                }
            });
            if (!CollectionUtils.isEmpty(deleteIds)) {
                shoppingCartDistributorMapper.deleteByIds(deleteIds);
            }
        }
        return beforeDistributorDO.get();
    }

    /**
     * 保持定制信息
     * 
     * @param cmd
     * @param cartId
     */
    private void saveDiyData(ShoppingCartCmd cmd, Integer cartId) {
        if (cmd.getGoodsType().equals(Constant.GOODS_TYPE_2) && cmd.getDiy() != null) {
            shoppingCartDiyDistributorMapper
                .insert(ShoppingCartConvertor.toShoppingCartDiyDistributorDO(cartId, cmd.getDiy()));
        }
    }

    /**
     * 批量添加购物车商品
     * 
     * @param cmds
     * @param userId
     * @return
     */
    public void createList(List<ShoppingCartCmd> cmds, String userId, String language) {
        commonValidator.checkUserId(userId);
        cmds.forEach(cmd -> {
            create(cmd, userId, language);
        });
    }

    /**
     * 修改购物车商品
     * 
     * @param cmd
     */
    public void update(ShoppingCartUpdateCmd cmd, String userId) {
        commonValidator.checkUserId(userId);
        BeanMap qryMap = BeanMap.create(cmd);
        Map<String, Object> subQryMap = new HashMap();
        subQryMap.putAll(qryMap);
        subQryMap.put("distributorId", userId);
        Date date = new Date(System.currentTimeMillis());
        subQryMap.put("updateTime", date);
        if (cmd.getGoodsType().equals(Constant.GOODS_TYPE_1)) {
            ShoppingCartDistributorDO beforeDistributorDO = getBeforeDistributorDO(subQryMap);
            if (beforeDistributorDO != null) {
                if (!beforeDistributorDO.getId().equals(cmd.getId())) {
                    beforeDistributorDO
                        .setItemCount(beforeDistributorDO.getItemCount().intValue() + cmd.getItemCount().intValue());
                } else {
                    beforeDistributorDO.setItemCount(cmd.getItemCount());
                }
                shoppingCartDistributorMapper.updateByPrimaryKey(beforeDistributorDO);
            } else {
                shoppingCartDistributorMapper.update(subQryMap);
            }
        } else {
            shoppingCartDistributorMapper.update(subQryMap);
        }
    }

    /**
     * 批量删除购物车
     * 
     * @param cmd
     */
    public void deleteByIds(BaseIds cmd) {
        shoppingCartDistributorMapper.deleteByIds(cmd.getIds());
        shoppingCartDiyDistributorMapper.deleteByCartIds(cmd.getIds());
    }

    /**
     * 删除购物车
     * 
     * @param cmd
     */
    public void deleteById(BaseId cmd) {
        shoppingCartDistributorMapper.deleteByPrimaryKey(cmd.getId());
        shoppingCartDiyDistributorMapper.deleteByCartId(cmd.getId());
    }

    /**
     * 根据商品Ids更新购物车商品状态
     *
     * @param goodsIds
     * @param openFlag
     * @return
     */
    public void changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag) {
        shoppingCartDistributorMapper.updateListOpenFlag(goodsIds, openFlag);
    }

    /**
     * 根据商品Ids更新购物车商品状态
     *
     * @param goodsIds
     * @param openFlag
     * @return
     */
    public void changeGoodsOpenFlagByItemIds(List<Integer> goodsIds, Short openFlag) {
        shoppingCartDistributorMapper.updateListOpenFlagByItemIds(goodsIds, openFlag);
    }

}
