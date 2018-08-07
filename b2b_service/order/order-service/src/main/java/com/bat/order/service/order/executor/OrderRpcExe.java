package com.bat.order.service.order.executor;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.dubboapi.thirdparty.feikuai.api.FeiKuaiServiceRpc;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.data.dao.OrderCustomerDO;
import com.bat.order.service.common.data.dto.OrderGoodsDTO;
import com.bat.order.service.common.data.dto.OrderInfoDTO;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.convertor.OrderCustomerConvertor;
import com.bat.order.service.order.convertor.OrderDistributorConvertor;
import com.bat.order.service.order.convertor.OrderRpcConvertor;
import com.bat.order.service.order.executor.distributor.UserDistributorOrderCmdExe;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.*;
import com.bat.dubboapi.financial.basesetting.api.FinancialCurrencyServiceRpc;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.flexible.exchange.ExchangeCardServiceRpc;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeB2BOrderDTORpcQry;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeOrderDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeSimpleDTO;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeUseCmd;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsAreVisibleRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.OrderInfoCmd;
import com.bat.dubboapi.order.order.dto.OrderTradeRpcQry;
import com.bat.dubboapi.order.order.dto.data.OrderDTO;
import com.bat.dubboapi.order.order.dto.data.OrderTradeRpcDTO;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.promotion.api.PromotionServiceCustomerRpc;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.OrderPromotionRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.promotion.dto.data.OrderPromotionRpcDTO;
import com.bat.dubboapi.promotion.dto.data.RebateVoucherRpcDTO;
import com.bat.dubboapi.system.globalsetting.api.BaseSettingServiceRpc;
import com.bat.dubboapi.system.globalsetting.api.FactorySettingServiceRpc;
import com.bat.dubboapi.system.globalsetting.dto.BaseSettingRpcDTO;
import com.bat.dubboapi.system.globalsetting.dto.FactorySettingOrderInvalidRpcDTO;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.dubboapi.thirdparty.maike.api.ThirdPartyMaikeServiceRpc;
import com.bat.dubboapi.thirdparty.mongodb.api.LogServiceRpc;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.dubboapi.thirdparty.order.ThirdPartyThirdCodeOrderServiceRpc;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemInventorySummaryRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.order.dto.distributor.OrderPromotionQry;
import com.bat.order.dao.cost.OrderCustomerCostMapper;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.OrderCustomerDataMapper;
import com.bat.order.dao.data.OrderDistributorDataMapper;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.OrderInfoDOMapper;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.mq.dto.GoodsSaleDTO;
import com.bat.order.mq.dto.OrderRefundDTO;
import com.bat.order.service.cost.executor.OrderDistributorCostCmdExe;
import com.bat.order.service.data.executor.OrderDistributorDataCmdExe;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 16:58
 */
@Component
@Slf4j
public class OrderRpcExe {

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;
    @DubboReference(check = false, timeout = 60000, retries = 0)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceDistributorRpc promotionServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceCustomerRpc promotionServiceCustomerRpc;
    @DubboReference(check = false, timeout = 5000)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private FinancialCurrencyServiceRpc financialCurrencyServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private DistributorCustomPriceServiceRpc distributorCustomPriceServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private ExchangeCardServiceRpc exchangeCardServiceRpc;
    @DubboReference(check = false, timeout = 20000, retries = 0)
    private ExchangeCodeServiceRpc exchangeCodeServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private ThirdPartyMaikeServiceRpc thirdPartyMaikeServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private FeiKuaiServiceRpc feiKuaiServiceRpc;
    @DubboReference(check = false, timeout = 5000, retries = 0)
    private ThirdPartyThirdCodeOrderServiceRpc thirdPartyThirdCodeOrderServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private ThirdPartyOrderServiceErpRpc thirdPartyOrderServiceErpRpc;
    @DubboReference(check = false, timeout = 5000)
    private LogServiceRpc logServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private FactorySettingServiceRpc factorySettingServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private BaseSettingServiceRpc baseSettingServiceRpc;
    @Resource
    private OrderCustomerDataMapper orderCustomerDataMapper;
    @Resource
    private OrderDistributorDataMapper orderDistributorDataMapper;
    @Resource
    private OrderCustomerCostMapper orderCustomerCostMapper;
    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;
    @Resource
    private OrderInfoDOMapper orderInfoDOMapper;
    @Resource
    private UserDistributorOrderCmdExe userDistributorOrderCmdExe;
    @Resource
    private OrderDistributorDataCmdExe distributorDataCmdExe;
    @Resource
    private OrderGoodsCmdExe orderGoodsCmdExe;
    @Resource
    private OrderDistributorCostCmdExe orderDistributorCostCmdExe;
    @Resource
    private OrderExtendDataMapper orderExtendDataMapper;
    @Resource
    private CommonValidator commonValidator;
    @Resource
    private MessageSendService messageSendService;

