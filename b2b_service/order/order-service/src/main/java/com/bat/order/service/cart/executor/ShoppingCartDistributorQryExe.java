package com.bat.order.service.cart.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.order.service.order.executor.distributor.UserDistributorOrderQryExe;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionRpcDTO;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;
import com.bat.order.dao.cart.ShoppingCartDistributorMapper;
import com.bat.order.dao.cart.ShoppingCartDiyDistributorMapper;
import com.bat.order.dao.cart.dataobject.ShoppingCartDistributorDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDiyDistributorDO;
import com.bat.order.service.cart.convertor.ShoppingCartConvertor;

@Component
public class ShoppingCartDistributorQryExe {

    @Resource
    private ShoppingCartDistributorMapper shoppingCartDistributorMapper;

    @Resource
    private ShoppingCartDiyDistributorMapper shoppingCartDiyDistributorMapper;

    @Resource
    private CommonRpcQryExe rpcQryExe;
    @Resource
    private CommonValidator commonValidator;

    @Resource
    private UserDistributorOrderQryExe userDistributorOrderQryExe;

    /**
     * 获取购物车商品列表
     * 
     * @param userId
     * @return
     */
    public List<ShoppingCartDTO> list(String userId, String language) {
        commonValidator.checkUserId(userId);
        List<ShoppingCartDistributorDO> shoppingCartDistributorDOS =
            shoppingCartDistributorMapper.listByDistributorId(Integer.valueOf(userId));

        // 过滤商品id
        List<Integer> goodsIdList =
            shoppingCartDistributorDOS.stream().map(ShoppingCartDistributorDO::getGoodsId).collect(Collectors.toList());

        List<ShoppingCartDiyDistributorDO> shoppingCartDiyDistributorDOS = new ArrayList<>();
        List<GoodsItemPromotionRpcDTO> rpcDTOS = new ArrayList<>();
        List<GoodsItemPriceRpcDTO> priceRpcDTOS = new ArrayList<>();
        List<GoodsItemBoxRpcDTO> boxRpcDTOS = new ArrayList<>();
        List<Integer> languageItemIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shoppingCartDistributorDOS)) {
            List<Integer> itemIds = new ArrayList<>();
            List<Integer> cartIds = new ArrayList<>();
            List<GoodsItemRpc> goodsItems = new ArrayList<>();
            shoppingCartDistributorDOS.forEach(shoppingCartDistributorDO -> {
                cartIds.add(shoppingCartDistributorDO.getId());
                if (!itemIds.contains(shoppingCartDistributorDO.getItemId())) {
                    itemIds.add(shoppingCartDistributorDO.getItemId());
                    GoodsItemRpc goodsItemRpc = new GoodsItemRpc();
                    goodsItemRpc.setItemId(shoppingCartDistributorDO.getItemId());
                    goodsItemRpc.setGoodsId(shoppingCartDistributorDO.getGoodsId());
                    goodsItems.add(goodsItemRpc);
                }
                if (((StringUtils.isBlank(shoppingCartDistributorDO.getLanguage()) && language.equals(Constant.LANGUAGE_EN))
                    || (StringUtils.isNotBlank(shoppingCartDistributorDO.getLanguage())
                        && !language.equals(shoppingCartDistributorDO.getLanguage())))
                    && !languageItemIds.contains(shoppingCartDistributorDO.getItemId())) {
                    languageItemIds.add(shoppingCartDistributorDO.getItemId());
                }
            });
            shoppingCartDiyDistributorDOS = shoppingCartDiyDistributorMapper.listByCartIds(cartIds);
            // 获取购物车商品活动数据
            rpcDTOS = rpcQryExe.goodsItemPromotionDistributor(shoppingCartDistributorDOS, Integer.valueOf(userId));
            // 获取价格
            priceRpcDTOS = rpcQryExe.listDistributorGoodsItemPrice(goodsItems, Integer.valueOf(userId));
            // 装箱规格
            boxRpcDTOS = rpcQryExe.listGoodsItemBoxRpcDTOList(itemIds);
        }
        List<Integer> promotionRuleIds = new ArrayList<>();
        List<Integer> groupSeckillIds = new ArrayList<>();
        List<ShoppingCartDTO> cartDTOS =
            ShoppingCartConvertor.toShoppingCartDTOByDistributorList(shoppingCartDistributorDOS,
                shoppingCartDiyDistributorDOS, rpcDTOS, promotionRuleIds, groupSeckillIds, priceRpcDTOS, boxRpcDTOS);
        if (!CollectionUtils.isEmpty(cartDTOS)) {
            List<ShoppingCartDistributorDO> updateCartDistributorDOS = new ArrayList<>();
            List<Integer> deleteIds = new ArrayList<>();
            Date date = new Date(System.currentTimeMillis());
            Map<Integer, ShoppingCartDTO> cartDTOMap =
                cartDTOS.stream().collect(Collectors.toMap(ShoppingCartDTO::getId, cartDTO -> cartDTO));
            shoppingCartDistributorDOS.forEach(shoppingCartDistributorDO -> {
                if (shoppingCartDistributorDO.getGoodsPromotionId() != null
                    && !promotionRuleIds.contains(shoppingCartDistributorDO.getGoodsPromotionId())) {
                    if (shoppingCartDistributorDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartDistributorDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartDistributorDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartDistributorDO.setGoodsPromotionId(null);
                        shoppingCartDistributorDO.setUpdateTime(date);
                        updateCartDistributorDOS.add(shoppingCartDistributorDO);
                    }
                }
                if (shoppingCartDistributorDO.getOrderPromotionId() != null
                    && !promotionRuleIds.contains(shoppingCartDistributorDO.getOrderPromotionId())) {
                    if (shoppingCartDistributorDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartDistributorDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartDistributorDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartDistributorDO.setOrderPromotionId(null);
                        shoppingCartDistributorDO.setUpdateTime(date);
                        updateCartDistributorDOS.add(shoppingCartDistributorDO);
                    }
                }
                if (shoppingCartDistributorDO.getGroupSeckillId() != null
                    && !groupSeckillIds.contains(shoppingCartDistributorDO.getGroupSeckillId())) {
                    if (shoppingCartDistributorDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartDistributorDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartDistributorDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartDistributorDO.setGroupSeckillId(null);
                        shoppingCartDistributorDO.setUpdateTime(date);
                        updateCartDistributorDOS.add(shoppingCartDistributorDO);
                    }
                }
            });
            if (!CollectionUtils.isEmpty(updateCartDistributorDOS)) {
                shoppingCartDistributorMapper.updateList(updateCartDistributorDOS);
            }
            if (!CollectionUtils.isEmpty(deleteIds)) {
                shoppingCartDistributorMapper.deleteByIds(deleteIds);
            }
        }
        // 语言处理
        if (!CollectionUtils.isEmpty(languageItemIds)) {
            List<GoodsItemRpcDTO> itemRpcDTOS = rpcQryExe.goodsItemByItemIds(languageItemIds);
            ShoppingCartConvertor.toShoppingCartDTOList(itemRpcDTOS, cartDTOS, language);
        }

        // 判断商品是否拥有可视
        if (ObjectUtils.isNotEmpty(goodsIdList)) {
            // 根据用户id和商品id列表，去计算列表中的不可视商品
            List<Map<Integer, Boolean>> invisibleProductListMap =
                userDistributorOrderQryExe.userProductVisibility(Integer.parseInt(userId), goodsIdList);
            // 循环购物车列表，如果购物车中的商品id能从不可视的map集合中取到内容，则说明购物车中有不可视商品
            for (ShoppingCartDTO shoppingCartDTO : cartDTOS) {
                long count = invisibleProductListMap.stream()
                    .filter(x -> ObjectUtils.isNotEmpty(x.get(shoppingCartDTO.getGoodsId()))).count();
                if (count > 0) {
                    // 不可视
                    shoppingCartDTO.setVisible(0);
                }
            }
        }
        return cartDTOS;
    }
}
