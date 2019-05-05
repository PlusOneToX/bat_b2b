package com.bat.thirdparty.erp.convertor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.common.enumtype.OrderStatus;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.flexible.ThirdFlexibleConstant;
import com.bat.thirdparty.common.order.OrderInfoConstant;
import com.bat.thirdparty.common.order.OrderPayConstant;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.ErpConfigExe;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import com.bat.thirdparty.order.service.executor.OrderQryExe;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.distributor.trade.dto.DistributorTradeRpcQryDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialCurrencyServiceRpc;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.flexible.exchange.ExchangeCardServiceRpc;
import com.bat.dubboapi.flexible.exchange.ExchangeCodeServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCardDTORpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeDTORpcQry;
import com.bat.dubboapi.flexible.model.api.ModelMaterialRelevanceServiceRpc;
import com.bat.dubboapi.flexible.model.api.ModelServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ModelDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.api.PictureServiceRpc;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.order.cost.dto.OrderDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.cost.dto.OrderGoodsDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryRpcQryDTO;
import com.bat.dubboapi.order.exchange.dto.OrderGoodsExchangeCodeRpcQryDTO;
import com.bat.dubboapi.order.order.api.OrderCustomerDataServiceRpc;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.dubboapi.order.order.dto.OrderCheckCmd;
import com.bat.dubboapi.order.order.dto.data.OrderCustomerDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDiyRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.dubboapi.order.type.dto.OrderTypeRpcQryDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;
import com.bat.thirdparty.distributor.executor.DistributorServiceQryExe;
import com.bat.thirdparty.erp.api.request.ErpOrderChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderCheckRequest;
import com.bat.thirdparty.erp.api.request.dto.order.CreateSaleOrderQry;
import com.bat.thirdparty.erp.api.request.dto.order.ExchangePlainCodeOrderErp;
import com.bat.thirdparty.erp.api.request.dto.order.SaleOrderDetailQry;
import com.bat.thirdparty.erp.service.executor.OrderRpcExe;

/**
 * 订单ERP
 */
