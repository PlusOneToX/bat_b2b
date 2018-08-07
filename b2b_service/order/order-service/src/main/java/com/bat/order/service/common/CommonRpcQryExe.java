package com.bat.order.service.common;

import static com.bat.order.service.common.Constant.PRESENT_FLAG_0;
import static com.bat.order.service.common.Constant.PRESENT_FLAG_1;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.common.error.OrderGoodsDiyErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.flexible.model.api.ModelMaterialRelevanceServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.OrderGoodDiyQry;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.dao.cart.dataobject.ShoppingCartCustomerDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDistributorDO;
import com.bat.order.dao.order.OrderGoodsDiyDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.service.cart.convertor.ShoppingCartConvertor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonRpcQryExe {

    private static final Logger logger = LoggerFactory.getLogger(CommonRpcQryExe.class);

    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceDistributorRpc promotionServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private ModelMaterialRelevanceServiceRpc modelMaterialRelevanceServiceRpc;

    @Resource
    private OrderGoodsDiyDOMapper orderGoodsDiyDOMapper;

    public List<GoodsItemPromotionRpcDTO> goodsItemPromotionDistributor(
        List<ShoppingCartDistributorDO> shoppingCartDistributorDOS, Integer distributorId) {
        List<GoodsItemRpcQry> rpcQryList =
            ShoppingCartConvertor.toGoodsItemRpcQryByDistributorList(shoppingCartDistributorDOS);
        Response<List<GoodsItemPromotionRpcDTO>> listResponse =
            promotionServiceRpc.goodsItemPromotion(rpcQryList, distributorId, PRESENT_FLAG_0);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    public List<GoodsItemPromotionRpcDTO>
        goodsItemPromotionDistributorByOrderGoodsDOList(List<OrderGoodsDO> orderGoodsDOS, Integer distributorId) {
        List<GoodsItemRpcQry> rpcQryList = CommonRpcConvertor.toGoodsItemRpcQryList(orderGoodsDOS);
        Response<List<GoodsItemPromotionRpcDTO>> listResponse =
            promotionServiceRpc.goodsItemPromotion(rpcQryList, distributorId, PRESENT_FLAG_1);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    public List<GoodsItemPromotionRpcDTO>
        goodsItemPromotionByCustomer(List<ShoppingCartCustomerDO> shoppingCartCustomerDOS, Integer distributorId) {
        List<GoodsItemRpcQry> rpcQryList =
            ShoppingCartConvertor.toGoodsItemRpcQryByCustomerList(shoppingCartCustomerDOS);
        Response<List<GoodsItemPromotionRpcDTO>> listResponse =
            promotionServiceRpc.goodsItemPromotion(rpcQryList, distributorId, PRESENT_FLAG_0);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    public GoodsItemRpcDTO goodsItemByCode(String itemCode) {
        com.bat.dubboapi.goods.common.Response<GoodsItemRpcDTO> response =
            goodsServiceRpc.goodsItemByCode(itemCode);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public List<GoodsItemRpcDTO> goodsItemByItemIds(List<Integer> itemIds) {
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> listResponse =
            goodsServiceRpc.listGoodsItemByIds(itemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    public List<GoodsItemPriceRpcDTO> listDistributorGoodsItemPrice(List<GoodsItemRpc> goodsItems,
        Integer distributorId) {
        GoodsItemPriceRpcQry rpcQry = new GoodsItemPriceRpcQry();
        rpcQry.setDistributorId(distributorId);
        rpcQry.setGoodsItems(goodsItems);
        com.bat.dubboapi.goods.common.Response<List<GoodsItemPriceRpcDTO>> listResponse =
            goodsServiceRpc.listDistributorGoodsItemPrice(rpcQry);
        if (listResponse != null && listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    public List<GoodsItemBoxRpcDTO> listGoodsItemBoxRpcDTOList(List<Integer> itemIds) {
        com.bat.dubboapi.goods.common.Response<List<GoodsItemBoxRpcDTO>> listResponse =
            goodsServiceRpc.listGoodsItemBoxList(itemIds);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        }
        return null;
    }

    // 根据货品编号和分销商编号获取C端价格
    public List<DistributorCustomerPriceDTO> getByDistributorIdAndItemIds(Integer distributorId,
        List<Integer> itemIds) {
        com.bat.dubboapi.distributor.common.Response<List<DistributorCustomerPriceDTO>> response =
            distributorCustomPriceServiceRpc.getByDistributorIdAndItemIds(distributorId, itemIds);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    // 根据型号ID和材质ID获取机型材质缺货状态
    public ModelMaterialRelevanceDTORpcQry getByModelIdAndMaterialId(Integer modelId, Integer materialId) {
        com.bat.dubboapi.flexible.common.Response<ModelMaterialRelevanceDTORpcQry> response =
            modelMaterialRelevanceServiceRpc.getByModelIdAndMaterialId(modelId, materialId);
        logger.info("根据型号ID和材质ID获取机型材质缺货状态");
        System.out.println(response.getData());
        System.out.println(response.isSuccess());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            return null;
        }
    }

    /**
     * 处理定制商品重量的问题
     * <p>
     * 定制商品 取柔性服务 型号与材质 关联的重量
     *
     * @param orderGoodsDOS
     * @return
     */
    public List<OrderGoodsDO> calcDiyWeight(List<OrderGoodsDO> orderGoodsDOS) {
        log.info("orderGoodsDOS json:{}", JSON.toJSONString(orderGoodsDOS));
        List<OrderGoodsDO> orderGoodsNormal = new ArrayList<>();
        List<OrderGoodsDO> orderGoodsDiy = new ArrayList<>();
        List<OrderGoodDiyQry> orderGoodDiyQries = new ArrayList<>();
        for (OrderGoodsDO orderGoodsDO : orderGoodsDOS) {
            if (orderGoodsDO.getGoodsType().equals(Constant.GOODS_TYPE_1)) {
                // 普通商品
                orderGoodsNormal.add(orderGoodsDO);
            } else if (orderGoodsDO.getGoodsType().equals(Constant.GOODS_TYPE_2)) {
                // 定制商品
                orderGoodsDiy.add(orderGoodsDO);
            }
        }
        // 封装批量请求参数
        for (int i = 0; i < orderGoodsDiy.size(); i++) {
            OrderGoodsDO orderGoodsDO = orderGoodsDiy.get(i);
            OrderGoodsDiyDO goodsDiyDO = orderGoodsDO.getOrderGoodsDiy();
            if (goodsDiyDO == null) {
                // 生成分销层数据时没有定制信息 去查。如果还没有那就真没有
                goodsDiyDO = orderGoodsDiyDOMapper.getByOrderGoodsId(orderGoodsDO.getId());
                if (goodsDiyDO == null) {
                    throw OrderException.buildException(OrderGoodsDiyErrorCode.B_ORDER_GOODS_DIY_NULL,
                        "B_ORDER_GOODS_DIY_NULL");
                }
            }
            OrderGoodDiyQry qry = new OrderGoodDiyQry();
            qry.setIndex(i);
            qry.setModelId(goodsDiyDO.getModelId());
            qry.setMaterialId(goodsDiyDO.getMaterialId());
            orderGoodDiyQries.add(qry);
        }
        if (!CollectionUtils.isEmpty(orderGoodDiyQries)) {
            log.info("rpc 请求参数 json:{}", JSON.toJSONString(orderGoodDiyQries));
            com.bat.dubboapi.flexible.common.Response<List<OrderGoodDiyQry>> listResponse =
                modelMaterialRelevanceServiceRpc.listByModelIdAndMaterialId(orderGoodDiyQries);
            log.info("rpc 返回值 json:{}", JSON.toJSONString(listResponse));
            if (listResponse.isSuccess()) {
                List<OrderGoodDiyQry> data = listResponse.getData();
                if (data.size() != orderGoodsDiy.size()) {
                    throw OrderException.buildException(OrderGoodsDiyErrorCode.B_ORDER_GOODS_DIY_SIZE_ERROR,
                        "B_ORDER_GOODS_DIY_SIZE_ERROR");
                }
                for (int i = 0; i < data.size(); i++) {
                    OrderGoodsDO orderGoodsDO = orderGoodsDiy.get(i);
                    orderGoodsDO.setWeight(data.get(i).getWeight());
                }
            }
        }
        orderGoodsNormal.addAll(orderGoodsDiy);
        return orderGoodsNormal;
    }
}