    public List<GoodsItemRpcDTO> listGoodsItems(List<Integer> itemIds) {
        Response<List<GoodsItemRpcDTO>> listResponse = goodsServiceRpc.listGoodsItemByIds(itemIds);
        if (listResponse.isSuccess()) {
            List<GoodsItemRpcDTO> goodsItemRpcDTOS = listResponse.getData();
            return goodsItemRpcDTOS;
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    /**
     * 根据分销商和货品ids查询库存
     * 
     * @param distributor
     * @param itemIds
     * @param goodsItemRpcDTOS
     * @return
     */
    public List<GoodsItemInventorySummaryRpcDTO> summaryByItemIdListAndAreaIdListAndDistributorId(
        DistributorRpcDTO distributor, List<Integer> itemIds, List<GoodsItemRpcDTO> goodsItemRpcDTOS) {
        // 直发客户情况
        List<Integer> notSupportOnWays = new ArrayList<>();
        if (distributor.getOnWayFlag() != null && distributor.getOnWayFlag().equals(Constant.ON_WAY_FLAG_0)) {
            notSupportOnWays.addAll(itemIds);
        } else {
            if (distributor.getAutoDelivery().equals(Constant.AUTO_DELIVERY_1)) {
                List<Integer> ids = goodsItemRpcDTOS.stream()
                    .filter(goodsItemRpcDTO -> goodsItemRpcDTO.getOnwaySaleFlag().equals(Constant.ON_WAY_FLAG_0))
                    .map(GoodsItemRpcDTO::getId).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(ids)) {
                    notSupportOnWays.addAll(ids);
                }
            }
        }
        com.bat.dubboapi.warehouse.common.Response<List<GoodsItemInventorySummaryRpcDTO>> listResponse =
            warehouseStockServiceRpc.summaryByItemIdListAndAreaIdListAndDistributorId(itemIds,
                distributor.getSalesAreaIds(), distributor.getId(), notSupportOnWays);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    /**
     * 分销商和下单货品计算库存数据
     *
     * @param distributor
     * @return
     */
    public List<ItemStockLockDTO> summaryLockStock(DistributorRpcDTO distributor, List<OrderGoodsDTO> goodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
        Map<Integer, List<GoodsItemPromotionPriceRpcDTO>> goodsItemPricesMap) {
        // 获取锁库货品列表（相同货品不合并数量）
        List<GoodsItemCountDTO> goodsItemCountDTOS =
            OrderDistributorConvertor.toGoodsItemCountDTOList(goodss, goodsItemRpcDTOMap);
        if (!CollectionUtils.isEmpty(goodsItemCountDTOS)) {
            // 直发客户情况
            List<Integer> notSupportOnWays = new ArrayList<>();
            goodss.forEach(goods -> {
                List<GoodsItemPromotionPriceRpcDTO> priceRpcDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(goodsItemPricesMap)) {
                    priceRpcDTOS = goodsItemPricesMap.get(goods.getSerialNumber());
                }
                GoodsItemRpcDTO goodsItemRpcDTO = goodsItemRpcDTOMap.get(goods.getItemCode());
                // 拼团秒杀支持预售情况，一定支持在途下单,拼团秒杀不支持预售情况，一定不支持在途下单
                // 否则直发客户不支持在途，一定不支持在途
                // 否则直发客户支持在途，但货品不支持在途，一定不支持在途
                if (CollectionUtils.isEmpty(priceRpcDTOS) || priceRpcDTOS.get(0).getMtoFlag() == null) {
                    if (distributor.getOnWayFlag() != null && distributor.getOnWayFlag().equals(Constant.ON_WAY_FLAG_0)) {
                        notSupportOnWays.add(goodsItemRpcDTO.getId());
                    } else if (distributor.getAutoDelivery() != null
                        && distributor.getAutoDelivery().equals(Constant.AUTO_DELIVERY_1)) {
                        if (goodsItemRpcDTO.getOnwaySaleFlag() == null
                            || goodsItemRpcDTO.getOnwaySaleFlag().equals(Constant.ON_WAY_FLAG_0)) {
                            notSupportOnWays.add(goodsItemRpcDTO.getId());
                        }
                    }
                } else if (priceRpcDTOS.get(0).getMtoFlag() != null
                    && priceRpcDTOS.get(0).getMtoFlag().equals(Constant.MTO_FRAG_0)) {
                    notSupportOnWays.add(goodsItemRpcDTO.getId());
                }
            });
            List<ItemStockLockDTO> itemStockLockDTOS = summaryLockStock(goodsItemCountDTOS,
                distributor.getSalesAreaIds(), distributor.getId(), notSupportOnWays);
            return itemStockLockDTOS;
        }
        return null;
    }

    public List<ItemStockLockDTO> summaryLockStock(List<GoodsItemCountDTO> itemCountList, List<Integer> areaIdList,
        Integer distributorId, List<Integer> notSupportOnWayList) {
        com.bat.dubboapi.warehouse.common.Response<List<ItemStockLockDTO>> response =
            warehouseStockServiceRpc.summaryLockStock(itemCountList, areaIdList, distributorId, notSupportOnWayList);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据分销获取货品会员价
     * 
     * @return
     */
    public List<GoodsItemPriceRpcDTO> listDistributorGoodsItemPrice(GoodsItemPriceRpcQry rpcQry) {
        if (!CollectionUtils.isEmpty(rpcQry.getGoodsItems())) {
            Response<List<GoodsItemPriceRpcDTO>> listResponse = goodsServiceRpc.listDistributorGoodsItemPrice(rpcQry);
            if (listResponse.isSuccess()) {
                return listResponse.getData();
            } else {
                throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 根据分销获取货品会员价
     *
     * @return
     */
    public List<GoodsItemPriceRpcDTO> listDistributorGoodsItemPrice(List<OrderGoodsDO> orderGoodsDOS,
        Integer distributorId) {
        GoodsItemPriceRpcQry rpcQry = OrderRpcConvertor.toGoodsItemPriceRpcQry(orderGoodsDOS, distributorId);
        Response<List<GoodsItemPriceRpcDTO>> listResponse = goodsServiceRpc.listDistributorGoodsItemPrice(rpcQry);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    /**
     * 根据分销商id和货品ids查询C端客价格
     * 
     * @param distributorId
     * @param itemIds
     * @return
     */
    public List<DistributorCustomerPriceDTO> listDistributorCustomerGoodsItemPrice(Integer distributorId,
        List<Integer> itemIds) {
        com.bat.dubboapi.distributor.common.Response<List<DistributorCustomerPriceDTO>> response =
            distributorCustomPriceServiceRpc.getByDistributorIdAndItemIds(distributorId, itemIds);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据C端客户和价格获取活动价
     * 
     * @param qry
     * @return
     */
    public List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceCustomer(GoodsItemPromotionPriceRpcQry qry) {
        if (!CollectionUtils.isEmpty(qry.getGoodsItemPrices())) {
            com.bat.dubboapi.promotion.common.Response<List<GoodsItemPromotionPriceRpcDTO>> response =
                promotionServiceCustomerRpc.goodsItemPromotionPrice(qry);
            if (response.isSuccess()) {
                List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPrices = response.getData();
                if (goodsItemPromotionPrices == null) {
                    throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_PROMOTION_ERROR);
                }
                if (!CollectionUtils.isEmpty(goodsItemPromotionPrices)) {
                    goodsItemPromotionPrices.forEach(goodsItemPromotionPrice -> {
                        if (!goodsItemPromotionPrice.getFlag().equals(OrderInfoErrorCode.B_PROMOTION_SUCCESS)) {
                            throw OrderException.buildException(goodsItemPromotionPrice.getFlag(),
                                goodsItemPromotionPrice.getMsg());
                        }
                    });
                }
                return goodsItemPromotionPrices;
            } else {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
        } else {
            return null;
        }
    }

    /**
     * 根据分销商和价格获取活动价
     * 
     * @param qry
     * @return
     */
    public List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPriceDistributor(GoodsItemPromotionPriceRpcQry qry) {
        com.bat.dubboapi.promotion.common.Response<List<GoodsItemPromotionPriceRpcDTO>> response =
            promotionServiceRpc.goodsItemPromotionPrice(qry);
        if (response.isSuccess()) {
            List<GoodsItemPromotionPriceRpcDTO> goodsItemPromotionPrices = response.getData();
            if (goodsItemPromotionPrices == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_PROMOTION_ERROR);
            }
            if (!CollectionUtils.isEmpty(goodsItemPromotionPrices)) {
                goodsItemPromotionPrices.forEach(goodsItemPromotionPrice -> {
                    if (!goodsItemPromotionPrice.getFlag().equals(OrderInfoErrorCode.B_PROMOTION_SUCCESS)) {
                        throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_PROMOTION_ERROR, goodsItemPromotionPrice.getMsg());
                    }
                });
            }
            return goodsItemPromotionPrices;
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取运费计算
     * 
     * @param rpcQry
     * @return
     */
    public LogisticsCalculationRpcDTO getLogisticsCalculation(LogisticsCalculationRpcQry rpcQry) {
        com.bat.dubboapi.system.common.Response<LogisticsCalculationRpcDTO> response =
            systemLogisticsServiceRpc.getLogisticsCalculationById(rpcQry);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取汇率
     * 
     * @param forCurrencyCode
     * @param toCurrencyCode
     * @return
     */
    public CurrencyRateRpcDTO getCurrencyRate(String forCurrencyCode, String toCurrencyCode) {
        com.bat.dubboapi.financial.common.Response<CurrencyRateRpcDTO> response =
            financialCurrencyServiceRpc.getCurrencyRate(forCurrencyCode, toCurrencyCode);
        if (response.isSuccess()) {
            CurrencyRateRpcDTO data = response.getData();
            if (data == null || data.getExchangeRate() == null) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_RATE_NULL);
            }
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据erp币种编码获取币种
     * 
     * @param erpNo
     * @return
     */
    public CurrencyRpcDO getCurrencyByErpNo(String erpNo) {
        com.bat.dubboapi.financial.common.Response<CurrencyRpcDO> response =
            financialCurrencyServiceRpc.getCurrencyByErpNo(erpNo);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 校验获取兑换卡
     * 
     * @param goodss
     * @return
     */
    public List<ExchangeCodeSimpleDTO> checkRxExchange(List<OrderGoodsDTO> goodss, Short shareFlag) {
        List<ExchangeCodeOrderDTO> dtos = OrderCustomerConvertor.toExchangeCodeOrderDTOList(goodss);
        com.bat.dubboapi.flexible.common.Response<List<ExchangeCodeSimpleDTO>> response =
            exchangeCardServiceRpc.checkRxExchange(dtos, shareFlag);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public void setUseOrderMsg(List<OrderCustomerDO> orderCustomerDOList) {
        List<ExchangeCodeUseCmd> cmds = OrderRpcConvertor.toExchangeCodeUseList(orderCustomerDOList);
        com.bat.dubboapi.flexible.common.Response response = exchangeCodeServiceRpc.setUseOrderMsg(cmds);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据分销商id获取分销商扩展信息
     * 
     * @param distributorId
     * @return
     */
    public DistributorExtendDataRpcDTO getDistributorExtendData(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorExtendDataRpcDTO> response =
            distributorServiceRpc.getDistributorExtendData(distributorId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据分销商id获取上级分销商扩展信息
     *
     * @param distributorId
     * @return
     */
    public DistributorExtendDataRpcDTO getAncestorDistributorExtendData(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorExtendDataRpcDTO> response =
            distributorServiceRpc.getAncestorDistributorExtendData(distributorId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据订单ids获取订单调起支付相关信息
     * 
     * @param qry
     * @return
     */
    public OrderTradeRpcDTO orderTrade(OrderTradeRpcQry qry) {
        // 交易方类型： 1分销商 2 C端客户
        DistributorExtendDataRpcDTO extendDataRpcDTO = null;
        // 根据订单id查询订单基本信息表
        List<OrderInfoDO> orderInfoDOS = orderInfoDOMapper.listByIds(qry.getOrderIds());
        Map<Integer, OrderInfoDO> orderInfoDOMap =
            orderInfoDOS.stream().collect(Collectors.toMap(OrderInfoDO::getId, orderInfoDO -> orderInfoDO));
        OrderTradeRpcDTO rpcDTO = null;
        if (qry.getCounterpartyType().equals(Constant.COUNTERPARTY_TYPE_1)) {
            // 查询付款分销商订单总费用直属分销商表信息
            List<OrderDistributorCostDO> distributorCostDOS =
                orderDistributorCostMapper.listByOrderIdsAndDistributorId(qry.getOrderIds(), qry.getDistributorId());
            // 查询付款分销商订单直属分销商信息表信息
            List<OrderDistributorDataDO> distributorDataDOS =
                orderDistributorDataMapper.listByOrderIdsAndDistributorId(qry.getOrderIds(), qry.getDistributorId());
            if (distributorDataDOS.get(0).getTreeNode() > 1) {
                // 获取该分销商的上级分销商拓展信息
                extendDataRpcDTO = getAncestorDistributorExtendData(qry.getDistributorId());
                // 上级分销商收款情况继续找上级分销收款方式
                while (extendDataRpcDTO != null && extendDataRpcDTO.getDistributionMode() != null
                    && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)) {
                    // 找到该笔订单收款的那个分销商
                    extendDataRpcDTO = getAncestorDistributorExtendData(extendDataRpcDTO.getDistributorId());
                }
            }
            rpcDTO = OrderRpcConvertor.toOrderTradeRpcDTOForDistributor(distributorCostDOS, distributorDataDOS,
                extendDataRpcDTO, orderInfoDOMap, qry.getCounterpartyType());
        } else {
            List<OrderCustomerDataDO> customerDataDOS =
                orderCustomerDataMapper.listByOrderIdsAndCustomerId(qry.getOrderIds(), qry.getCustomerId());
            List<OrderCustomerCostDO> customerCostDOS =
                orderCustomerCostMapper.listByOrderIdsAndCustomerId(qry.getOrderIds(), qry.getCustomerId());
            // 分销商C端收款情况
            extendDataRpcDTO = getDistributorExtendData(customerDataDOS.get(0).getDistributorId());
            if (extendDataRpcDTO != null && extendDataRpcDTO.getCustomerMode() != null
                && extendDataRpcDTO.getCustomerMode().equals(Constant.DISTRIBUTION_MODE_2)) {
                extendDataRpcDTO = getAncestorDistributorExtendData(extendDataRpcDTO.getDistributorId());
            }
            // 分销商收款情况(一直找到最后一个收款对象)上级分销商收款情况继续找上级分销收款方式
            while (extendDataRpcDTO != null && extendDataRpcDTO.getDistributionMode() != null
                && extendDataRpcDTO.getDistributionMode().equals(Constant.DISTRIBUTION_MODE_2)) {
                extendDataRpcDTO = getAncestorDistributorExtendData(extendDataRpcDTO.getDistributorId());
            }
            rpcDTO = OrderRpcConvertor.toOrderTradeRpcDTOForCustomer(customerCostDOS, customerDataDOS, extendDataRpcDTO,
                orderInfoDOMap, qry.getCounterpartyType());
        }
        if (extendDataRpcDTO != null) {
            rpcDTO.setCustomerFlag(extendDataRpcDTO.getCustomerFlag());
            rpcDTO.setCustomerMode(extendDataRpcDTO.getCustomerMode());
            rpcDTO.setSubAccountFlag(extendDataRpcDTO.getSubAccountFlag());
        }
        return rpcDTO;
    }

    /**
     * 获取分销商数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorRpcDTO getDistributor(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorRpcDTO> response =
            distributorServiceRpc.distributorById(Integer.valueOf(distributorId));
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取兑换卡相关数据
     * 
     * @param secretCode
     * @return
     */
    public List<ExchangeCodeB2BOrderDTORpcQry> listExchangeCodeB2BOrder(List<String> secretCode) {
        com.bat.dubboapi.flexible.common.Response<List<ExchangeCodeB2BOrderDTORpcQry>> response =
            exchangeCodeServiceRpc.listB2BOrderMsgBySecretCodeList(secretCode);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 兑换卡核销C端用户信息
     * 
     * @param userId
     * @param userName
     * @param orderGoodss
     */
    public void setUseOrderMsg(Integer userId, String userName, List<OrderGoodsDO> orderGoodss) {
        List<ExchangeCodeUseCmd> cmds = OrderRpcConvertor.toExchangeCodeUseCmdList(userId, userName, orderGoodss);
        com.bat.dubboapi.flexible.common.Response response = exchangeCodeServiceRpc.setUseOrderMsg(cmds);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 第三方接口服务同步分销订单
     * 
     * @param cmd
     * @return
     */
    public List<OrderDTO> createDistributorOrder(OrderInfoCmd cmd) {
        List<OrderInfoDO> orders = new ArrayList<>();
        List<OrderExtendDataDO> orderExtendDataDOS = orderExtendDataMapper
            .listByOrderThirdpartyNoAndDistributorId(cmd.getOrderThirdpartyNo(), cmd.getDistributorId());
        if (!CollectionUtils.isEmpty(orderExtendDataDOS)) {
            List<Integer> orderIds =
                orderExtendDataDOS.stream().map(OrderExtendDataDO::getOrderId).collect(Collectors.toList());
            orders = orderInfoDOMapper.listByIds(orderIds);
        } else {
            OrderInfoDTO orderDTO = OrderRpcConvertor.toOrderInfoDTO(cmd);
            orders = userDistributorOrderCmdExe.createOrder(orderDTO, cmd.getDistributorId(), null, null, null, null);
        }
        return OrderRpcConvertor.toOrderDTO(orders);
    }

    /**
     * ERP订单变更
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderChangeByErp(ErpOrderChangeCmd cmd) {
        boolean error = false;
        // 锁定库存
        List<ItemStockLockDTO> lockDTOS = new ArrayList<>();
        // 反锁库存
        List<ItemStockLockDTO> unLockDTOS = new ArrayList<>();
        OrderDistributorDataDO erpDistributorData = null;
        try {
            // 变更内容
            Map<String, List<Object>> changeMap = new HashMap<>();
            // 变更商品订单销售数量
            List<GoodsSaleDTO> saleDTOS = new ArrayList<>();
            // 分销商层数据变更(如币种)
            erpDistributorData = distributorDataCmdExe.orderChangeByErp(cmd, changeMap);
            OrderInfoDO order = orderInfoDOMapper.selectByPrimaryKey(erpDistributorData.getOrderId());
            OrderExtendDataDO orderExtendData = orderExtendDataMapper.getByOrderId(order.getId());
            commonValidator.checkOrderChangeErp(order, orderExtendData);
            DistributorRpcDTO distributor = getDistributor(erpDistributorData.getDistributorId());
            // 变更订单明细，变更分销层明细费用
            List<OrderGoodsDistributorCostDO> distributorCostDOS = orderGoodsCmdExe.orderChangeByErp(cmd, order,
                distributor, erpDistributorData, unLockDTOS, lockDTOS, changeMap, saleDTOS);
            // 变更订单，重新计算订单金额
            OrderRefundDTO refundDTO = orderDistributorCostCmdExe.orderChangeByErp(cmd, distributor, distributorCostDOS,
                erpDistributorData, changeMap);
            // 发送日志消息
            if (changeMap != null) {
                for (Map.Entry<String, List<Object>> entry : changeMap.entrySet()) {
                    messageSendService.oredrLogPackage(erpDistributorData.getOrderId(), "1-ERP订单变更", entry.getKey(),
                        JSONObject.toJSONString(entry.getValue()), "erp");
                }
            }
            // 发送退款申请单
            if (refundDTO != null) {
                messageSendService.orderRefund(refundDTO);
            }
            // 反锁订单变更库存 ERP订单变更
            if (!CollectionUtils.isEmpty(unLockDTOS)) {
                messageSendService.orderUnLockStock(unLockDTOS, order.getId());
            }
            // 发送层级订单费用重新计算
            if (erpDistributorData.getTreeNode() == 1 && erpDistributorData.getDirectFlag().equals(Constant.DIRECT_FLAG_0)) {
                messageSendService.orderChangeCost(order.getId());
            }
            // 发送商品订单销量变更
            if (!CollectionUtils.isEmpty(saleDTOS)) {
                saleDTOS.forEach(saleDTO -> {
                    if (saleDTO.getSaleNum() > 0) {
                        saleDTO.setChangeType(Constant.CHANGE_TYPE_1);
                    } else {
                        saleDTO.setSaleNum(Math.abs(saleDTO.getSaleNum()));
                        saleDTO.setChangeType(Constant.CHANGE_TYPE_2);
                    }
                });
                messageSendService.orderGoodsSale(saleDTOS, order.getId());
            }
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
            throw e;
        } finally {
            // 当发生异常时，订单变更失败，需考虑反锁已锁定库存 ERP订单变更
            if (error && erpDistributorData != null && !CollectionUtils.isEmpty(lockDTOS)) {
                messageSendService.orderUnLockStock(lockDTOS, erpDistributorData.getOrderId());
            }
        }
    }

    /**
     * 根据反锁数据库存反锁
     * 
     * @param unLockDTOS
     */
    public void unLockStock(List<ItemStockLockDTO> unLockDTOS) {
        com.bat.dubboapi.warehouse.common.Response response = warehouseStockServiceRpc.unLockStock(unLockDTOS);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 取消工厂订单
     * 
     * @param orderId
     * @param remark
     */
    public com.bat.dubboapi.thirdparty.common.Response<Boolean> cancelOrderToFactory(String factoryCode,
        Integer orderId,String orderNo, String remark) {
        if (StringUtils.isNotBlank(factoryCode) && factoryCode.equals(FactoryEnum.LHW.name())) {
            return com.bat.dubboapi.thirdparty.common.Response.of(true);
        }else if (StringUtils.isNotBlank(factoryCode) && factoryCode.equals(FactoryEnum.DH.name())) {
            return com.bat.dubboapi.thirdparty.common.Response.of(true);
        }else if (StringUtils.isNotBlank(factoryCode) && factoryCode.equals(FactoryEnum.DH_OLK.name())) {
            return com.bat.dubboapi.thirdparty.common.Response.of(true);
        }else if(StringUtils.isNotBlank(factoryCode) && factoryCode.equals(FactoryEnum.MK.name())) {
            return thirdPartyMaikeServiceRpc.cancelOrderToFactory(factoryCode, orderId, remark);
        }else if(StringUtils.isNotBlank(factoryCode) && factoryCode.equals(FactoryEnum.KDS_FK.name())){
            return feiKuaiServiceRpc.cancelOrderToFactory(factoryCode,orderNo);
        }
        return com.bat.dubboapi.thirdparty.common.Response.of(true);
    }

    /**
     * 兑换码核销
     * 
     * @param distributorId
     * @param platform
     * @param code
     * @param orderId
     * @return
     */
    public com.bat.dubboapi.thirdparty.common.Response writeOffCode(Integer distributorId, String platform,
        String code, Integer orderId) {
        return thirdPartyThirdCodeOrderServiceRpc.writeOffCode(distributorId, platform, code, orderId);
    }

    /**
     * 根据发货单ids查找发货单日志
     * 
     * @param ids
     * @param operateType
     * @return
     */
    public com.bat.dubboapi.thirdparty.common.Response<List<OrderDeliverBillLogDTO>>
        findOrderDeliverBillLogByIdsAndOperateType(List<Integer> ids, String operateType) {
        return logServiceRpc.findOrderDeliverBillLogByIdsAndOperateType(ids, operateType);
    }

    /**
     * 取消或作废erp订单
     * 
     */
    public void orderCancelErp(OrderDistributorDataDO erpDistributorData, OrderExtendDataDO orderExtendDataDO) {
        Short erpCancelType = Constant.ERP_CANCEL_TYPE_2;
        if (erpDistributorData.getOrderStatus().equals(OrderStatus.PENDING.getValue())) {
            erpCancelType = Constant.ERP_CANCEL_TYPE_1;
        }
        com.bat.dubboapi.thirdparty.common.Response response =
            thirdPartyOrderServiceErpRpc.cancelOrder(orderExtendDataDO.getOrderErpNo(), erpCancelType,
                orderExtendDataDO.getOrderId(), erpDistributorData.getCancelRemark());
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public OrderPromotionRpcDTO promotionByOrderPromotionIds(OrderPromotionQry qry) {
        OrderPromotionRpcQry rpcQry = new OrderPromotionRpcQry();
        BeanUtils.copyProperties(qry, rpcQry);
        com.bat.dubboapi.promotion.common.Response<OrderPromotionRpcDTO> response =
            promotionServiceRpc.promotionByOrderPromotionIds(rpcQry);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取定制订单同步工厂时间
     * 
     * @return
     */
    public Integer getOrderAsynFactoryTime(Integer distributorId) {
        try {
            com.bat.dubboapi.system.common.Response<FactorySettingOrderInvalidRpcDTO> response =
                factorySettingServiceRpc.getFactorySettingOrderInvalid(distributorId);
            if (response.isSuccess()) {
                return response.getData().getInvalid();
            } else {
                return 14;
            }
        } catch (Exception e) {
            return 14;
        }
    }

    /**
     * 根据分销商id 代金券id集合 查询可用的代金券列表
     * 
     * @param distributorId
     * @param rebateVoucherIds
     * @return
     */
    public List<RebateVoucherRpcDTO> listRebateVoucher(Integer distributorId, List<Integer> rebateVoucherIds) {
        com.bat.dubboapi.promotion.common.Response<List<RebateVoucherRpcDTO>> listResponse =
            promotionServiceRpc.listRebateVoucher(distributorId, rebateVoucherIds);
        if (!listResponse.isSuccess()) {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
        return listResponse.getData();
    }

    public String getBaseSettingByKey(String key, String defaultValue) {
        try {
            com.bat.dubboapi.system.common.Response<BaseSettingRpcDTO> response =
                baseSettingServiceRpc.getBaseSettingByKey(key);
            if (!response.isSuccess()) {
                log.error(response.getErrCode(), response.getErrMessage());
                return defaultValue;
            }
            BaseSettingRpcDTO data = response.getData();
            if (data == null || data.getValue() == null) {
                return defaultValue;
            } else {
                return data.getValue();
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * <h2>分销商可视商品</h2>
     * 
     * @param userId
     */
    public DistributorGoodsControlRpcDTO distributorGoodsControl(Integer userId) {
        com.bat.dubboapi.distributor.common.Response<
            DistributorGoodsControlRpcDTO> distributorGoodsControlRpcDTOResponse =
                distributorServiceRpc.distributorGoodsControl(userId);
        if (!distributorGoodsControlRpcDTOResponse.isSuccess()) {
            throw OrderException.buildException(distributorGoodsControlRpcDTOResponse.getErrCode(),
                distributorGoodsControlRpcDTOResponse.getErrMessage());
        }
        return distributorGoodsControlRpcDTOResponse.getData();

    }

    public List<GoodsAreVisibleRpcDTO> goodsAreVisible(List<Integer> orderGoodsList) {
        Response<List<GoodsAreVisibleRpcDTO>> listResponse = goodsServiceRpc.goodsAreVisible(orderGoodsList);
        if (!listResponse.isSuccess()) {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
        return listResponse.getData();
    }

    public DistributorPayWayRpcDTO distributorPaymentWayById(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorPayWayRpcDTO> payWayRpcDTOResponse =
            distributorServiceRpc.distributorPaymentWayById(distributorId);
        if (payWayRpcDTOResponse.isSuccess()) {
            return payWayRpcDTOResponse.getData();
        }
        return null;
    }

    public Integer findATier1Distributor(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<List<DistributorTreePathRpcDTO>> distributorTreePaths =
            distributorServiceRpc.getDistributorTreePaths(distributorId);
        // 如果是成功且返回值不为空，则取出最大的分销商id返回
        if (distributorTreePaths.isSuccess() && ObjectUtils.isNotEmpty(distributorTreePaths.getData())) {
            return distributorTreePaths.getData().stream()
                .max(Comparator.comparing(DistributorTreePathRpcDTO::getTreeNode)).get().getDistributorAncestorId();
        } else {
            return null;
        }
    }
}