@Component
public class ErpOrderConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpOrderConvertor.class);

    @DubboReference(check = false, timeout = 10000)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemUserServiceRpc systemUserServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private ModelMaterialRelevanceServiceRpc modelMaterialRelevanceServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private ModelServiceRpc modelServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private PictureServiceRpc pictureServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ExchangeCodeServiceRpc exchangeCodeServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private FinancialCurrencyServiceRpc financialCurrencyServiceRpc;

    @Autowired
    private OrderQryExe orderQryExe;

    @Autowired
    private DistributorServiceQryExe distributorServiceQryExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderCustomerDataServiceRpc orderCustomerDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ExchangeCardServiceRpc exchangeCardServiceRpc;

    @Resource
    private ConfigQry configQry;

    @Resource
    OrderRpcExe orderRpcExe;
    // @Resource
    // private ErpConfig erpConfig;
    @Resource
    private ErpConfigExe erpConfigExe;

    /**
     * 组装销售订单参数（ERP）获取信息
     * 
     * @return
     */
    public CreateSaleOrderQry toCreateSaleOrderQry(Integer orderId) {

        // 获取订单信息
        OrderInfoRpcQryDTO orderInfoRpcQryDTO = orderQryExe.getOrderInfoById(orderId);
        // 获取订单类型
        OrderTypeRpcQryDTO orderType = orderQryExe.getOrderTypeById(orderInfoRpcQryDTO.getOrderTypeId());
        // 获取订单扩展数据
        OrderExtendDataSimpleRpcDTO orderExtendDataRpcData = orderQryExe.getExtendDataByOrderId(orderId);
        if (StringUtils.isNotBlank(orderExtendDataRpcData.getOrderErpNo())) {
            // 已生成ERP订单号
            throw ThirdPartyException.buildException(ThirdOrderErrorCode.T_ORDER_SYNC_ERP_ALREADY);
        }
        List<OrderDistributorDataRpcDTO> orderDistributorDataRpcDTOList =
            orderQryExe.getByOrderIdAndErpFlag(orderId, OrderInfoConstant.ORDER_DISTRIBUTOR_SYNC_ERP_FLAG_YES);
        if (orderDistributorDataRpcDTOList == null || orderDistributorDataRpcDTOList.size() == 0) {
            // 订单不需要同步ERP
            throw ThirdPartyException.buildException(ThirdOrderErrorCode.T_ORDER_NOT_NEED_SYNC_ERP);
        }
        OrderDistributorDataRpcDTO orderDistributorDataRpcDTO = orderDistributorDataRpcDTOList.get(0);
        CreateSaleOrderQry createSaleOrderQry = toErpOrderBean(orderInfoRpcQryDTO, orderType,
            orderInfoRpcQryDTO.getStockType(), orderInfoRpcQryDTO.getCreateTime(), orderDistributorDataRpcDTO,
            orderExtendDataRpcData.getAutoDelivery());
        // 设置是否需要同步收款单到ERP
        setSyncVoucherFlag(createSaleOrderQry, orderDistributorDataRpcDTO);
        return createSaleOrderQry;
    }

    // 设置是否需要同步收款单到ERP（线上需要）
    private void setSyncVoucherFlag(CreateSaleOrderQry createSaleOrderQry,
        OrderDistributorDataRpcDTO orderDistributorDataRpcDTO) {
        if (OrderPayConstant.ORDER_PAY_WAY_INTERVAL.equals(orderDistributorDataRpcDTO.getPayWay())) {
            // 区间结算不需要
            return;
        }
        if (OrderPayConstant.ORDER_PAY_WAY_OFFLINE_TRANSFER.equals(orderDistributorDataRpcDTO.getPayWay())) {
            // 线下转账不需要
            return;
        }
        if (OrderPayConstant.ORDER_PAY_WAY_ACCOUNT_BALANCE.equals(orderDistributorDataRpcDTO.getPayWay())) {
            // 余额不需要
            return;
        }
        createSaleOrderQry.setSyncVoucherFlag(true);
    }

    /**
     * 组装 同步ERP的对象
     * @param orderInfoRpcQryDTO
     * @param orderType
     * @param stockType
     * @param createTime
     * @param orderDistributorDataRpcDTO
     * @param autoDelivery
     * @return
     */
    private CreateSaleOrderQry toErpOrderBean(OrderInfoRpcQryDTO orderInfoRpcQryDTO, OrderTypeRpcQryDTO orderType,
        Short stockType, Date createTime, OrderDistributorDataRpcDTO orderDistributorDataRpcDTO, Short autoDelivery) {
        CreateSaleOrderQry bean = new CreateSaleOrderQry();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = ft.format(createTime);
        bean.setFDATE(time);

        com.bat.dubboapi.order.common.Response<OrderDeliveryRpcQryDTO> deliveryResp =
            orderDeliveryServiceRpc.getByOrderId(orderInfoRpcQryDTO.getId());
        if (deliveryResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!deliveryResp.isSuccess()) {
            throw ThirdPartyException.buildException(deliveryResp.getErrCode(), deliveryResp.getErrMessage());
        }
        OrderDeliveryRpcQryDTO orderDeliveryRpcQryDTO = deliveryResp.getData();
        com.bat.dubboapi.system.common.Response<UserRpcDTO> userRpcDTOResponse =
            systemUserServiceRpc.getUserById(orderInfoRpcQryDTO.getSalesId());
        UserRpcDTO sysUserRpcDTO = userRpcDTOResponse.getData();
        if (orderDistributorDataRpcDTO != null) {
            bean.setFNote(orderDistributorDataRpcDTO.getRemark());
        }
        // 订单费用
        OrderDistributorCostRpcQryDTO orderDistributorCostRpcQryDTO = orderQryExe
            .getByOrderIdAndDistributorId(orderInfoRpcQryDTO.getId(), orderDistributorDataRpcDTO.getDistributorId());
        LOGGER.info("开始查看订单信息");
        if(orderDistributorCostRpcQryDTO.getRebateVoucherAmount()!=null&&orderDistributorCostRpcQryDTO.getRebateVoucherAmount().compareTo(BigDecimal.ZERO)>0){
            //有返利金额
            bean.setF_PAEZ_FLAMOUNT(String.valueOf(orderDistributorCostRpcQryDTO.getRebateVoucherAmount()));
        }

        List<OrderGoodsRpcDTO> orderGoodsRpcDTOList = orderQryExe.listByOrderId(orderInfoRpcQryDTO.getId());
        LOGGER.info("同步ERP的orderGoodsList：{}", JSON.toJSONString(orderGoodsRpcDTOList));

        // 支付方式
        Short payWay = orderDistributorDataRpcDTO.getPayWay();

        List<OrderGoodsDistributorCostRpcQryDTO> orderGoodsDistributorCostRpcQryDTOList =
            orderQryExe.listOrderGoodDistributorCostByOrderIdAndDistributorId(orderInfoRpcQryDTO.getId(),
                orderDistributorDataRpcDTO.getDistributorId());
        Map<Integer, OrderGoodsDistributorCostRpcQryDTO> orderGoodsDistributorCostRpcQryDTOMap =
            orderGoodsDistributorCostRpcQryDTOList.stream()
                .collect(Collectors.toMap(OrderGoodsDistributorCostRpcQryDTO::getOrderGoodsId,
                    orderGoodsDistributorCostRpcQryDTO -> orderGoodsDistributorCostRpcQryDTO));
        // 需要处理特殊的运费
        BigDecimal logisticCost = getByDistributorDirectFlagAndPlatformAmount(orderDistributorCostRpcQryDTO,
            orderDistributorDataRpcDTO, orderGoodsDistributorCostRpcQryDTOList, orderGoodsRpcDTOList);
        // 汇率转换（B2B数据库记录的是人民币）
        logisticCost = logisticCost.divide(orderDistributorDataRpcDTO.getCurrentRates(), 2, BigDecimal.ROUND_HALF_UP);
        // 物流费用
        bean.setFLogisticsCost(String.valueOf(logisticCost));
        if (orderDeliveryRpcQryDTO != null && StringUtils.isNotBlank(orderDistributorDataRpcDTO.getRemark())) {
            bean.setFNote(orderDeliveryRpcQryDTO.getDistributionName() + ","
                    +orderDeliveryRpcQryDTO.getLogisticsErpId()+"," + orderDistributorDataRpcDTO.getRemark());
        }
        if (orderDeliveryRpcQryDTO != null && StringUtils.isBlank(orderDistributorDataRpcDTO.getRemark())) {
            bean.setFNote(orderDeliveryRpcQryDTO.getDistributionName()+ ","
                    +orderDeliveryRpcQryDTO.getLogisticsErpId());
        }
        OrganizationRpcDTO organizationDTO = sysUserRpcDTO.getOrganizationDTO();
        bean.setFSALEORGID(organizationDTO == null ? "" : organizationDTO.getErpOrganizationId());
        bean.setFSALERID(sysUserRpcDTO.getErpUserNo());
        DistributorRpcDTO distributorRpcDTO =
            distributorServiceQryExe.getDistributorById(orderDistributorDataRpcDTO.getDistributorId());
        bean.setFCUSTID(distributorRpcDTO.getErpNo());
        // 处理详情
        Map<Integer, OrderGoodsDiyRpcDTO> diyRpcDTOMap = toOrderGoodsDiyRpcMap(orderType, orderInfoRpcQryDTO.getId());
        // 处理仓库 订单商品明细 id、关联仓库编码
        Map<Integer, String> wareHouseMap = orderGoodsSetWarehouseNo(orderType, orderGoodsRpcDTOList, diyRpcDTOMap);
        Map<Integer, List<ExchangeCodeDTORpcQry>> codeDTORpcQryMap =
            getExchangeCodeMapByOrderId(orderInfoRpcQryDTO.getId());
        // 是否邮费兑换卡
        Boolean postageExchangeFlag = false;
        if (codeDTORpcQryMap != null && codeDTORpcQryMap.size() > 0) {

            List<ExchangeCodeDTORpcQry> exchangeCodeDTORpcQryList =
                codeDTORpcQryMap.get(orderGoodsRpcDTOList.get(0).getId());
            if (exchangeCodeDTORpcQryList != null && exchangeCodeDTORpcQryList.size() > 0) {
                ExchangeCodeDTORpcQry exchangeCodeDTORpcQry = exchangeCodeDTORpcQryList.get(0);
                com.bat.dubboapi.flexible.common.Response<ExchangeCardDTORpc> exchangeCardDTORpcResponse =
                    exchangeCardServiceRpc.getById(exchangeCodeDTORpcQry.getExchangeId());
                if (!exchangeCardDTORpcResponse.isSuccess()) {
                    throw ThirdPartyException.buildException(exchangeCardDTORpcResponse.getErrCode(),
                        exchangeCardDTORpcResponse.getErrMessage());
                }
                ExchangeCardDTORpc exchangeCardDTORpc = exchangeCardDTORpcResponse.getData();
                if (ThirdFlexibleConstant.EXCHANGE_CARD_MAIL_SETTING_GIVING_FREIGHT
                    .equals(exchangeCardDTORpc.getMailSetting())) {
                    // 收运费
                    postageExchangeFlag = true;
                }
            }
        }
        // 设置同步ERP订单明细
        List<SaleOrderDetailQry> saleOrderDetailQries =
            toErpOrderDetail(orderDistributorDataRpcDTO, orderGoodsRpcDTOList, bean.getFSALEORGID(), time, wareHouseMap,
                diyRpcDTOMap, orderGoodsDistributorCostRpcQryDTOMap, codeDTORpcQryMap,
                orderDistributorDataRpcDTO.getRemark(), postageExchangeFlag);
        // 设置明细
        bean.setSALEORDERDETAIL(saleOrderDetailQries);

        com.bat.dubboapi.financial.common.Response<CurrencyRpcDO> currencyResp =
            financialCurrencyServiceRpc.getCurrencyByCurrencyCode(orderDistributorDataRpcDTO.getCurrencyType());
        if (currencyResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
        }
        if (!currencyResp.isSuccess()) {
            throw ThirdPartyException.buildException(currencyResp.getErrCode(), currencyResp.getErrMessage());
        }
        CurrencyRpcDO currencyRpcDO = currencyResp.getData();

        bean.setFSETTLECURRID(currencyRpcDO.getErpNo());

        /*重构没有事业部了
        if (admin.getBusinessUnitId() != null && admin.getBusinessUnitId() != 0) {
            BusinessUnit businessUnit = businessUnitDBManager.findById(Long.valueOf(admin.getBusinessUnitId()));
            if (businessUnit != null) {
                bean.setF_LHR_DIVISION(businessUnit.getErpBusinessUnitId());
            }
        }*/
        // ERP订单类型
        bean.setFBILLTYPEID(orderType.getErpType());
        // 是否MTO
        bean.setFSOMTO("False");
        bean.setFDirectShipment("False");
        // 直运或MTO订单类型需做处理
        if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_2)) {
            // 转换为标准的type传过去erp
            // MTO 订单传递给ERP 传标准销售订单的TYPE
            bean.setFSOMTO("True");
            OrderTypeRpcQryDTO specialFlag = orderQryExe.getOrderTypeBySpecialFlag(ThirdCommonConstant.SPECIAL_FLAG_1);
            bean.setFBILLTYPEID(specialFlag.getErpType());
        } else if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
            bean.setFDirectShipment("True");
            bean.setCustomDiyFlag(true);
        }
        PlatformTenantErpRpcDTO erpConfig = erpConfigExe.getPlatformTenantErpConfig();
        bean.setFRecConditionId(erpConfig.getSettleDefault());
        // 未设置收款条件客户，默认现款支付
        if (!OrderPayConstant.ORDER_PAY_WAY_INTERVAL.equals(payWay)
            && !OrderPayConstant.ORDER_PAY_WAY_OFFLINE_TRANSFER.equals(payWay)
            && !OrderPayConstant.ORDER_PAY_WAY_ACCOUNT_BALANCE.equals(payWay)) {
            // 没设置区间结算、非线下转账、非余额、设置为线上支付 (柔性不变)
            if (!orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_3)
                && !orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
                // 不属于现销订单、传到ERP要改
                OrderTypeRpcQryDTO cashType = orderQryExe.getOrderTypeBySpecialFlag(ThirdCommonConstant.SPECIAL_FLAG_3);
                bean.setFBILLTYPEID(cashType.getErpType());
            }
            bean.setFRecConditionId(erpConfig.getSettleCashOnline());
        } else if (OrderPayConstant.ORDER_PAY_WAY_OFFLINE_TRANSFER.equals(payWay)) {
            // 线下
            bean.setFRecConditionId(erpConfig.getSettleCashOffline());
        } else if (OrderPayConstant.ORDER_PAY_WAY_ACCOUNT_BALANCE.equals(payWay)) {
            bean.setFRecConditionId(erpConfig.getSettleMonth());
            if (!orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_3)
                && !orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
                // 不属于现销订单、传到ERP要改 (排除柔性)
                OrderTypeRpcQryDTO cashType = orderQryExe.getOrderTypeBySpecialFlag(ThirdCommonConstant.SPECIAL_FLAG_3);
                bean.setFBILLTYPEID(cashType.getErpType());
            }
        } else {
            DistributorTradeRpcQryDTO distributorTradeRpcQryDTO =
                distributorServiceQryExe.getTradeByDistributorId(orderDistributorDataRpcDTO.getDistributorId());
            bean.setFRecConditionId(distributorTradeRpcQryDTO.getErpSettleAccountNo());
        }

        // 设置ERP物流信息
        setErpOrderDeliveryMsg(bean, orderDeliveryRpcQryDTO);
        // 改同步B2B订单号到ERP、非ID
        bean.setFB2B_BILLNO(orderInfoRpcQryDTO.getOrderNo());
        // 是否自动审核 0自动审核 1非自动审核
        if (orderDistributorDataRpcDTO.getOrderStatus().equals(OrderStatus.CONFIRMED.getValue())) {
            bean.setWhetherAudit("0");
        } else {
            bean.setWhetherAudit("1");
        }
        bean.setFDerivedFrom("B2B");// 订单来源
        /*直运订单 重构
        if (order.getOrderGoodsType() == OrderConstant.OrderTypeDirectTransportation) {
             bean.setDirectTransportationFlag(true);
        }*/
        // 是否直发
        Boolean orderZFFlag = checkOrderIsZF(orderInfoRpcQryDTO.getStockType(), orderType, autoDelivery);
        bean.setF_SOZF(orderZFFlag ? "True" : "False");

        /* if (OrderInfoConstant.ORDER_STOCK_TYPE_INSTOCK.equals(stockType)
            && !ErpOrderTypeConfig.customizedOrder.equals(orderTypeId)
            && DistributorConstant.DISTRIBUTOR_AUTO_DELIVERY_YES.equals(distributorRpcDTO.getAutoDelivery())) {
            // 直发
            bean.setF_SOZF("True");
        }*/

        /* if(OrderInfoConstant.ORDER_STOCK_TYPE_INSTOCK.equals(stockType) && )*/

        // bean.setNeedRecAdvance(order.getPayStatus()==3?"True":order.getPayWay()==OrderConstant.CompanyTransferType?"True":"False");
        bean.setFNeedRecAdvance("False");// 现款线下、现款线上订单 预收
        if (!OrderPayConstant.ORDER_PAY_WAY_INTERVAL.equals(payWay)
            && !OrderPayConstant.ORDER_PAY_WAY_OFFLINE_TRANSFER.equals(payWay)) {
            // bean.setControlSend(controlType);//现款线上订单 发货
            bean.setFNeedRecAdvance("True");
        }

        bean.setF_SOPT("False");
        // 是否拼团
        Boolean isGroupOrder = checkOrderIsGroup(orderGoodsDistributorCostRpcQryDTOList);
        if (isGroupOrder) {
            bean.setF_SOPT("True");
        }
        // 是否合并发货
        bean.setF_SOHDFH("False");
        if (stockType.equals(OrderInfoConstant.ORDER_STOCK_TYPE_MERGE)) {
            // 订单拆分
            bean.setF_SOHDFH("True");
        }
        // 是否兑换卡、
        bean.setF_SODH("False");
        if (codeDTORpcQryMap != null && codeDTORpcQryMap.size() > 0) {
            bean.setF_SODH("True");
            // 邮费付款单
            if (postageExchangeFlag) {
                bean.setF_SODH("False");
            }
        }
        // 是否是门店
        OrderCustomerDataRpcDTO orderCustomerDataRpcDTO =
            orderQryExe.getOrderCustomerDataByOrderId(orderInfoRpcQryDTO.getId());
        if (orderCustomerDataRpcDTO != null) {
            bean.setF_B2BStoresNo(orderCustomerDataRpcDTO.getShopCode());
            bean.setF_B2BStoresName(orderCustomerDataRpcDTO.getShopName());
        }
        return bean;
    }

    /**
     * 需要处理特殊的运费
     * @param orderDistributorCostRpcQryDTO
     * @param orderDistributorDataRpcDTO
     * @param orderGoodsDistributorCostRpcQryDTOList
     * @param orderGoodsRpcDTOList
     * @return
     */
    private BigDecimal getByDistributorDirectFlagAndPlatformAmount(
        OrderDistributorCostRpcQryDTO orderDistributorCostRpcQryDTO,
        OrderDistributorDataRpcDTO orderDistributorDataRpcDTO,
        List<OrderGoodsDistributorCostRpcQryDTO> orderGoodsDistributorCostRpcQryDTOList,
        List<OrderGoodsRpcDTO> orderGoodsRpcDTOList) {
        // 分销层数据直接下单为否（direct_flag），且平台应收款金额不为空（platform_amount)时,同步平台收款金额，运费=订单平台方收款总金额-订单明细平台收款总金额
        if (OrderInfoConstant.ORDER_DISTRIBUTOR_DATA_DIRECT_FLAG_YES
            .equals(orderDistributorDataRpcDTO.getDirectFlag())) {
            return orderDistributorCostRpcQryDTO.getDistributionAmount();
        }
        if (orderDistributorCostRpcQryDTO.getPlatformAmount() == null) {
            // 平台金额不为空
            return orderDistributorCostRpcQryDTO.getDistributionAmount();
        }
        Map<Integer, OrderGoodsRpcDTO> orderGoodsRpcDTOMap = orderGoodsRpcDTOList.stream()
            .collect(Collectors.toMap(OrderGoodsRpcDTO::getId, orderGoodsRpcDTO -> orderGoodsRpcDTO));
        BigDecimal detailAllPrice = new BigDecimal("0.00");
        for (int x = 0; x < orderGoodsDistributorCostRpcQryDTOList.size(); x++) {
            OrderGoodsRpcDTO orderGoodsRpcDTO =
                orderGoodsRpcDTOMap.get(orderGoodsDistributorCostRpcQryDTOList.get(x).getOrderGoodsId());
            BigDecimal priceAmount = orderGoodsDistributorCostRpcQryDTOList.get(x).getPlatformPrice()
                .multiply(new BigDecimal(orderGoodsRpcDTO.getItemCount())).setScale(2, BigDecimal.ROUND_HALF_UP);
            detailAllPrice = detailAllPrice.add(priceAmount);
        }
        // 返回 运费=订单平台方收款总金额-订单明细平台收款总金额
        return orderDistributorCostRpcQryDTO.getPlatformAmount().subtract(detailAllPrice).setScale(2,
            BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 判断订单是否ERP直发
     * 
     * @param stockType
     *            库存类型
     * @param orderType
     *            订单类型
     * @param autoDelivery
     *            是否直发
     * @return
     */
    public static Boolean checkOrderIsZF(Short stockType, OrderTypeRpcQryDTO orderType, Short autoDelivery) {
        if (stockType == null) {
            return false;
        }
        // 含有在途都不是直发
        if (OrderInfoConstant.ORDER_STOCK_TYPE_ONWAY.equals(stockType)) {
            return false;
        }
        // 含有在途都不是直发
        if (OrderInfoConstant.ORDER_STOCK_TYPE_MERGE.equals(stockType)) {
            return false;
        }
        // 定制都不是
        if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
            return false;
        }
        // 非B2B直发订单
        if (OrderInfoConstant.ORDER_INFO_AUTO_DELIVERY_NOT.equals(autoDelivery)) {
            return false;
        }
        // 直运订单不是直发
        if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_5)) {
            return false;
        }
        return true;
    }

    /**
     * 订单明细设置仓库编码
     *
     * @param orderType
     *            订单类型id
     * @param orderGoodsRpcDTOList
     */
    private Map<Integer, String> orderGoodsSetWarehouseNo(OrderTypeRpcQryDTO orderType,
        List<OrderGoodsRpcDTO> orderGoodsRpcDTOList, Map<Integer, OrderGoodsDiyRpcDTO> diyRpcDTOMap) {
        Map<Integer, String> wareHouseMap = new HashMap<>();
        if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
            // 定制订单
            for (int i = orderGoodsRpcDTOList.size() - 1; i >= 0; i--) {
                OrderGoodsRpcDTO orderGoodsRpcDTO = orderGoodsRpcDTOList.get(i);
                OrderGoodsDiyRpcDTO diyRpcDTO = diyRpcDTOMap.get(orderGoodsRpcDTO.getId());
                PlatformTenantDiyFactoryRpcDTO tenantFactoryConfig = configQry.getTenantFactoryConfig(diyRpcDTO.getManufactors());
                wareHouseMap.put(orderGoodsRpcDTO.getItemId(), tenantFactoryConfig.getWarehouseNo());
            }
            return wareHouseMap;
        }
        if (orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_2)) {
            // MTO订单
            WarehouseDTORpcQry warehouseDTORpcQry = orderRpcExe.getWarehouseDTORpcQry(orderType.getWarehouseId(), null);
            for (int x = 0; x < orderGoodsRpcDTOList.size(); x++) {
                wareHouseMap.put(orderGoodsRpcDTOList.get(x).getItemId(), warehouseDTORpcQry.getWarehouseNo());
            }
            return wareHouseMap;
        }
        Map<Integer, WarehouseDTORpcQry> warehouseDTORpcQryMap = new HashMap<>();

        // 非定制订单
        for (int x = 0; x < orderGoodsRpcDTOList.size(); x++) {
            OrderGoodsRpcDTO orderGoodsRpcDTO = orderGoodsRpcDTOList.get(x);
            WarehouseDTORpcQry warehouseDTORpcQry = warehouseDTORpcQryMap.get(orderGoodsRpcDTO.getWarehouseId());
            if (warehouseDTORpcQry == null) {
                warehouseDTORpcQry = orderRpcExe.getWarehouseDTORpcQry(orderGoodsRpcDTO.getWarehouseId(), null);
                warehouseDTORpcQryMap.put(warehouseDTORpcQry.getId(), warehouseDTORpcQry);
            }
            wareHouseMap.put(orderGoodsRpcDTO.getItemId(), warehouseDTORpcQry.getWarehouseNo());
        }
        return wareHouseMap;
    }

    /**
     * 根据订单id查询核销兑换码列表
     *
     * @param orderId
     * @return
     */
    private Map<Integer, List<ExchangeCodeDTORpcQry>> getExchangeCodeMapByOrderId(Integer orderId) {
        com.bat.dubboapi.flexible.common.Response<List<ExchangeCodeDTORpcQry>> exchangeCodeResp =
            exchangeCodeServiceRpc.listByUserOrderId(orderId, true);
        if (exchangeCodeResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
        }
        if (!exchangeCodeResp.isSuccess()) {
            throw ThirdPartyException.buildException(exchangeCodeResp.getErrCode(), exchangeCodeResp.getErrMessage());
        }
        Map<Integer, List<ExchangeCodeDTORpcQry>> codeDTORpcQryMap = new HashMap<>();
        List<ExchangeCodeDTORpcQry> exchangeCodeDTORpcQryList = exchangeCodeResp.getData();
        if (exchangeCodeDTORpcQryList != null && exchangeCodeDTORpcQryList.size() > 0) {
            codeDTORpcQryMap = exchangeCodeDTORpcQryList.stream()
                .collect(Collectors.toMap(ExchangeCodeDTORpcQry::getUserOrderGoodsId,
                    exchangeCodeDTORpcQry -> Lists.newArrayList(exchangeCodeDTORpcQry),
                    (List<ExchangeCodeDTORpcQry> oldList, List<ExchangeCodeDTORpcQry> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        }
        return codeDTORpcQryMap;
    }

    /**
     * 设置ERP物流信息
     *
     * @param bean
     * @param orderDeliveryRpcQryDTO
     */
    private void setErpOrderDeliveryMsg(CreateSaleOrderQry bean, OrderDeliveryRpcQryDTO orderDeliveryRpcQryDTO) {
        if (orderDeliveryRpcQryDTO != null) {
            bean.setFContact(orderDeliveryRpcQryDTO.getUserName());
            bean.setFContactPhone(orderDeliveryRpcQryDTO.getMobile());
            String fcontactAddress = "";
            if (StringUtils.isNotBlank(orderDeliveryRpcQryDTO.getProvinceName())) {
                fcontactAddress = fcontactAddress + orderDeliveryRpcQryDTO.getProvinceName();
            }
            if (StringUtils.isNotBlank(orderDeliveryRpcQryDTO.getCityName())) {
                fcontactAddress = fcontactAddress + orderDeliveryRpcQryDTO.getCityName();
            }
            if (StringUtils.isNotBlank(orderDeliveryRpcQryDTO.getDistrictName())) {
                fcontactAddress = fcontactAddress + orderDeliveryRpcQryDTO.getDistrictName();
            }
            bean.setFContactAddress(fcontactAddress + orderDeliveryRpcQryDTO.getAddress());
        }
    }

    /**
     * 定制订单明细转换为map
     *
     * @param orderType
     * @param orderId
     * @return
     */
    private Map<Integer, OrderGoodsDiyRpcDTO> toOrderGoodsDiyRpcMap(OrderTypeRpcQryDTO orderType, Integer orderId) {
        Map<Integer, OrderGoodsDiyRpcDTO> diyRpcDTOMap = new HashMap<>();
        if (!orderType.getSpecialFlag().equals(ThirdCommonConstant.SPECIAL_FLAG_4)) {
            return diyRpcDTOMap;
        }
        // 定制订单
        List<OrderGoodsDiyRpcDTO> diyRpcDTOList = orderQryExe.listOrderGoodsDiyByOrderId(orderId);
        LOGGER.info("同步到erp的orderGoodsDiyDOList:" + JSON.toJSONString(diyRpcDTOList));
        diyRpcDTOMap = diyRpcDTOList.stream().collect(
            Collectors.toMap(OrderGoodsDiyRpcDTO::getOrderGoodsId, orderGoodsDiyRpcDTO -> orderGoodsDiyRpcDTO));
        return diyRpcDTOMap;
    }

    /**
     * 判断是否是拼团
     *
     * @param orderGoodsDistributorCostRpcQryDTOList
     * @return
     */
    public Boolean checkOrderIsGroup(List<OrderGoodsDistributorCostRpcQryDTO> orderGoodsDistributorCostRpcQryDTOList) {
        for (int x = 0; x < orderGoodsDistributorCostRpcQryDTOList.size(); x++) {
            if (orderGoodsDistributorCostRpcQryDTOList.get(x).getSpellGroupId() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param orderGoodsRpcDTOList
     * @param orgId
     *            ERP销售组织id
     * @param time
     *            下单时间
     * @param wareHouseMap
     * @param diyDOMap
     * @param goodsDistributorCostDOMap
     * @param codeDTORpcQryMap
     *            兑换卡订单兑换明细
     * @param remark
     *            订单备注
     * @return
     */
    private List<SaleOrderDetailQry> toErpOrderDetail(OrderDistributorDataRpcDTO orderDistributorDataRpcDTO,
        List<OrderGoodsRpcDTO> orderGoodsRpcDTOList, String orgId, String time, Map<Integer, String> wareHouseMap,
        Map<Integer, OrderGoodsDiyRpcDTO> diyDOMap,
        Map<Integer, OrderGoodsDistributorCostRpcQryDTO> goodsDistributorCostDOMap,
        Map<Integer, List<ExchangeCodeDTORpcQry>> codeDTORpcQryMap, String remark, Boolean postageExchangeFlag) {
        List<SaleOrderDetailQry> details = new ArrayList<>();
        // 图片map
        Map<Integer, PictureDTORpcQry> pictureDTORpcQryMap = new HashMap<>();
        // 材质和型号关联map
        Map<String, ModelMaterialRelevanceDTORpcQry> materialRelevanceDTORpcQryMap = new HashMap<>();
        // 型号Map
        Map<Integer, ModelDTORpcQry> modelDTORpcQryMap = new HashMap<>();

        for (OrderGoodsRpcDTO orderGoodsRpcDTO : orderGoodsRpcDTOList) {
            SaleOrderDetailQry orderDetailQry = new SaleOrderDetailQry();
            orderDetailQry.setFMATERIALID(orderGoodsRpcDTO.getItemCode());
            orderDetailQry.setFMATERIALNAME(orderGoodsRpcDTO.getItemName());
            orderDetailQry.setFQTY(String.valueOf(orderGoodsRpcDTO.getItemCount()));
            orderDetailQry.setFSTOCKORGID(orgId);
            // orderDetailQry.setFSTOCKORGID("100"); // 固定用bat组织
            orderDetailQry.setFSETTLEORGID(orgId);
            OrderGoodsDistributorCostRpcQryDTO orderGoodsDistributorCostRpcQryDTO =
                goodsDistributorCostDOMap.get(orderGoodsRpcDTO.getId());
            // 外币换算时统一保留两位小数位
            Integer scale = 4;
            if (orderDistributorDataRpcDTO.getCurrentRates() != null
                && orderDistributorDataRpcDTO.getCurrentRates().compareTo(new BigDecimal(1)) != 0) {
                scale = 2;
            }
            // B2B实际单价
            BigDecimal fB2BVIPPrice = orderGoodsDistributorCostRpcQryDTO.getSalePrice()
                .divide(orderDistributorDataRpcDTO.getCurrentRates(), scale, RoundingMode.HALF_UP);
            orderDetailQry.setF_B2BVIPPrice(String.valueOf(fB2BVIPPrice));
            if (OrderInfoConstant.ORDER_DISTRIBUTOR_DATA_DIRECT_FLAG_YES
                .equals(orderDistributorDataRpcDTO.getDirectFlag())
                || orderGoodsDistributorCostRpcQryDTO.getPlatformPrice() == null) {
                if (orderGoodsDistributorCostRpcQryDTO.getActualPrice() == null
                    || orderGoodsDistributorCostRpcQryDTO.getActualPrice().compareTo(BigDecimal.ZERO) == 0) {
                    // 0元订单
                    orderDetailQry.setFTaxPrice(String.valueOf(0.00));
                    orderDetailQry.setFIsFree("1");
                } else {
                    // 汇率转换
                    BigDecimal decimal = orderGoodsDistributorCostRpcQryDTO.getActualPrice()
                        .divide(orderDistributorDataRpcDTO.getCurrentRates(), scale, RoundingMode.HALF_UP);
                    orderDetailQry.setFTaxPrice(String.valueOf(decimal));
                    orderDetailQry.setFIsFree("0");
                }
            } else {
                if (orderGoodsDistributorCostRpcQryDTO.getPlatformPrice() == null
                    || orderGoodsDistributorCostRpcQryDTO.getPlatformPrice().compareTo(BigDecimal.ZERO) == 0) {
                    // 0元订单
                    orderDetailQry.setFTaxPrice(String.valueOf(0.00));
                    orderDetailQry.setFIsFree("1");
                } else {
                    BigDecimal fTxPrice = orderGoodsDistributorCostRpcQryDTO.getPlatformPrice()
                        .divide(orderDistributorDataRpcDTO.getCurrentRates(), scale, RoundingMode.HALF_UP);
                    orderDetailQry.setFTaxPrice(String.valueOf(fTxPrice));
                    orderDetailQry.setFIsFree("0");
                }
            }
            OrderGoodsDiyRpcDTO orderGoodsDiyRpcDTO = diyDOMap.get(orderGoodsRpcDTO.getId());
            if (orderGoodsDiyRpcDTO != null) {
                // 定制订单
                if (orderGoodsDiyRpcDTO.getPictureId() > 0) {
                    PictureDTORpcQry pictureDTORpcQry = pictureDTORpcQryMap.get(orderGoodsDiyRpcDTO.getPictureId());
                    if (pictureDTORpcQry == null) {
                        com.bat.dubboapi.flexible.common.Response<PictureDTORpcQry> pictureResp =
                            pictureServiceRpc.getDTORpcById(orderGoodsDiyRpcDTO.getPictureId());
                        if (pictureResp == null) {
                            throw ThirdPartyException
                                .buildException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
                        }
                        if (!pictureResp.isSuccess()) {
                            throw ThirdPartyException.buildException(pictureResp.getErrCode(),
                                pictureResp.getErrMessage());
                        }
                        pictureDTORpcQry = pictureResp.getData();
                        pictureDTORpcQryMap.put(pictureDTORpcQry.getId(), pictureDTORpcQry);
                    }
                    orderDetailQry.setF_PictureID(pictureDTORpcQry.getName());
                }
                orderDetailQry.setFB2BMaterNo(String.valueOf(orderGoodsDiyRpcDTO.getId()));
                ModelMaterialRelevanceDTORpcQry modelMaterialRelevanceDTORpcQry = materialRelevanceDTORpcQryMap
                    .get(orderGoodsDiyRpcDTO.getModelId() + "_" + orderGoodsDiyRpcDTO.getMaterialId());
                if (modelMaterialRelevanceDTORpcQry == null) {
                    com.bat.dubboapi.flexible.common.Response<
                        ModelMaterialRelevanceDTORpcQry> modelMaterialRelevanceDTORpcQryResponse =
                            modelMaterialRelevanceServiceRpc.getByModelIdAndMaterialId(orderGoodsDiyRpcDTO.getModelId(),
                                orderGoodsDiyRpcDTO.getMaterialId());
                    if (modelMaterialRelevanceDTORpcQryResponse == null) {
                        throw ThirdPartyException
                            .buildException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
                    }
                    if (!modelMaterialRelevanceDTORpcQryResponse.isSuccess()) {
                        throw ThirdPartyException.buildException(modelMaterialRelevanceDTORpcQryResponse.getErrCode(),
                            modelMaterialRelevanceDTORpcQryResponse.getErrMessage());
                    }
                    modelMaterialRelevanceDTORpcQry = modelMaterialRelevanceDTORpcQryResponse.getData();
                    materialRelevanceDTORpcQryMap.put(
                        orderGoodsDiyRpcDTO.getModelId() + "_" + orderGoodsDiyRpcDTO.getMaterialId(),
                        modelMaterialRelevanceDTORpcQry);
                }

                orderDetailQry.setFBomId(modelMaterialRelevanceDTORpcQry.getBomCode());
                orderDetailQry.setF_FlexProductType(orderGoodsDiyRpcDTO.getModelName());

                ModelDTORpcQry modelDTORpcQry = modelDTORpcQryMap.get(orderGoodsDiyRpcDTO.getModelId());
                if (modelDTORpcQry == null) {
                    com.bat.dubboapi.flexible.common.Response<ModelDTORpcQry> modelDTORpcQryResponse =
                        modelServiceRpc.getByModelIdOrModelNo(orderGoodsDiyRpcDTO.getModelId(), null);
                    if (modelDTORpcQryResponse == null) {
                        throw ThirdPartyException
                            .buildException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
                    }
                    if (!modelDTORpcQryResponse.isSuccess()) {
                        throw ThirdPartyException.buildException(modelDTORpcQryResponse.getErrCode(),
                            modelDTORpcQryResponse.getErrMessage());
                    }
                    modelDTORpcQry = modelDTORpcQryResponse.getData();
                    modelDTORpcQryMap.put(modelDTORpcQry.getId(), modelDTORpcQry);
                }
                orderDetailQry.setF_FlexProductTypeNo(modelDTORpcQry.getModelNo());
                // 设置材质名称
                orderDetailQry.setFB2BMaterName(orderGoodsDiyRpcDTO.getMaterialName());
            }
            orderDetailQry.setFENTRYDISCOUNTRATE(time);
            String warehouseNo = wareHouseMap.get(orderGoodsRpcDTO.getItemId());
            if (StringUtils.isNotBlank(warehouseNo)) {
                orderDetailQry.setFSOSTOCKID(warehouseNo);
            }

            // 处理兑换卡的内容
            List<ExchangeCodeDTORpcQry> exchangeCodeDTORpcQryList = codeDTORpcQryMap.get(orderGoodsRpcDTO.getId());
            if (exchangeCodeDTORpcQryList != null && exchangeCodeDTORpcQryList.size() > 0) {
                List<ExchangePlainCodeOrderErp> F_PAEZ_CardOrderEntity = new ArrayList<>();
                exchangeCodeDTORpcQryList.stream().forEach(exchangeCode -> {
                    ExchangePlainCodeOrderErp codeOrderErp = new ExchangePlainCodeOrderErp();
                    codeOrderErp.setFCardNo(exchangeCode.getPlainCode());
                    codeOrderErp.setFCardMark(remark);
                    F_PAEZ_CardOrderEntity.add(codeOrderErp);
                });
                orderDetailQry.setF_PAEZ_CardOrderEntity(F_PAEZ_CardOrderEntity);

                // 邮费付款单
                if (postageExchangeFlag) {
                    List<OrderGoodsExchangeCodeRpcQryDTO> exchangeCodeRpcQryDTOList =
                        orderQryExe.getOrderGoodsExchangeCodeByUserOrderId(orderGoodsRpcDTO.getOrderId());
                    if (exchangeCodeRpcQryDTOList != null && exchangeCodeRpcQryDTOList.size() > 0) {
                        OrderGoodsExchangeCodeRpcQryDTO orderGoodsExchangeCodeRpcQryDTO =
                            exchangeCodeRpcQryDTOList.get(0);
                        if (orderGoodsExchangeCodeRpcQryDTO.getExchangeOrderId() != null) {
                            orderDetailQry
                                .setF_CardSONo(String.valueOf(orderGoodsExchangeCodeRpcQryDTO.getExchangeOrderId()));
                        }
                        if (orderGoodsExchangeCodeRpcQryDTO.getDistributorId() != null) {
                            // 发卡分销商的信息
                            DistributorRpcDTO distributor = distributorServiceQryExe
                                .getDistributorById(orderGoodsExchangeCodeRpcQryDTO.getDistributorId());
                            orderDetailQry.setF_CardCustId(distributor.getErpNo());
                        }
                        if (orderGoodsExchangeCodeRpcQryDTO.getSalesId() != null) {
                            Response<UserRpcDTO> userRpcDTOResponse =
                                systemUserServiceRpc.getUserById(orderGoodsExchangeCodeRpcQryDTO.getSalesId());
                            if (!userRpcDTOResponse.isSuccess()) {
                                throw ThirdPartyException.buildException(userRpcDTOResponse.getErrCode(),
                                    userRpcDTOResponse.getErrMessage());
                            }
                            UserRpcDTO userRpcDTO = userRpcDTOResponse.getData();
                            orderDetailQry.setF_CardSalerId(userRpcDTO.getErpUserNo());
                        }
                    }

                }
            }

            // 服务费(服务费总数量及单价)
            /* if (good.getHasChargeNum() != null && good.getServiceFee() != null) {
                detailRequest.setServiceBoxNum(new BigDecimal(good.getHasChargeNum().toString()).toString());
                detailRequest.setServiceFee(good.getServiceFee().toString());
            }*/
            // 设置行序号
            orderDetailQry.setFSerialNumber(String.valueOf(orderGoodsRpcDTO.getSerialNumber()));

            BigDecimal rebateVoucherAmount = orderGoodsDistributorCostRpcQryDTO.getRebateVoucherAmount();
            if(rebateVoucherAmount!=null){
                // 明细返利金额单价
                BigDecimal flPrice = rebateVoucherAmount
                        .divide(orderDistributorDataRpcDTO.getCurrentRates(), scale, RoundingMode.HALF_UP);
                orderDetailQry.setF_FLPrice(String.valueOf(flPrice));
                // 明细返利金额
                BigDecimal flAmount = flPrice.multiply(new BigDecimal(orderGoodsRpcDTO.getItemInCount()));
                orderDetailQry.setF_FLAmount(String.valueOf(flAmount));
            }
            details.add(orderDetailQry);
        }
        return details;
    }

    public ErpOrderChangeCmd toErpOrderChangeCmd(ErpOrderChangeRequest request) {
        ErpOrderChangeCmd changeCmd = new ErpOrderChangeCmd();
        BeanUtils.copyProperties(request, changeCmd);
        List<ErpOrderChangeDetailCmd> orderDetails = new ArrayList<>();
        changeCmd.setOrderDetails(orderDetails);
        request.getOrderDetails().forEach(orderDetail -> {
            ErpOrderChangeDetailCmd changeDetailCmd = new ErpOrderChangeDetailCmd();
            BeanUtils.copyProperties(orderDetail, changeDetailCmd);
            orderDetails.add(changeDetailCmd);
        });
        return changeCmd;
    }

    public OrderCheckCmd toOrderCheckCmd(ErpOrderCheckRequest request) {
        OrderCheckCmd checkCmd = new OrderCheckCmd();
        checkCmd.setOrderErpNo(request.getOrderNo());
        // 1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭)
        if (request.getOrderStatus().equals(ThirdCommonConstant.ORDER_ERP_STATUS_1)) {
            checkCmd.setOrderStatus(OrderStatus.PENDING.getValue());
        } else if (request.getOrderStatus().equals(ThirdCommonConstant.ORDER_ERP_STATUS_2)) {
            checkCmd.setOrderStatus(OrderStatus.CONFIRMED.getValue());
        } else if (request.getOrderStatus().equals(ThirdCommonConstant.ORDER_ERP_STATUS_5)) {
            checkCmd.setOrderStatus(OrderStatus.CANCELLED.getValue());
        } else {
            throw ThirdPartyException.buildException(ErrorCode.B_ERP_ORDER_CHECK_ERROR,
                MessageUtils.get(ErrorCode.B_ERP_ORDER_CHECK_ERROR));
        }
        return checkCmd;
    }

}
