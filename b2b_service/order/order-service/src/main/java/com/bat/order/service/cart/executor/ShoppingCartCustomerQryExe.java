package com.bat.order.service.cart.executor;

import com.bat.order.service.common.CommonRpcQryExe;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionRpcDTO;
import com.bat.order.api.cart.dto.data.ShoppingCartDTO;
import com.bat.order.dao.cart.ShoppingCartCustomerMapper;
import com.bat.order.dao.cart.ShoppingCartDiyCustomerMapper;
import com.bat.order.dao.cart.dataobject.ShoppingCartCustomerDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDiyCustomerDO;
import com.bat.order.service.cart.convertor.ShoppingCartConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShoppingCartCustomerQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCustomerQryExe.class);

    @Resource
    private ShoppingCartCustomerMapper shoppingCartCustomerMapper;

    @Resource
    private ShoppingCartDiyCustomerMapper shoppingCartDiyCustomerMapper;

    @Resource
    private CommonRpcQryExe rpcQryExe;
    @Resource
    private CommonValidator commonValidator;

    /**
     * 获取购物车商品列表(C端客户)
     *
     * @param userId
     * @param distributorId
     * @param language
     * @return
     */
    public List<ShoppingCartDTO> list(String userId, String distributorId, String language) {
        //检查用户id不能为空且归属分销商不能为空
        commonValidator.checkUserId(userId, distributorId);

        LOGGER.info("userId: " + userId + "----" + "distributorId: " + distributorId);

        //通过用户编号和分销商编号查询数据
        List<ShoppingCartCustomerDO> shoppingCartCustomerDOS = shoppingCartCustomerMapper.listByCustomerIdAndDistributorId(Integer.valueOf(userId), Integer.valueOf(distributorId));

        LOGGER.info("shoppingCartCustomerDOS");
        LOGGER.info(String.valueOf(shoppingCartCustomerDOS));

        List<ShoppingCartDiyCustomerDO> shoppingCartDiyCustomerDOS = new ArrayList<>();
        List<GoodsItemPromotionRpcDTO> rpcDTOS = new ArrayList<>();

        // 分销商对应C端价格
        List<DistributorCustomerPriceDTO> customerPriceDTOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(shoppingCartCustomerDOS)) {
            List<Integer> itemIds = new ArrayList<>();
            List<Integer> cartIds = new ArrayList<>();
            //cartIds->购物车ID编号   itemIds->货品ID编号
            shoppingCartCustomerDOS.forEach(shoppingCartCustomerDO -> {
                cartIds.add(shoppingCartCustomerDO.getId());
                if (!itemIds.contains(shoppingCartCustomerDO.getItemId())) {
                    itemIds.add(shoppingCartCustomerDO.getItemId());
                }
                //根据购物车id查询数据 --------暂时不上线 update 2018-5-11
                //ShoppingCartDiyCustomerDO shoppingCartDiyCustomerDO= shoppingCartDiyCustomerMapper.getByCardId(shoppingCartCustomerDO.getId());

                //根据材质编号和机型编号查询缺货状态 --------暂时不上线 update 2018-5-11
                //ModelMaterialRelevanceDTORpcQry modelIdAndMaterialId = rpcQryExe.getByModelIdAndMaterialId(shoppingCartDiyCustomerDO.getModelId(), shoppingCartDiyCustomerDO.getMaterialId());

                //设置机型材质缺货状态 --------暂时不上线 update 2018-5-11
                //shoppingCartCustomerDO.setIsStockOut(modelIdAndMaterialId.getUnderStockFlag());

            });
            LOGGER.info("购物车数据" + shoppingCartCustomerDOS);

            // 获取购物车定制信息
            shoppingCartDiyCustomerDOS = shoppingCartDiyCustomerMapper.listByCartIds(cartIds);

            LOGGER.info("获取购物车定制信息: " + shoppingCartDiyCustomerDOS);

            // 根据货品编号和分销商编号获取C端价格
            customerPriceDTOS = rpcQryExe.getByDistributorIdAndItemIds(Integer.valueOf(distributorId), itemIds);

        }
        List<Integer> promotionRuleIds = new ArrayList<>();
        List<Integer> groupSeckillIds = new ArrayList<>();
        List<ShoppingCartDTO> cartDTOS = ShoppingCartConvertor.toShoppingCartDTOByCustomerList(shoppingCartCustomerDOS,
                shoppingCartDiyCustomerDOS, rpcDTOS, promotionRuleIds, groupSeckillIds, customerPriceDTOS);
        if (!CollectionUtils.isEmpty(cartDTOS)) {
            List<ShoppingCartCustomerDO> updateCartCustomerDOS = new ArrayList<>();
            List<Integer> deleteIds = new ArrayList<>();
            Date date = new Date(System.currentTimeMillis());
            Map<Integer, ShoppingCartDTO> cartDTOMap = cartDTOS.stream().collect(Collectors.toMap(ShoppingCartDTO::getId, cartDTO -> cartDTO));
            shoppingCartCustomerDOS.forEach(shoppingCartCustomerDO -> {
                // goods_promotion_id商品促销活动字段
                if (shoppingCartCustomerDO.getGoodsPromotionId() != null
                        && !promotionRuleIds.contains(shoppingCartCustomerDO.getGoodsPromotionId())) {
                    if (shoppingCartCustomerDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartCustomerDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartCustomerDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartCustomerDO.setGoodsPromotionId(null);
                        shoppingCartCustomerDO.setUpdateTime(date);
                        updateCartCustomerDOS.add(shoppingCartCustomerDO);
                    }
                }
                if (shoppingCartCustomerDO.getOrderPromotionId() != null
                        && !promotionRuleIds.contains(shoppingCartCustomerDO.getOrderPromotionId())) {
                    if (shoppingCartCustomerDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartCustomerDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartCustomerDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartCustomerDO.setOrderPromotionId(null);
                        shoppingCartCustomerDO.setUpdateTime(date);
                        updateCartCustomerDOS.add(shoppingCartCustomerDO);
                    }
                }
                if (shoppingCartCustomerDO.getGroupSeckillId() != null
                        && !groupSeckillIds.contains(shoppingCartCustomerDO.getGroupSeckillId())) {
                    if (shoppingCartCustomerDO.getItemType().equals(Constant.ITEM_TYPE_2)) {
                        deleteIds.add(shoppingCartCustomerDO.getId());
                        ShoppingCartDTO cartDTO = cartDTOMap.get(shoppingCartCustomerDO.getId());
                        cartDTOS.remove(cartDTO);
                    } else {
                        shoppingCartCustomerDO.setGroupSeckillId(null);
                        shoppingCartCustomerDO.setUpdateTime(date);
                        updateCartCustomerDOS.add(shoppingCartCustomerDO);
                    }
                }
            });

            if (!CollectionUtils.isEmpty(updateCartCustomerDOS)) {
                shoppingCartCustomerMapper.updateList(updateCartCustomerDOS);
            }
            if (!CollectionUtils.isEmpty(deleteIds)) {
                shoppingCartCustomerMapper.deleteByIds(deleteIds);
            }
        }
        // TODO 语言处理
        return cartDTOS;
    }

}
