package com.bat.order.service.deliver;

import static com.bat.order.service.common.constant.OrderInfoConstant.ORDER_DELIVER_STATUS_UN_SHIPPED;
import static com.bat.order.service.common.error.OrderDeliverErrorCode.B_ORDER_DELIVER_BILL_NOT_EXISTS;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.error.*;
import com.bat.order.service.deliver.convertor.OrderDeliveryConvertor;
import com.bat.order.service.deliver.validator.OrderDeliverValidator;
import com.bat.order.service.message.MessageSendService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.google.common.collect.Lists;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.common.ResponseBaseBean;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.*;
import com.bat.dubboapi.order.order.dto.OrderCancelSyncParam;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyOrderServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyOutboundSyncErpCmd;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyPurchaseOrderDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.ErpPurchaseOrderOutboundCmd;
import com.bat.dubboapi.thirdparty.order.ThirdPartyOrderDeliverServiceRpc;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.dubboapi.thirdparty.order.dto.OutDataDeliverOrderModel;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.cost.OrderDistributorCostServiceI;
import com.bat.order.api.data.OrderCustomerDataServiceI;
import com.bat.order.api.data.OrderDistributorDataServiceI;
import com.bat.order.api.data.OrderExtendDataServiceI;
import com.bat.order.api.deliver.OrderDeliverBillDetailServiceI;
import com.bat.order.api.deliver.OrderDeliverBillServiceI;
import com.bat.order.api.deliver.OrderDeliveryServiceI;
import com.bat.order.api.deliver.dto.OrderDeliverBillResp;
import com.bat.order.api.deliver.dto.OrderGoodsDeliverBean;
import com.bat.order.api.order.OrderGoodsDiyServiceI;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.stock.api.OrderGoodsStockServiceI;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.enumtype.OrderStatus;
import com.bat.order.service.common.error.*;
import com.bat.order.service.stock.executor.WarehouseStockQryExe;

