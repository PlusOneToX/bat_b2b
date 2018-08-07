package com.bat.order.service.cart.executor;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.bat.order.service.cart.convertor.ShoppingCartConvertor;
import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartDiyCmd;
import com.bat.order.api.cart.dto.ShoppingCartUpdateCmd;
import com.bat.order.api.cart.dto.ShoppingCustomerCartCmd;
import com.bat.order.dao.cart.ShoppingCartCustomerMapper;
import com.bat.order.dao.cart.ShoppingCartDiyCustomerMapper;
import com.bat.order.dao.cart.dataobject.ShoppingCartCustomerDO;

@Component
public class ShoppingCartCustomerCmdExe {

    @Resource
    private ShoppingCartCustomerMapper shoppingCartCustomerMapper;
    @Resource
    private ShoppingCartDiyCustomerMapper shoppingCartDiyCustomerMapper;
    @Resource
    private CommonValidator commonValidator;
    @Resource
    private CommonRpcQryExe rpcQryExe;

    /**
     * 新增购物车商品(支持C端柔性定制)
     * 
     * @param cmd
     * @param userId
     * @param distributorId
     * @param language
     * @return
     */
    public BaseId createDiy(ShoppingCustomerCartCmd cmd, String userId, String distributorId, String language) {
        commonValidator.checkUserId(userId, distributorId);
        BaseId id = new BaseId();
        GoodsItemRpcDTO rpcDTO = rpcQryExe.goodsItemByCode(cmd.getItemCode());
        ShoppingCartCustomerDO afterCustomerDO =
            ShoppingCartConvertor.toShoppingCartCustomerDO(cmd, userId, distributorId, language, rpcDTO);
        shoppingCartCustomerMapper.insert(afterCustomerDO);
        saveDiyData(cmd.getDiy(), afterCustomerDO.getId());
        id.setId(afterCustomerDO.getId());
        return id;
    }

    /**
     * 新增购物车商品(支持C端柔性定制)
     * 
     * @param cmds
     * @param userId
     * @param distributorId
     * @param language
     */
    public void createDiyList(List<ShoppingCustomerCartCmd> cmds, String userId, String distributorId,
        String language) {
        commonValidator.checkUserId(userId, distributorId);
        cmds.forEach(cmd -> {
            createDiy(cmd, userId, distributorId, language);
        });
    }

    /**
     * 新增购物车商品(C端客戶)
     *
     * @param cmd
     * @param userId
     */
    public BaseId create(ShoppingCartCmd cmd, String userId, String distributorId, String language) {
        commonValidator.checkUserId(userId, distributorId);
        ShoppingCartCustomerDO beforeCustomerDO = null;
        // 非定制商品，考虑合并商品和活动一样的数据
        if (cmd.getGoodsType().equals(Constant.GOODS_TYPE_1)) {
            Map map = new HashMap();
            map.put("itemId", cmd.getItemId());
            map.put("customerId", userId);
            map.put("distributorId", distributorId);
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
            beforeCustomerDO = getBeforeCustomerDO(map);
        }
        ShoppingCartCustomerDO afterCustomerDO =
            ShoppingCartConvertor.toShoppingCartCustomerDO(cmd, userId, distributorId, language);
        if (beforeCustomerDO != null) {
            afterCustomerDO.setId(beforeCustomerDO.getId());
            afterCustomerDO
                .setItemCount(beforeCustomerDO.getItemCount().intValue() + afterCustomerDO.getItemCount().intValue());
            shoppingCartCustomerMapper.updateByPrimaryKey(afterCustomerDO);
        } else {
            shoppingCartCustomerMapper.insert(afterCustomerDO);
            saveDiyData(cmd.getDiy(), afterCustomerDO.getId());
        }
        BaseId id = new BaseId();
        id.setId(afterCustomerDO.getId());
        return id;
    }