@DubboService
public class OrderDeliveryServiceRpcImpl implements OrderDeliveryServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDeliveryServiceRpcImpl.class);

    @Autowired
    private OrderExtendDataServiceI orderExtendDataServiceI;

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @Autowired
    private OrderDeliverBillServiceI orderDeliverBillServiceI;

    @DubboReference(check = false, timeout = 5000)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @Autowired
    private OrderDeliveryServiceI orderDeliveryServiceI;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderDistributorDataServiceI orderDistributorDataServiceI;

    @Autowired
    private OrderDistributorCostServiceI orderDistributorCostServiceI;

    @Autowired
    private OrderCustomerDataServiceI orderCustomerDataServiceI;

    @Autowired
    private OrderGoodsStockServiceI orderGoodsStockServiceI;

    @DubboReference(check = false, timeout = 20000, retries = 0)
    private ThirdPartyOrderDeliverServiceRpc thirdPartyOrderDeliverServiceRpc;

    @DubboReference(check = false, timeout = 20000, retries = 0)
    private ThirdPartyOrderServiceErpRpc thirdPartyOrderServiceErpRpc;

    @CreateCache
    private Cache<String, String> keyCache;

    @Autowired
    private OrderGoodsDiyServiceI orderGoodsDiyServiceI;

    @Autowired
    private OrderDeliverBillDetailServiceI orderDeliverBillDetailServiceI;

    @Resource
    private OrderConfig orderConfig;

    @Autowired
    private OrderDeliverValidator orderDeliverValidator;

    @Autowired
    private WarehouseStockQryExe warehouseStockQryExe;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private OrderDeliveryConvertor orderDeliveryConvertor;

    @Resource
    private MessageSendService sendService;

    @Override
    public Response<Integer> queryFactoryTrackingNumber(String orderNo) {
        // 根据工厂单号查询订单扩展数据表
        OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderFactoryNo(orderNo);
        if (orderExtendDataDO == null) {
            // 根据订单id查询扩展数据
            orderExtendDataDO = orderExtendDataServiceI.getByOrderId(Integer.valueOf(orderNo));
            if (orderExtendDataDO == null) {
                LOGGER.info("手动发货查询不到订单号{}", orderNo);
            }
        }
        return Response.of(orderExtendDataDO.getOrderId());
    }

    @Override
    @Transactional
    public ResponseBaseBean deliverOrder(OrderDeliveryCmd orderDeliveryCmd) {
        LOGGER.info("工厂发货、物流参数{}", JSON.toJSONString(orderDeliveryCmd));
        try {
            if (orderDeliveryCmd.getExpressTime() == null || orderDeliveryCmd.getExpressTime() <= 0) {
                orderDeliveryCmd.setExpressTime(System.currentTimeMillis());
            }
            // 根据工厂单号查询订单扩展数据表
            OrderExtendDataDO orderExtendDataDO =
                orderExtendDataServiceI.getByOrderFactoryNo(orderDeliveryCmd.getOrderNo());
            if (orderExtendDataDO == null) {
                if (orderDeliveryCmd.getManualFlag()) {
                    // 根据订单id查询扩展数据
                    orderExtendDataDO =
                        orderExtendDataServiceI.getByOrderId(Integer.valueOf(orderDeliveryCmd.getOrderNo()));
                    if (orderExtendDataDO == null) {
                        LOGGER.info("手动发货查询不到订单号{}", orderDeliveryCmd.getOrderNo());
                        return ResponseBaseBean.responseBean(OrderInfoErrorCode.O_ORDER_FACTORY_NO_ERROR_CODE,
                            MessageUtils.get(OrderInfoErrorCode.O_ORDER_FACTORY_NO_ERROR_MSG));
                    }
                } else {
                    LOGGER.info("查询不到该工厂单号{}", orderDeliveryCmd.getOrderNo());
                    // 非bat订单、调bat的第三方接口
                    return ResponseBaseBean.responseBean(OrderInfoErrorCode.O_ORDER_FACTORY_NO_ERROR_CODE,
                        MessageUtils.get(OrderInfoErrorCode.O_ORDER_FACTORY_NO_ERROR_MSG));
                }
            }

            // 根据订单id查询订单信息
            OrderInfoDO orderInfo = orderInfoServiceI.getById(orderExtendDataDO.getOrderId());
            // 如果订单的状态不是待发货，就返回错误
            if (orderInfo.getDeliverStatus() != 1) {
                return ResponseBaseBean.responseBean(OrderInfoErrorCode.THE_ORDER_IS_NOT_IN_PENDING_STATUS,
                    MessageUtils.get(OrderInfoErrorCode.THE_ORDER_IS_NOT_IN_PENDING_MSG));
            }

            OutDataDeliverOrderModel outDataDeliverOrderModel = new OutDataDeliverOrderModel();
            ResponseBaseBean response = new ResponseBaseBean();
            // 已发货
            OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderExtendDataDO.getOrderId());
            // 重构后历史订单同步给第三方还是要传原来的订单id
            String orderNo = StringUtils.isBlank(orderInfoDO.getOrderNo()) ? String.valueOf(orderInfoDO.getId())
                : orderInfoDO.getOrderNo();
            OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataServiceI
                .listByCondition(orderExtendDataDO.getOrderId(), null, OrderInfoConstant.ORDER_ERP_FLAG_YES).get(0);
            // 设置物流信息
            LogisticsRpcDTO logisticsRpcDTO = null;
            LogisticsRpcQry qry = new LogisticsRpcQry();
            qry.setLogisticsKdnCode(orderDeliveryCmd.getExpressCode());
            qry.setDistributorId(orderDistributorDataDO.getDistributorId());
            com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsResponse =
                systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry);
            if (logisticsResponse == null) {
                return ResponseBaseBean.responseBean(11, "找不到对应的B2B配送方式");
            }
            if (!logisticsResponse.isSuccess()) {
                LOGGER.error("匹配工厂和B2B配送方式异常，{}", JSON.toJSONString(logisticsResponse));
                return ResponseBaseBean.responseBean(11, logisticsResponse.getErrMessage());
            }
            List<LogisticsRpcDTO> logisticsRpcDTOList = logisticsResponse.getData();
            OrderDeliveryDO orderDeliveryDO = orderDeliveryServiceI.getByOrderId(orderInfoDO.getId());
            if (logisticsRpcDTOList != null && logisticsRpcDTOList.size() > 0) {
                for (LogisticsRpcDTO logistics : logisticsRpcDTOList) {
                    if (logistics.getId() - orderDeliveryDO.getDistributionId() == 0) {
                        logisticsRpcDTO = logistics;
                        break;
                    }
                }
                if (logisticsRpcDTO == null) {
                    logisticsRpcDTO = logisticsRpcDTOList.get(0);
                }
            } else {
                LogisticsRpcQry qry1 = new LogisticsRpcQry();
                qry1.setLogisticsId(orderDeliveryDO.getDistributionId());
                com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsRpcDTOResponse =
                        systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry1);
                if (logisticsResponse == null || !logisticsResponse.isSuccess()) {
                    return ResponseBaseBean.responseBean(11, "");
                }
                logisticsRpcDTO = logisticsRpcDTOResponse.getData().get(0);
                if(StringUtils.isNotEmpty(orderDeliveryCmd.getExpressName())){
                    logisticsRpcDTO.setName(orderDeliveryCmd.getExpressName());
                }
                if(StringUtils.isNotEmpty(orderDeliveryCmd.getExpressCode())){
                    logisticsRpcDTO.setLogisticsKdnCode(orderDeliveryCmd.getExpressCode());
                }
            }
            if (OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED.equals(orderInfoDO.getDeliverStatus())) {
                // 重置物流信息
                List<OrderDeliverBillDO> orderDeliverBillDOList =
                    orderDeliverBillServiceI.listByOrderId(orderInfoDO.getId());
                if (orderDeliverBillDOList == null) {
                    return ResponseBaseBean.responseBean(MaikeErrorConstant.DeliverySynOrderNullCode,
                        MaikeErrorConstant.DeliverySynOrderNullMsg);
                }
                OrderExtendDataDO finalOrderExtendDataDO = orderExtendDataDO;
                LogisticsRpcDTO finalLogisticsRpcDTO = logisticsRpcDTO;
                orderDeliverBillDOList.stream().forEach(orderDeliverBillDO -> {
                    orderDeliverBillDO.setDistributionId(finalLogisticsRpcDTO.getId());
                    orderDeliverBillDO.setDistributionName(finalLogisticsRpcDTO.getName());
                    orderDeliverBillDO.setDistributionCode(finalLogisticsRpcDTO.getLogisticsKdnCode());
                    orderDeliverBillDO.setLogisticsNo(orderDeliveryCmd.getExpressNo());
                    orderDeliverBillDO.setUpdateTime(new Date());
                    orderDeliverBillDO.setDeliverTime(new Date(orderDeliveryCmd.getExpressTime()));
                    orderDeliverBillServiceI.update(orderDeliverBillDO);
                    // 同步第三方发货
                    if (StringUtils.isNotBlank(finalOrderExtendDataDO.getOrderThirdpartyNo())) {
                        // 改为队列
                        messageSendService.syncLogisticToThirdNew(orderDeliverBillDO.getId());
                    }
                });

                outDataDeliverOrderModel.setDeliverNo(String.valueOf(orderDeliveryDO.getId()));
                response.setData(outDataDeliverOrderModel);
                return response;
            }
            // 获取b2b订单货品明细
            List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(orderInfoDO.getId());
            // ErpDeliverOrderRequest rockPrinceRequest = new ErpDeliverOrderRequest();
            List<OrderGoodsDeliverBean> deliverOrderDetails = new ArrayList<>();
            for (OrderGoodsDO orderGoodsDO : orderGoodsDOList) {
                // 定制都是全部发货
                OrderGoodsDeliverBean orderGoodsDeliverBean = new OrderGoodsDeliverBean();
                orderGoodsDeliverBean.setCount(orderGoodsDO.getItemCount());
                orderGoodsDO.setUnDeliverCount(0);
                orderGoodsDO.setDeliverCount(orderGoodsDO.getItemCount());
                orderGoodsDeliverBean.setOrderGoodsDO(orderGoodsDO);
                deliverOrderDetails.add(orderGoodsDeliverBean);
            }
            // 批量修改
            orderGoodsServiceI.batchUpdate(orderGoodsDOList);
            if (deliverOrderDetails == null || deliverOrderDetails.size() == 0) {
                throw OrderException.buildException("找不到商品明细");
            }
            com.bat.order.api.common.response.ResponseBaseBean responseBaseBean = orderDeliverBillServiceI
                .createBill(deliverOrderDetails, OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED,
                    OrderDeliverConstant.ORDER_DELIVER_LOGISTICS_STATUS_ON_WAY, logisticsRpcDTO.getName(),
                    orderDeliveryCmd.getExpressNo(), orderInfoDO, logisticsRpcDTO.getId(),
                    OrderDeliverConstant.ORDER_DELIVER_TYPE_SALE, orderDeliveryDO, null, null,
                    new Date(orderDeliveryCmd.getExpressTime()), true,
                    StringUtils.isNotBlank(orderExtendDataDO.getOrderThirdpartyNo())
                        ? OrderDeliverConstant.ORDER_DELIVER_NEED_PUSH_YES
                        : OrderDeliverConstant.ORDER_DELIVER_NEED_PUSH_NO,
                    orderDeliveryCmd.getExpressCode());
            // 批量修改订单状态为已发货
            orderInfoDO.setDeliverStatus(OrderInfoConstant.ORDER_DELIVER_STATUS_SHIPPED);
            orderInfoDO.setDeliverTime(new Date());
            orderInfoServiceI.update(orderInfoDO);
            // 为后面记录推送状态做前期准备

            LOGGER.info("工厂-发货单同步-同步后订单id{}-订单的状态为{},创建发货流水响应{}", orderInfoDO.getId(), orderInfoDO.getDeliverStatus(),
                JSON.toJSONString(responseBaseBean));
            OrderDeliverBillResp billResp = (OrderDeliverBillResp)responseBaseBean.getData();
            OrderDeliverBillDO orderDeliverBillDO = billResp.getOrderDeliverBillDO();
            List<OrderDeliverBillDetailDO> deliverBillDetailDOList = billResp.getDeliverBillDetailDOList();
            if (StringUtils.isNotBlank(orderExtendDataDO.getOrderThirdpartyNo())) {
                // 同步第三方发货
                /* Response<Short> logisticsToThirdPartyResp = thirdPartyOrderDeliverServiceRpc.syncLogisticsToThirdParty(
                    orderInfoDO.getId(), orderExtendDataDO.getOrderThirdpartyNo(), logisticsRpcDTO.getName(),
                    orderLogistics, logisticsRpcDTO.getId(), orderInfoDO.getOrderSource());
                if (logisticsToThirdPartyResp == null || !logisticsToThirdPartyResp.isSuccess()) {
                    // 不抛异常、不返回错误
                    orderDeliverBillDO.setPushStatus(OrderDeliverConstant.ORDER_DELIVER_PUSH_STATUS_FAIL);
                }else{
                    Short pushStatus = logisticsToThirdPartyResp.getData();
                    orderDeliverBillDO.setPushStatus(pushStatus);
                }
                orderDeliverBillDO.setUpdateTime(new Date());
                orderDeliverBillServiceI.update(orderDeliverBillDO);*/
                // 改为队列
                messageSendService.syncLogisticToThirdNew(orderDeliverBillDO.getId());
            }
            outDataDeliverOrderModel.setDeliverNo(String.valueOf(orderDeliverBillDO.getId()));
            response.setData(outDataDeliverOrderModel);
            // 处理采购销售单
            List<OrderGoodsDiyDO> orderGoodsDiyDOList = orderGoodsDiyServiceI.listByOrderId(orderInfoDO.getId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<OrderGoodsDetailDTO> orderGoodsDetailDTOS =
                BeanUtils.copyList(deliverBillDetailDOList, OrderGoodsDetailDTO.class);
            String time = simpleDateFormat.format(orderDeliverBillDO.getCreateTime());
            if (StringUtils.isBlank(orderDeliverBillDO.getDeliverStkNo())) {
                // 改为队列
                DiyPurchaseOrderDTO diyPurchaseOrderDTO = orderDeliveryConvertor.getDiyPurchaseOrderDTO(
                    orderExtendDataDO.getOrderErpNo(), orderDeliverBillDO.getId(),
                    orderGoodsDiyDOList.get(0).getManufactors(), orderGoodsDetailDTOS, time);
                OrderDistributorCostDO orderDistributorCostDO = orderDistributorCostServiceI
                    .getByOrderIdAndDistributorId(orderInfoDO.getId(), orderDistributorDataDO.getDistributorId());
                // 组装参数
                DiyOutboundSyncErpCmd diyOutboundSyncErpCmd = orderDeliveryConvertor.getSyncErpOutboundParam(
                    orderExtendDataDO.getOrderErpNo(), orderDeliverBillDO, orderGoodsDiyDOList.get(0).getManufactors(),
                    orderGoodsDetailDTOS, time, orderDistributorCostDO.getDistributionAmount());
                ErpPurchaseOrderOutboundCmd outboundCmd = new ErpPurchaseOrderOutboundCmd();
                outboundCmd.setDiyOutboundSyncErpCmd(diyOutboundSyncErpCmd);
                outboundCmd.setDiyPurchaseOrderDTO(diyPurchaseOrderDTO);
                // 暂时停止出库单同步erp，做定时同步
                messageSendService.syncErpPurchaseAndOutboundOrderNew(outboundCmd);

            }
            /*if (StringUtils.isBlank(orderDeliverBillDO.getDeliverErpNo())) {
                OrderDistributorCostDO orderDistributorCostDO = orderDistributorCostServiceI
                    .getByOrderIdAndDistributorId(orderInfoDO.getId(), orderDistributorDataDO.getDistributorId());
                //组装参数
                DiyOutboundSyncErpCmd diyOutboundSyncErpCmd = orderDeliveryConvertor.getSyncErpOutboundParam(orderExtendDataDO.getOrderErpNo(), orderDeliverBillDO, orderGoodsDiyDOList.get(0).getManufactors()
                        , orderGoodsDetailDTOS, time, orderDistributorCostDO.getDistributionAmount());
               messageSendService.syncOutStockToERP(diyOutboundSyncErpCmd);
            }*/
            /* orderDeliverBillDO.setUpdateTime(new Date());
            orderDeliverBillServiceI.update(orderDeliverBillDO);*/
        } catch (OrderException e) {
            e.printStackTrace();
            LOGGER.info("工厂发货失败、原因是{}", e.getMsg());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseBaseBean.responseBean(MaikeErrorConstant.DeliverySynFailCode, e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("工厂发货失败、原因是{}", e.getMessage());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseBaseBean.responseBean(MaikeErrorConstant.DeliverySynFailCode,
                MaikeErrorConstant.DeliverySynFailMsg);
        }

        return ResponseBaseBean.responseBean();
    }

    @Override
    public com.bat.dubboapi.order.common.Response<OrderDeliveryRpcQryDTO> getByOrderId(Integer orderId) {
        OrderDeliveryDO orderDeliveryDO = orderDeliveryServiceI.getByOrderId(orderId);
        com.bat.dubboapi.system.common.Response<LogisticsRpcDTO> response = systemLogisticsServiceRpc.getLogisticsById(orderDeliveryDO.getDistributionId());
        OrderDeliveryRpcQryDTO dto = BeanUtils.copy(orderDeliveryDO, OrderDeliveryRpcQryDTO.class);
        dto.setLogisticsErpId(response.getData().getLogisticsErpId());
        return com.bat.dubboapi.order.common.Response.of(dto);
    }

    /**
     * ERP同步出库单到B2B
     *
     * @param erpDeliverOrderRequest
     */
    @Transactional
    @Override
    public ResponseBaseBean syncOutboundOrderFromERP(ErpDeliverOrderRequest erpDeliverOrderRequest) {
        ResponseBaseBean responseBaseBean = new ResponseBaseBean();
        List<ErpDeliverGoodsItemEntry> erpDeliverGoodsItemEntryList = null;
        LOGGER.info("erp->B2B出库单、参数{}", JSON.toJSONString(erpDeliverOrderRequest));
        try {
            List<OrderDeliverBillDO> orderDeliverBillDOList =
                orderDeliverBillServiceI.listByDeliverErpNo(erpDeliverOrderRequest.getDeliverOrderNo());
            List<Integer> orderIds =
                orderDeliverBillDOList.stream().map(OrderDeliverBillDO::getOrderId).collect(Collectors.toList());

            List<Integer> deliverGoodsIds = new ArrayList<>();
            Map<Integer, OrderDeliverBillDO> deliverGoodsMap = new HashMap<>();
            // 之前同步过出库单、数量变少、需要锁上或者增加
            boolean existFlag = false;
            if (orderDeliverBillDOList.size() > 0) {
                for (OrderDeliverBillDO orderDeliverBillDO : orderDeliverBillDOList) {
                    deliverGoodsMap.put(orderDeliverBillDO.getOrderId(), orderDeliverBillDO);
                    deliverGoodsIds.add(orderDeliverBillDO.getId());
                }
                existFlag = true;
            }
            // 根据订单编号对发货明细进行分组
            List<ErpDeliverOrderDetailRequest> deliverOrderDetails = erpDeliverOrderRequest.getDeliverOrderDetails();
            Map<String, List<ErpDeliverOrderDetailRequest>> deliverMap =
                deliverOrderDetails.stream().collect(Collectors.groupingBy(ErpDeliverOrderDetailRequest::getOrderNo));
            // 生成发货单
            List<String> orderNos = new ArrayList<>(deliverMap.keySet());
            // 返回ERP
            erpDeliverGoodsItemEntryList = new ArrayList<>();
            for (String orderNo : orderNos) {// 迭代每一笔订单
                // ERP的订单号
                OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderErpNo(orderNo);
                if (orderExtendDataDO == null) {
                    LOGGER.info("非B2B单号{},忽略发货单", orderNo);
                    continue;
                }
                OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataServiceI
                    .listByCondition(orderExtendDataDO.getOrderId(), null, OrderInfoConstant.ORDER_ERP_FLAG_YES).get(0);
                // 订单已取消，不能同步销售出库单
                if (OrderInfoConstant.ORDER_STATUS_CANCEL.equals(orderDistributorDataDO.getOrderStatus())) {
                    throw OrderException.buildException(OrderInfoErrorCode.ORDER_STATUS_HAS_BEEN_CANCEL,
                        orderNo + MessageUtils.get(OrderInfoErrorCode.ORDER_STATUS_HAS_BEEN_CANCEL));
                }
                OrderDeliverBillDO orderDeliverBillDO = deliverGoodsMap.get(orderExtendDataDO.getOrderId());
                // if (order.getOrderStatus() == AllDeliver && deliverGoods == null) {// 该订单已全部发货
                // continue;
                // }
                OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderExtendDataDO.getOrderId());
                // 定制订单发货无需同步
                if (orderConfig.getDiyOrderTypeId().equals(orderInfoDO.getOrderTypeId())) {
                    continue;
                }
                List<OrderDeliverBillDetailDO> deliverBillDetailDOList = new ArrayList<>();
                Map<Integer, List<OrderDeliverBillDetailDO>> deliverGoodsDetailMap = new HashMap<>();
                if (orderDeliverBillDO != null) {
                    deliverGoodsMap.remove(orderExtendDataDO.getOrderId(), orderDeliverBillDO);
                    deliverBillDetailDOList =
                        orderDeliverBillDetailServiceI.listByOrderDeliverBillId(orderDeliverBillDO.getId());
                    if (deliverBillDetailDOList != null && deliverBillDetailDOList.size() > 0) {
                        /* for (OrderDeliverBillDetailDO deliverGoodsDetail : deliverBillDetailDOList) {
                            List<OrderDeliverBillDetailDO> list = deliverGoodsDetailMap.get(deliverGoodsDetail.getSerialNumber());
                            if (list == null) {
                                list = new ArrayList<>();
                                deliverGoodsDetailMap.put(deliverGoodsDetail.getSerialNumber(), list);
                            }
                            list.add(deliverGoodsDetail);
                        }*/
                        deliverGoodsDetailMap = deliverBillDetailDOList.stream()
                            .collect(Collectors.toMap(OrderDeliverBillDetailDO::getSerialNumber, Lists::newArrayList,
                                (List<OrderDeliverBillDetailDO> oldList, List<OrderDeliverBillDetailDO> newList) -> {
                                    oldList.addAll(newList);
                                    return oldList;
                                }));
                    }
                }

                List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(orderInfoDO.getId());// 获取b2b订单货品明细
                /*  Map<String, List<OrderGoodsDO>> orderGoodsMap = new HashMap<>();// 记录相同物料id的货品记录（编码进行分组）
                for (OrderGoodsDO orderGoods : orderGoodsDOList) {
                    List<OrderGoodsDO> orderGoodss = orderGoodsMap.get(orderGoods.getItemCode());
                    if (orderGoodss == null) {
                        orderGoodss = new ArrayList<>();
                        orderGoodsMap.put(orderGoods.getItemCode(), orderGoodss);
                    }
                    orderGoodss.add(orderGoods);
                }*/
                Map<Integer, OrderGoodsDO> orderGoodsDOMap = orderGoodsDOList.stream()
                    .collect(Collectors.toMap(OrderGoodsDO::getSerialNumber, orderGoodsDO -> orderGoodsDO));
                // 记录相同物料id的发货明细
                /*     Map<String, ErpDeliverOrderDetailRequest> deliverOrderMap =
                        new HashMap<String, ErpDeliverOrderDetailRequest>();*/
                List<ErpDeliverOrderDetailRequest> erpDeliverOrderList = deliverMap.get(orderNo);
                /* for (ErpDeliverOrderDetailRequest erpDeliverOrderDetailRequest : erpDeliverOrderList) {
                    ErpDeliverOrderDetailRequest orderDetailRequest =
                            deliverOrderMap.get(erpDeliverOrderDetailRequest.getItemNo());
                    if (orderDetailRequest == null) {
                        deliverOrderMap.put(erpDeliverOrderDetailRequest.getItemNo(), erpDeliverOrderDetailRequest);
                    } else {
                        orderDetailRequest.setNum(orderDetailRequest.getNum() + erpDeliverOrderDetailRequest.getNum());
                    }
                }*/
                List<OrderGoodsDeliverBean> delivers = new ArrayList<OrderGoodsDeliverBean>();
                Map<Integer, Integer> itemsDeliver = new HashMap<>();
                for (int x = 0; x < erpDeliverOrderList.size(); x++) {
                    /* Integer itemDeviverCount = 0;
                    Integer itemUnDeliverCount = 0;
                    Integer deliverCount = 0;
                    Integer unDeliverCount = 0;*/
                    /* ErpDeliverOrderDetailRequest deliverRequest = deliverOrderMap.get(key);
                    List<OrderGoodsDO> orderGoodss = orderGoodsMap.get(key);
                    List<OrderDeliverBillDetailDO> details = deliverGoodsDetailMap.get(key);
                    deliverGoodsDetailMap.remove(key);*/
                    ErpDeliverOrderDetailRequest erpDeliverOrderDetailRequest = erpDeliverOrderList.get(x);

                    OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(erpDeliverOrderDetailRequest.getOrderEntryId());
                    if (orderGoodsDO == null) {
                        LOGGER.info("erp出库、非B2B的明细{}", erpDeliverOrderDetailRequest.getOrderEntryId());
                        throw OrderException.buildException("非B2B订单明细",
                            erpDeliverOrderDetailRequest.getOrderEntryId() + "非B2B订单明细");
                    }
                    // 已同步过发货单的时候，统计已同步的数量
                    List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOList =
                        deliverGoodsDetailMap.get(orderGoodsDO.getSerialNumber());
                    if (orderDeliverBillDetailDOList != null && orderDeliverBillDetailDOList.size() > 0) {
                        LOGGER.info("重推出库单、开始回调订单明细商品出库数量orderGoods{},orderDeliverBillDetailDOList{}",
                            JSON.toJSONString(orderGoodsDO), JSON.toJSONString(orderDeliverBillDetailDOList));
                        for (int i = 0; i < orderDeliverBillDetailDOList.size(); i++) {
                            orderGoodsDO.setDeliverCount(
                                orderGoodsDO.getDeliverCount() - orderDeliverBillDetailDOList.get(i).getCount());
                            orderGoodsDO.setUnDeliverCount(
                                orderGoodsDO.getUnDeliverCount() + orderDeliverBillDetailDOList.get(i).getCount());
                        }
                        LOGGER.info("重推出库单、处理后回调订单明细商品出库数量orderGoods{},orderDeliverBillDetailDOList{}",
                            JSON.toJSONString(orderGoodsDO), JSON.toJSONString(orderDeliverBillDetailDOList));
                        // 再次推单、该明细还在、需要移除（这个map大于0下一步会重置orderGoods明细的未发数量）
                        deliverGoodsDetailMap.remove(orderGoodsDO.getSerialNumber());
                    }
                    /*if (CollectionUtils.isNotEmpty(details)) {// 已同步过发货单的时候，统计已同步的数量
                        for (OrderDeliverBillDetailDO detail : details) {
                            deliverCount = deliverCount + detail.getCount();
                            unDeliverCount = unDeliverCount + detail.getCount();
                        }
                    }
                    Integer itemId = null;
                    for (OrderGoodsDO orderGoods : orderGoodss) {
                        if (itemId == null) {
                            itemId = orderGoods.getItemId();
                        }
                        if (orderGoods.getDeliverCount() - deliverCount >= 0) {
                            orderGoods.setDeliverCount(orderGoods.getDeliverCount() - deliverCount);
                            orderGoods.setUnDeliverCount(orderGoods.getUnDeliverCount() + deliverCount);
                            itemUnDeliverCount = itemUnDeliverCount + orderGoods.getUnDeliverCount();
                            deliverCount = 0;
                        } else {
                            deliverCount = deliverCount - orderGoods.getDeliverCount();
                            orderGoods.setDeliverCount(0);
                            orderGoods.setUnDeliverCount(orderGoods.getItemCount());
                            itemUnDeliverCount = itemUnDeliverCount + orderGoods.getItemCount();
                        }
                    }*/

                    if (orderGoodsDO.getUnDeliverCount() < erpDeliverOrderDetailRequest.getNum()) {// 发货数量大于未发数量时
                        String errMsg =
                            "订单为" + orderNo + ",itemNo为" + orderGoodsDO.getItemCode() + "超出了可发货数量" + ",可发货数量为"
                                + orderGoodsDO.getUnDeliverCount() + ",发货数量为" + erpDeliverOrderDetailRequest.getNum();
                        throw OrderException.buildException(errMsg, errMsg);
                    }

                    OrderGoodsDeliverBean deliverBean = new OrderGoodsDeliverBean();
                    deliverBean.setId(orderGoodsDO.getId());
                    deliverBean.setItemId(orderGoodsDO.getItemId());
                    deliverBean.setOrderGoodsDO(orderGoodsDO);
                    deliverBean.setCount(erpDeliverOrderDetailRequest.getNum());
                    deliverBean.setWarehouseNo(erpDeliverOrderDetailRequest.getNo());
                    delivers.add(deliverBean);
                    orderGoodsDO
                        .setDeliverCount(orderGoodsDO.getDeliverCount() + erpDeliverOrderDetailRequest.getNum());
                    orderGoodsDO
                        .setUnDeliverCount(orderGoodsDO.getUnDeliverCount() - erpDeliverOrderDetailRequest.getNum());
                }
                if (delivers.size() <= 0) {
                    continue;
                }
                com.bat.order.api.common.response.ResponseBaseBean billResponse =
                    orderDeliverBillServiceI.createBill(delivers, erpDeliverOrderRequest.getDocumentStatus(),
                        OrderDeliverConstant.ORDER_DELIVER_LOGISTICS_STATUS_ON_WAY, null,
                        erpDeliverOrderRequest.getExpressNo(), orderInfoDO, null,
                        OrderDeliverConstant.ORDER_DELIVER_TYPE_SALE, null, erpDeliverOrderRequest.getExpressType(),
                        erpDeliverOrderRequest.getDeliverOrderNo(), new Date(erpDeliverOrderRequest.getCheckTime()),
                        true, OrderDeliverConstant.ORDER_DELIVER_NEED_PUSH_NO, null);
                if (billResponse.getCode() != 0) {
                    throw OrderException.buildException(String.valueOf(billResponse.getCode()), billResponse.getMsg());
                }
                // 解锁加锁库存(在库存变更接口处理)
                // stockService.deductLockStock(goods, itemsDeliver,order,order.getId(),deliverOrderMap,null);
                // 扣减库存
                // if(!OrderConstant.OrderTypeMto.equals(order.getOrderGoodsType())&&!OrderConstant.OrderTypeDirectTransportation.equals(order.getOrderGoodsType())){
                // stockService.deductStockByDeliver(delivers);
                // }
                if (deliverGoodsDetailMap.size() > 0) {// 说明销售出库单删除了行项目明细，需还原未发货数量
                    LOGGER.info(orderNo + "订单发货明细被删除、需要重置orderGoods发货数量{}", JSON.toJSONString(deliverGoodsDetailMap));
                    for (Integer serialNumber : deliverGoodsDetailMap.keySet()) {
                        OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(serialNumber);
                        List<OrderDeliverBillDetailDO> details = deliverGoodsDetailMap.get(serialNumber);
                        /*  Integer deliverCount = 0;
                        if (CollectionUtils.isNotEmpty(details)) {// 已同步过发货单的时候，统计已同步的数量
                            for (OrderDeliverBillDetailDO detail : details) {
                                deliverCount = deliverCount + detail.getCount();
                            }
                        }
                        if (orderGoodsDO.getDeliverCount() - deliverCount >= 0) {
                            orderGoodsDO.setDeliverCount(orderGoodsDO.getDeliverCount() - deliverCount);
                            orderGoodsDO.setUnDeliverCount(orderGoodsDO.getUnDeliverCount() + deliverCount);
                            deliverCount = 0;
                        } else {
                            deliverCount = deliverCount - orderGoodsDO.getDeliverCount();
                            orderGoodsDO.setDeliverCount(0);
                            orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount());
                        }*/
                        for (int k = 0; k < details.size(); k++) {
                            orderGoodsDO.setDeliverCount(orderGoodsDO.getDeliverCount() - details.get(k).getCount());
                            orderGoodsDO
                                .setUnDeliverCount(orderGoodsDO.getUnDeliverCount() + details.get(k).getCount());
                        }
                    }
                }

                // 可能存在多次发货
                List<OrderDeliverBillDO> orderDeliverBillDOS =
                    orderDeliverBillServiceI.listByOrderId(orderInfoDO.getId());

                // 处理订单发货状态
                OrderDeliveryConvertor.dealwithDeliverStatus(orderInfoDO, erpDeliverOrderRequest.getDocumentStatus(),
                    orderDeliverBillDOS, orderGoodsDOList, erpDeliverOrderRequest.getExpressNo(),
                    erpDeliverOrderRequest.getDeliverOrderNo());
                orderInfoServiceI.update(orderInfoDO);
                LOGGER.info("b2b的erp同步发货单，同步后-订单id{}-订单的发货状态为{}", orderInfoDO.getId(), orderInfoDO.getDeliverStatus());
                LOGGER.info("发货后orderGoodsList{}", JSON.toJSONString(orderGoodsDOList));
                orderGoodsServiceI.batchUpdate(orderGoodsDOList);
                ErpDeliverGoodsItemEntry deliverGoodsItem = new ErpDeliverGoodsItemEntry();
                deliverGoodsItem.setOrderNo(orderNo);
                OrderDeliverBillResp orderDeliverBillResp = (OrderDeliverBillResp)billResponse.getData();
                deliverGoodsItem.setDeliverId(String.valueOf(orderDeliverBillResp.getOrderDeliverBillDO().getId()));
                messageSendService.orderDeliverBillLogPackage(orderDeliverBillResp.getOrderDeliverBillDO().getId(),
                    null, "erp同步发货单", "同步成功", JSONObject.toJSONString(erpDeliverOrderRequest));
                erpDeliverGoodsItemEntryList.add(deliverGoodsItem);
                // 处理分销商短信通知
                // distributorService.sendLogisticsSms(order.getDistributorId(),order.getOrderNo(),request.getExpressNo(),order.getId());
            }
            if (deliverGoodsMap.size() > 0) {// 以前同步销售出库，再次同步销售出库单时已删除的订单销售出库单明细
                for (Integer orderId : deliverGoodsMap.keySet()) {
                    OrderDeliverBillDO deliverGoods = deliverGoodsMap.get(orderId);
                    // OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderId);
                    // 订单归属分销商
                    OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataServiceI
                        .listByCondition(orderId, null, OrderInfoConstant.ORDER_ERP_FLAG_YES).get(0);
                    List<OrderGoodsDO> orderGoodss = orderGoodsServiceI.listByOrderId(orderId);// 获取b2b订单货品明细
                    List<OrderDeliverBillDetailDO> deliverGoodsDetails =
                        orderDeliverBillDetailServiceI.listByOrderDeliverBillId(deliverGoods.getId());
                    for (OrderDeliverBillDetailDO deliverGoodsDetail : deliverGoodsDetails) {
                        for (OrderGoodsDO orderGoods : orderGoodss) {
                            if (deliverGoodsDetail.getItemId().equals(orderGoods.getItemId())) {
                                if (deliverGoodsDetail.getCount() > orderGoods.getDeliverCount()) {// 当发货单数量大于已发数量时
                                    deliverGoodsDetail
                                        .setCount(deliverGoodsDetail.getCount() - orderGoods.getDeliverCount());
                                    orderGoods.setDeliverCount(0);
                                    orderGoods.setUnDeliverCount(orderGoods.getItemCount());
                                } else {
                                    orderGoods
                                        .setDeliverCount(orderGoods.getDeliverCount() - deliverGoodsDetail.getCount());
                                    orderGoods
                                        .setUnDeliverCount(orderGoods.getItemCount() - orderGoods.getDeliverCount());
                                    break;
                                }
                            }
                        }
                    }
                    boolean b = true;
                    // 更新订单状态
                    for (OrderGoodsDO orderGoods : orderGoodss) {
                        if (orderGoods.getDeliverCount() > 0) {
                            b = false;
                            break;
                        }
                    }
                    // 订单待发货
                    if (b) {
                        // 改为已确认
                        orderDistributorDataDO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_CONFIRMED);
                        orderDistributorDataServiceI.update(orderDistributorDataDO);
                        if (orderDistributorDataDO.getOrderStatus() != null && orderDistributorDataDO
                            .getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()) {
                            sendService.orderConfirmMsg(orderDistributorDataDO.getOrderId());
                        }
                    }
                    orderGoodsServiceI.batchUpdate(orderGoodss);

                }
            }
            // 不为空时，说明已同步过，删除原来同步的单据
            if (CollectionUtils.isNotEmpty(deliverGoodsIds)) {
                orderDeliverBillDetailServiceI.deleteByOrderDeliverBillIds(deliverGoodsIds);
                orderDeliverBillServiceI.batchDelete(deliverGoodsIds);
            }
            // 处理order_goods_stock明细（修改发货数量、增加才要(之前发5个、后面改为发3个)、减少不用、删除的发货明细要加回来）
            // 这里面有处理锁库数据
            if (existFlag) {
                warehouseStockQryExe.dealWithOrderGoodsStockBySyncOutboundAgain(orderIds);

            }
        } catch (OrderException e) {
            e.printStackTrace();
            LOGGER.error("ERP同步发货单到B2B{}", e);
            responseBaseBean.setCode(OrderDeliverErrorCode.ORDER_ERP_SYNC_OUTBOUND_ORDER_EXCEPTION_CODE);
            responseBaseBean.setMsg(e.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ERP同步发货单到B2B、EXCEPTION{}", e);
            responseBaseBean.setCode(OrderDeliverErrorCode.ORDER_ERP_SYNC_OUTBOUND_ORDER_EXCEPTION_CODE);
            responseBaseBean.setMsg(MessageUtils.get(OrderCommonErrorCode.SYSTEM_EXCEPTION));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            responseBaseBean.setData(erpDeliverGoodsItemEntryList);
            return responseBaseBean;
        }
    }

    /**
     * 手动触发同步第三方物流接口、组装参数
     * 
     * @param id
     * @return
     */
    @Override
    public com.bat.dubboapi.order.common.Response<OrderLogisticsSyncParam>
        queryLogisticsParamByOrderDeliveryBillId(Integer id) {
        LOGGER.info("手动触发同步第三方物流接口、组装参数 id:{}", id);
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillServiceI.getById(id);
        if (orderDeliverBillDO == null) {
            return com.bat.dubboapi.order.common.Response.buildFailure(B_ORDER_DELIVER_BILL_NOT_EXISTS,
                MessageUtils.get(OrderDeliverErrorCode.B_ORDER_DELIVER_BILL_NOT_EXISTS));
        }
        if (orderDeliverBillDO.getPushStatus() != null
            && OrderDeliverConstant.ORDER_DELIVER_PUSH_STATUS_SUCCESS.equals(orderDeliverBillDO.getPushStatus())) {
            return com.bat.dubboapi.order.common.Response.buildFailure(
                OrderDeliverErrorCode.ORDER_DELIVER_SYNC_REPEAT,
                MessageUtils.get(OrderDeliverErrorCode.ORDER_DELIVER_SYNC_REPEAT));
        }
        OrderLogisticsSyncParam logisticsSyncParam =
            OrderDeliveryConvertor.toOrderLogisticsSyncParam(orderDeliverBillDO);
        LogisticsRpcQry rpcQry = new LogisticsRpcQry();
        rpcQry.setLogisticsId(logisticsSyncParam.getDistributionId());
        com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsResponse =
            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(rpcQry);
        if (logisticsResponse == null) {
            return com.bat.dubboapi.order.common.Response.buildFailure(
                DubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION,
                MessageUtils.get(DubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION));
        }
        if (!logisticsResponse.isSuccess()) {
            return com.bat.dubboapi.order.common.Response.buildFailure(logisticsResponse.getErrCode(),
                logisticsResponse.getErrMessage());
        }
        List<LogisticsRpcDTO> logisticsRpcDTOList = logisticsResponse.getData();
        LogisticsRpcDTO logisticsRpcDTO = logisticsRpcDTOList.get(0);
        // 设置快递公司标准名称
        logisticsSyncParam.setExpressName(logisticsRpcDTO.getLogisticsKdnName());
        OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderDeliverBillDO.getOrderId());
        logisticsSyncParam.setPlatform(orderInfoDO.getOrderSource());
        // 原来的订单id（重构后历史订单没有编码或者没有同步给第三方）
        logisticsSyncParam.setOrderNo(StringUtils.isBlank(orderInfoDO.getOrderNo())
            ? String.valueOf(orderInfoDO.getId()) : orderInfoDO.getOrderNo());
        List<OrderDistributorDataDO> orderDistributorDataDOList = orderDistributorDataServiceI
            .listByCondition(orderDeliverBillDO.getOrderId(), null, OrderInfoConstant.ORDER_ERP_FLAG_YES);
        logisticsSyncParam.setDistributionId(orderDeliverBillDO.getDistributionId());
        OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderId(orderDeliverBillDO.getOrderId());
        // 传的是B2B的单号
        logisticsSyncParam.setOtherOrderNo(orderExtendDataDO.getOrderThirdpartyNo());
        logisticsSyncParam.setDistributorId(orderDistributorDataDOList.get(0).getDistributorId());
        return com.bat.dubboapi.order.common.Response.of(logisticsSyncParam);
    }

    @Override
    public Response<OrderCancelSyncParam> queryCancelOrderParamByOrderId(Integer orderId) {
        OrderCancelSyncParam orderCancelSyncParam = new OrderCancelSyncParam();
        OrderInfoDO orderInfoDO = orderInfoServiceI.getById(orderId);
        orderCancelSyncParam.setPlatform(orderInfoDO.getOrderSource());
        orderCancelSyncParam.setOrderId(orderId);
        // 原来的订单id（重构后历史订单没有编码或者没有同步给第三方）
        orderCancelSyncParam.setOrderNo(StringUtils.isBlank(orderInfoDO.getOrderNo())
            ? String.valueOf(orderInfoDO.getId()) : orderInfoDO.getOrderNo());
        List<OrderDistributorDataDO> orderDistributorDataDOList =
            orderDistributorDataServiceI.listByCondition(orderId, null, OrderInfoConstant.ORDER_ERP_FLAG_YES);
        OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderId(orderId);
        // 传的是B2B的单号
        orderCancelSyncParam.setOtherOrderNo(orderExtendDataDO.getOrderThirdpartyNo());
        orderCancelSyncParam.setDistributorId(orderDistributorDataDOList.get(0).getDistributorId());
        return com.bat.dubboapi.order.common.Response.of(orderCancelSyncParam);
    }

    /**
     * 根据订单发货单流水id 组装同步ERP失败的参数
     * 
     * @param id
     * @return
     */
    @Override
    public com.bat.dubboapi.order.common.Response<OrderDeliverSyncErpParam>
        querySyncErpParamByOrderDeliverId(Integer id) {
        try {
            OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillServiceI.getById(id);
            if (StringUtils.isNotBlank(orderDeliverBillDO.getDeliverErpNo())
                && StringUtils.isNotBlank(orderDeliverBillDO.getDeliverStkNo())) {
                return com.bat.dubboapi.order.common.Response.buildFailure(
                    OrderDeliverErrorCode.ORDER_DELIVER_SYNC_REPEAT,
                    MessageUtils.get(OrderDeliverErrorCode.ORDER_DELIVER_SYNC_REPEAT));
            }
            OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataServiceI
                .listByCondition(orderDeliverBillDO.getOrderId(), null, OrderInfoConstant.ORDER_ERP_FLAG_YES).get(0);
            // 判断订单全都支付的情况、收款单是否同步了
            orderDeliverValidator.validOrderStatusBySyncRxToErp(orderDistributorDataDO.getPayStatus(),
                orderDeliverBillDO.getOrderId());
            // 组装同步ERP参数
            OrderDeliverSyncErpParam erpParam = OrderDeliveryConvertor.toOrderDeliverSyncErpParam(orderDeliverBillDO);
            List<OrderGoodsDiyDO> orderGoodsDiyDOList =
                orderGoodsDiyServiceI.listByOrderId(orderDeliverBillDO.getOrderId());
            erpParam.setManufactors(orderGoodsDiyDOList.get(0).getManufactors());
            List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOList =
                orderDeliverBillDetailServiceI.listByOrderDeliverBillId(id);
            // 组装发货明细列表
            List<OrderGoodsDetailCountDTO> orderGoodsDetailDTOS =
                BeanUtils.copyList(orderDeliverBillDetailDOList, OrderGoodsDetailCountDTO.class);
            erpParam.setOrderGoodsDetailDTOS(orderGoodsDetailDTOS);

            OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderId(orderDeliverBillDO.getOrderId());
            erpParam.setOrderErpNo(orderExtendDataDO.getOrderErpNo());
            if (erpParam.getSyncOutbountFlag()) {
                // 出库单需要物流费用
                OrderDistributorCostDO orderDistributorCostDO =
                    orderDistributorCostServiceI.getByOrderIdAndDistributorId(orderDeliverBillDO.getOrderId(),
                        orderDistributorDataDO.getDistributorId());
                erpParam.setDistributionAmount(orderDistributorCostDO.getDistributionAmount());
            }
            return com.bat.dubboapi.order.common.Response.of(erpParam);
        } catch (OrderException e) {
            e.printStackTrace();
            LOGGER.info("获取同步ERP销售出库单和采购单参数异常{}", e);
            return com.bat.dubboapi.order.common.Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("获取同步ERP销售出库单和采购单参数异常{}", e);
            return com.bat.dubboapi.order.common.Response.buildFailure(
                OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_AND_PURCHASE_PARAM_ERROR,
                MessageUtils.get(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_AND_PURCHASE_PARAM_ERROR));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public com.bat.dubboapi.order.common.Response setErpPurchaseNoAndOutBoundNo(Integer id, String outBoundNo,
        String purchaseNo) {
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillServiceI.getById(id);
        if (StringUtils.isNotBlank(outBoundNo)) {
            orderDeliverBillDO.setDeliverErpNo(outBoundNo);
        }
        if (StringUtils.isNotBlank(purchaseNo)) {
            orderDeliverBillDO.setDeliverStkNo(purchaseNo);
        }
        orderDeliverBillDO.setUpdateTime(new Date());
        orderDeliverBillServiceI.update(orderDeliverBillDO);
        return com.bat.dubboapi.order.common.Response.buildSuccess();
    }

    /**
     * ERP->B2B销售出库单状态变更 单据状态 1.创建，2.已审核，3.取消，4.删除 ，5.提交
     * 
     * @param request
     */
    @Override
    public ResponseBaseBean changeDeliverOrderStatus(ErpDeliverOrderStatusRequest request) {
        LOGGER.info("erp------>销售出库单状态变更接口,请求参数：{}", JSON.toJSONString(request));
        Short status = request.getStatus();
        if (StringUtils.isBlank(request.getDeliverOrderNo())) {
            return ResponseBaseBean.responseBean();
        }
        try {
            List<OrderDeliverBillDO> orderDeliverBillDOList =
                orderDeliverBillServiceI.listByDeliverErpNo(request.getDeliverOrderNo());
            if (orderDeliverBillDOList == null || orderDeliverBillDOList.size() == 0) {
                return ResponseBaseBean.responseBean(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_ERROR_CODE,
                    MessageUtils.get(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_ERROR_MSG));
            }
            List<Integer> ids = new ArrayList<>();
            List<Integer> orderIds = new ArrayList<>();
            // 订单商品锁库由库存变更处理
            for (OrderDeliverBillDO orderDeliverBillDO : orderDeliverBillDOList) {
                if (orderDeliverBillDO.getDeliverStatus().equals(status)
                    && !status.equals(OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED)) {
                    // 如果状态和b2b本身的状态一直，而且状态不是2的2情况下，b2b不必进行变更（是2的情况下得更新）
                    return ResponseBaseBean.responseBean();
                }
                orderDeliverBillDO.setUpdateTime(new Date());
                if (OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED.equals(status)) {
                    orderDeliverBillDO.setDeliverTime(new Date());
                } else if (OrderDeliverConstant.ORDER_DELIVER_STATUS_UN_CONFIRMED.equals(status)) {
                    orderDeliverBillDO.setDeliverTime(null);
                }
                if (orderDeliverBillDO.getOrderId() != null && orderDeliverBillDO.getOrderId() > 0
                    && !orderIds.contains(orderDeliverBillDO.getOrderId())) {
                    // 排除取消或者删除的发货单
                    orderIds.add(orderDeliverBillDO.getOrderId());
                }
                ids.add(orderDeliverBillDO.getId());
                if (OrderDeliverConstant.ORDER_DELIVER_STATUS_CANCEL.equals(status)) {
                    orderDeliverBillDO.setOrderId(0);
                }
                orderDeliverBillDO.setDeliverStatus(status);
                if (StringUtils.isNotBlank(request.getExpressType())) {
                    LogisticsRpcQry logisticsRpcQry = new LogisticsRpcQry();
                    logisticsRpcQry.setLogisticsErpId(request.getExpressType());
                    logisticsRpcQry.setName(request.getExpressType());
                    com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> logisticsResponse =
                        systemLogisticsServiceRpc.listLogisticsFromRpcByParams(logisticsRpcQry);
                    if (logisticsResponse == null) {
                        throw OrderException.buildException(DubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
                    }
                    if (!logisticsResponse.isSuccess()) {
                        throw OrderException.buildException(logisticsResponse.getErrCode(),
                            logisticsResponse.getErrMessage());
                    }
                    List<LogisticsRpcDTO> logisticsRpcDTOList = logisticsResponse.getData();
                    if (logisticsRpcDTOList != null && logisticsRpcDTOList.size() > 0) {
                        boolean b = false;
                        for (LogisticsRpcDTO logisticsRpcDTO : logisticsRpcDTOList) {
                            if (logisticsRpcDTO.getId().equals(orderDeliverBillDO.getDistributionId())) {
                                orderDeliverBillDO.setDistributionId(logisticsRpcDTO.getId());
                                orderDeliverBillDO.setDistributionName(logisticsRpcDTO.getName());
                                b = true;
                                break;
                            }
                        }
                        if (b) {
                            orderDeliverBillDO.setDistributionName(logisticsRpcDTOList.get(0).getName());
                            orderDeliverBillDO.setDistributionId(logisticsRpcDTOList.get(0).getId());
                        }
                    }
                }
                if (StringUtils.isNotBlank(request.getExpressNo())) {
                    orderDeliverBillDO.setLogisticsNo(request.getExpressNo());
                }
            }
            List<OrderInfoDO> orders = orderInfoServiceI.listByIds(orderIds);
            Map<Integer, OrderInfoDO> orderMap = new HashMap<>();
            // 订单商品明细id为key
            Map<Integer, OrderGoodsDO> orderGoodsMap = new HashMap<>();

            Map<Integer, List<OrderGoodsDO>> orderGoodsListMap = new HashMap<>();
            // 订单id为key\value为每个订单的发货流水
            Map<Integer, List<OrderDeliverBillDO>> orderDeliverBillMap = new HashMap<>();
            for (OrderInfoDO order : orders) {
                // if(order.getOrderTypeValue().equals(diyType)){// 定制订单，无需变更销售出库单
                // return ResponseBaseBean.responseBean();
                // }
                // 状态 1、创建 2、已审核、3、取消 4、删除 5、提交
                if (status == 3 || status == 4) {
                    orderMap.put(order.getId(), order);
                    order.setDeliverStatus(ORDER_DELIVER_STATUS_UN_SHIPPED);
                    OrderDistributorDataDO orderDistributorDataDO = orderDistributorDataServiceI
                        .listByCondition(order.getId(), null, OrderInfoConstant.ORDER_ERP_FLAG_YES).get(0);
                    // 修改已确认
                    orderDistributorDataDO.setOrderStatus(OrderInfoConstant.ORDER_STATUS_CONFIRMED);
                    orderDistributorDataDO.setUpdateTime(new Date());
                    LOGGER.info("订单{}订单状态由{}修改为已确认", order.getId(), orderDistributorDataDO.getOrderStatus());
                    orderDistributorDataServiceI.update(orderDistributorDataDO);
                    if (orderDistributorDataDO.getOrderStatus() != null
                        && orderDistributorDataDO.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()) {
                        sendService.orderConfirmMsg(orderDistributorDataDO.getOrderId());
                    }
                }
                List<OrderDeliverBillDO> billDOList = orderDeliverBillServiceI.listByOrderId(order.getId());
                orderDeliverBillMap.put(order.getId(), billDOList);
                List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(order.getId());
                // 订单发货状态矫正
                OrderDeliveryConvertor.dealwithDeliverStatus(order, status, billDOList, orderGoodsDOList,
                    request.getExpressNo(), request.getDeliverOrderNo());
                LOGGER.info("处理后的订单{}", JSON.toJSONString(order));
                /*   if (StringUtils.isNotBlank(request.getExpressNo())) {
                     含有物流单号
                     distributorService.sendLogisticsSms(order.getDistributorId(), order.getOrderNo(),
                     request.getExpressNo() ,order.getId());
                }*/
                // 订单纬度
                orderGoodsListMap.put(order.getId(), orderGoodsDOList);
                orderGoodsDOList.stream().forEach(orderGoodsDO -> {
                    orderGoodsMap.put(orderGoodsDO.getId(), orderGoodsDO);
                });
                orderInfoServiceI.update(order);
            }
            if (status == 3 || status == 4) {// 取消或删除
                List<OrderDeliverBillDetailDO> deliverGoodsDetails =
                    orderDeliverBillDetailServiceI.listByOrderDeliverBillIdList(ids);
                // 修改的订单明细
                List<OrderGoodsDO> changeOrderGoods = new ArrayList<>();
                // 需要重新插入订单明细(锁库记录由库存变更处理)
                warehouseStockQryExe.createOrderGoodsStockAgainByOutboundOrderStatus(orderGoodsMap, deliverGoodsDetails,
                    changeOrderGoods);
                LOGGER.info("处理后的订单列表{}", JSON.toJSONString(changeOrderGoods));
                orderGoodsServiceI.batchUpdate(changeOrderGoods);
                for (OrderInfoDO orderInfoDO : orders) {
                    messageSendService.oredrLogPackage(orderInfoDO.getId(), "ERP->B2B销售出库单状态变更-订单修改", "更新成功",
                        JSONObject.toJSONString(orderInfoDO), "erp");
                }
            }
            if (status == 4) {// 删除
                orderDeliverBillDetailServiceI.deleteByOrderDeliverBillIds(ids);
                orderDeliverBillServiceI.batchDelete(ids);
            } else {
                orderDeliverBillServiceI.batchUpdate(orderDeliverBillDOList);
                for (OrderDeliverBillDO orderDeliverBillDO : orderDeliverBillDOList) {
                    if (orderDeliverBillDO.getDeliverStatus() != null && orderDeliverBillDO
                        .getDeliverStatus() == OrderDeliverConstant.ORDER_DELIVER_STATUS_CONFIRMED.shortValue()) {
                        messageSendService.orderDeliverMsg(orderDeliverBillDO.getId());
                    }
                    messageSendService.orderDeliverBillLogPackage(orderDeliverBillDO.getId(),
                        orderDeliverBillDO.getOrderId(), "ERP->B2B销售出库单状态变更-发货单更新", "更新成功",
                        JSONObject.toJSONString(orderDeliverBillDO));
                }
            }
            return ResponseBaseBean.responseBean();
        } catch (OrderException e) {
            e.printStackTrace();
            LOGGER.error(MessageUtils.get(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_MSG)
                + "erp------>销售出库单状态变更异常{}", e.getMsg());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseBaseBean.responseBean(
                OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_CODE,
                MessageUtils.get(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_MSG));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("erp------>销售出库单状态变更系统异常{}", e.getMessage());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseBaseBean.responseBean(
                OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_CODE,
                MessageUtils.get(OrderDeliverErrorCode.ORDER_ERP_OUTBOUND_ORDER_STATUS_UPDATE_ERROR_MSG));
        }

    }

    @Override
    public com.bat.dubboapi.order.common.Response updatePushStatus(Integer id, Short pushStatus) {
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillServiceI.getById(id);
        orderDeliverBillDO.setPushStatus(pushStatus);
        orderDeliverBillServiceI.update(orderDeliverBillDO);
        return com.bat.dubboapi.order.common.Response.buildSuccess();
    }

    public static void main(String[] args) {
        OrderGoodsStockDO orderGoodsStockDO = new OrderGoodsStockDO();
        orderGoodsStockDO.setItemId(1);
        OrderGoodsStockDO copy = BeanUtils.copy(orderGoodsStockDO, OrderGoodsStockDO.class);
        copy.setItemId(2);
        System.out.println(JSON.toJSONString(orderGoodsStockDO));
        System.out.println(JSON.toJSONString(copy));
    }

    @Override
    public Response<List<Integer>> getNotErpOrderDeliverBillIds(Date startTime) {
        List<Integer> ids = orderDeliverBillServiceI.getNotErpOrderDeliverBillIds(startTime);
        return Response.of(ids);
    }
}