    /**
     * 获取已添加一样的商品数据
     *
     * @return
     */
    private ShoppingCartCustomerDO getBeforeCustomerDO(Map map) {
        AtomicReference<ShoppingCartCustomerDO> beforeCustomerDO = new AtomicReference();
        List<ShoppingCartCustomerDO> shoppingCartCustomerDOS = shoppingCartCustomerMapper.listByMap(map);
        if (!CollectionUtils.isEmpty(shoppingCartCustomerDOS)) {
            List<Integer> deleteIds = new ArrayList<>();
            shoppingCartCustomerDOS.forEach(shoppingCartCustomerDO -> {
                if (beforeCustomerDO.get() == null) {
                    ShoppingCartCustomerDO customerDO = new ShoppingCartCustomerDO();
                    BeanUtils.copyProperties(shoppingCartCustomerDO, customerDO);
                    beforeCustomerDO.set(customerDO);
                } else {
                    deleteIds.add(shoppingCartCustomerDO.getId());
                    beforeCustomerDO.get().setItemCount(beforeCustomerDO.get().getItemCount().intValue()
                        + shoppingCartCustomerDO.getItemCount().intValue());
                }
            });
            if (!CollectionUtils.isEmpty(deleteIds)) {
                shoppingCartCustomerMapper.deleteByIds(deleteIds);
            }
        }
        return beforeCustomerDO.get();
    }

    /**
     * 保持定制信息
     *
     * @param cmd
     * @param cartId
     */
    private void saveDiyData(ShoppingCartDiyCmd cmd, Integer cartId) {
        if (cmd != null) {
            shoppingCartDiyCustomerMapper.insert(ShoppingCartConvertor.toShoppingCartDiyCustomerDO(cartId, cmd));
        }
    }

    /**
     * 批量加入购物车商品
     * 
     * @param cmds
     * @param userId
     * @param distributorId
     */
    public void createList(List<ShoppingCartCmd> cmds, String userId, String distributorId, String language) {
        commonValidator.checkUserId(userId, distributorId);
        cmds.forEach(cmd -> {
            create(cmd, userId, distributorId, language);
        });
    }

    /**
     * 修改购物车商品
     * 
     * @param cmd
     */
    public void update(ShoppingCartUpdateCmd cmd, String userId, String distributorId) {
        commonValidator.checkUserId(userId, distributorId);
        BeanMap qryMap = BeanMap.create(cmd);
        Map<String, Object> subQryMap = new HashMap();
        subQryMap.putAll(qryMap);
        subQryMap.put("customerId", userId);
        subQryMap.put("distributorId", distributorId);
        Date date = new Date(System.currentTimeMillis());
        subQryMap.put("updateTime", date);
        if (cmd.getGoodsType().equals(Constant.GOODS_TYPE_1)) {
            ShoppingCartCustomerDO beforeCustomerDO = getBeforeCustomerDO(subQryMap);
            if (beforeCustomerDO != null) {
                if (!beforeCustomerDO.getId().equals(cmd.getId())) {
                    beforeCustomerDO
                        .setItemCount(beforeCustomerDO.getItemCount().intValue() + cmd.getItemCount().intValue());
                } else {
                    beforeCustomerDO.setItemCount(cmd.getItemCount());
                }
                shoppingCartCustomerMapper.updateByPrimaryKey(beforeCustomerDO);
            } else {
                shoppingCartCustomerMapper.update(subQryMap);
            }
        } else {
            shoppingCartCustomerMapper.update(subQryMap);
        }
    }

    /**
     * 批量删除购物车
     * 
     * @param cmd
     */
    public void deleteByIds(BaseIds cmd) {
        shoppingCartCustomerMapper.deleteByIds(cmd.getIds());
        shoppingCartDiyCustomerMapper.deleteByCartIds(cmd.getIds());
    }

    /**
     * 删除购物车
     * 
     * @param cmd
     */
    public void deleteById(BaseId cmd) {
        shoppingCartCustomerMapper.deleteByPrimaryKey(cmd.getId());
        shoppingCartDiyCustomerMapper.deleteByCartId(cmd.getId());
    }

    /**
     * 根据商品Ids更新购物车商品状态
     *
     * @param goodsIds
     * @param openFlag
     * @return
     */
    public void changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag) {
        shoppingCartCustomerMapper.updateListOpenFlag(goodsIds, openFlag);
    }

    /**
     * 根据货品Ids更新购物车货品状态
     *
     * @param goodsIds
     * @param openFlag
     * @return
     */
    public void changeGoodsOpenFlagByItemIds(List<Integer> goodsIds, Short openFlag) {
        shoppingCartCustomerMapper.updateListOpenFlagByItemIds(goodsIds, openFlag);
    }

}
