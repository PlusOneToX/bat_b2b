package com.bat.order.service.third.financial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.constant.DistributorConstant;
import com.bat.order.service.common.error.OrderCustomerDataErrorCode;
import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.third.flexible.FlexibleQryExeRpc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountRatioRpcDTOQry;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountSalemanRpcDTOQry;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountUserConfigRpcDTOQry;
import com.bat.dubboapi.financial.subaccount.dto.OrderSubAccountCmd;
import com.bat.dubboapi.financial.subaccount.dto.SubAccountReceiveCmd;
import com.bat.dubboapi.flexible.shop.dto.ShopDTORpcQry;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.cost.OrderDistributorCostServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.third.distributor.DistributorQryExeRpc;

@Component
public class FinancialCmdExeRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialCmdExeRpc.class);

    @Autowired
    private DistributorQryExeRpc distributorQryExeRpc;

    @Autowired
    private FlexibleQryExeRpc flexibleQryExeRpc;

    @Autowired
    private OrderDistributorCostServiceI orderDistributorCostServiceI;

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private FinancialQryExeRpc financialQryExeRpc;

    /**
     * 处理分账
     * 
     * @param customerDataDO
     * @param customerCostDO
     */
    public void dealwithShopSubAccount(OrderCustomerDataDO customerDataDO, OrderCustomerCostDO customerCostDO,
        OrderDistributorCostDO orderDistributorCostDO) {

        OrderSubAccountCmd orderSubAccountCmd = new OrderSubAccountCmd();
        orderSubAccountCmd.setOutTradeNo(customerCostDO.getOutTradeNo());
        // 处理店铺分账订单
        DistributorExtendDataRpcDTO extendDataRpcDTO =
            distributorQryExeRpc.getByDistributorId(customerDataDO.getDistributorId());

        if (DistributorConstant.Extend_CUSTOMER_FLAG_NO.equals(extendDataRpcDTO.getCustomerFlag())) {
            LOGGER.info("分销商id{}没有开C端模式、不属于分账订单", customerDataDO.getDistributorId());
            return;
        }
        if (DistributorConstant.Extend_CUSTOMER_MODE_SELF.equals(extendDataRpcDTO.getSubAccountFlag())) {
            LOGGER.info("分销商id{}不属于自己收款、不属于分账订单", customerDataDO.getDistributorId());
            return;
        }
        if (extendDataRpcDTO.getSubAccountFlag() == null
            || DistributorConstant.Extend_SUB_ACCOUNT_FLAG_NO.equals(extendDataRpcDTO.getSubAccountFlag())) {
            LOGGER.info("分销商id{}没有开分账、不属于分账订单", customerDataDO.getDistributorId());
            return;
        }
        // 查询分销商下面所有的业务员
        List<DistributorSubAccountSalemanRpcDTOQry> salemanRpcDTOQryList =
            distributorQryExeRpc.listAllSalemanByDistributorId(customerDataDO.getDistributorId());
        if (salemanRpcDTOQryList == null || salemanRpcDTOQryList.size() == 0) {
            LOGGER.info("分销商id{}下面没有业务员、不属于分账订单", customerDataDO.getDistributorId());
            return;
        }
        Map<Integer, DistributorSubAccountSalemanRpcDTOQry> salemanRpcDTOQryMap = salemanRpcDTOQryList.stream().collect(
            Collectors.toMap(DistributorSubAccountSalemanRpcDTOQry::getId, salemanRpcDTOQry -> salemanRpcDTOQry));

        /*      List<AccountWxDistributorRpcQryDTO> accountWxDistributorRpcQryDTOList = financialQryExeRpc.listWxPayAccountByCondition(customerDataDO.getDistributorId(), null,
                FinancialConstant.ACCOUNT_TYPE_PARTNER);
        if(accountWxDistributorRpcQryDTOList ==null || accountWxDistributorRpcQryDTOList.size()==0){
            LOGGER.info("分销商id{}尚未配置服务商账户、不属于分账订单",customerDataDO.getDistributorId());
            return;
        }
        AccountWxDistributorRpcQryDTO wxDistributorRpcQryDTO = accountWxDistributorRpcQryDTOList.get(0);*/
        if (StringUtils.isBlank(customerDataDO.getShopCode())) {
            LOGGER.info("订单id{}不属于门店订单、不属于分账订单", customerDataDO.getOrderId());
            return;
        }
        // 开始计算分账
        ShopDTORpcQry shopDTORpcQry = flexibleQryExeRpc.getByDistributorIdAndShopCode(customerDataDO.getDistributorId(),
            customerDataDO.getShopCode());
        if (shopDTORpcQry == null) {
            String errMsg = MessageUtils.get(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SHOP_CODE_ERROR) + "【"
                + customerDataDO.getShopCode() + "】";
            LOGGER.error("订单{}分账失败、原因是{}", customerDataDO.getOrderId(), errMsg);
            throw OrderException.buildException(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SHOP_CODE_ERROR, errMsg);
        }
        if (Constant.OPEN_FLAG_0.equals(shopDTORpcQry.getOpenFlag())) {
            String errMsg = MessageUtils.get(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SHOP_DISABLED_ERROR) + "【"
                + customerDataDO.getShopCode() + "】";
            LOGGER.error("订单{}分账失败、原因是{}", customerDataDO.getOrderId(), errMsg);
            throw OrderException.buildException(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SHOP_DISABLED_ERROR,
                errMsg);
        }
        if (shopDTORpcQry.getUserConfigId() == null || shopDTORpcQry.getSalemanId() == null) {
            LOGGER.info("门店{}没有关联分账配置、不做处理", customerDataDO.getShopCode());
            return;
        }
        DistributorSubAccountUserConfigRpcDTOQry userConfigRpcDTOQry =
            distributorQryExeRpc.getUserSubAccountConfigById(shopDTORpcQry.getUserConfigId());

        List<DistributorSubAccountRatioRpcDTOQry> ratioRpcDTOQryList =
            distributorQryExeRpc.listBySubAccountConfigIdOrderByLevelSequenceAsc(shopDTORpcQry.getUserConfigId());
        DistributorSubAccountSalemanRpcDTOQry saleman = salemanRpcDTOQryMap.get(shopDTORpcQry.getSalemanId());
        if (Constant.OPEN_FLAG_0.equals(saleman.getOpenFlag())) {
            String errMsg = MessageUtils.get(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SALEMAN_DISABLED_ERROR) + "【"
                + saleman.getName() + "】";
            LOGGER.error("业务员禁用、不进行分账{}", errMsg);
            throw OrderException.buildException(OrderCustomerDataErrorCode.ORDER_DISTRIBUTOR_SALEMAN_DISABLED_ERROR,
                errMsg);
        }
        List<SubAccountReceiveCmd> receiveCmdList = new ArrayList<>();
        // 是否开始分账
        Boolean subAccountFlag = false;
        Integer parentSalemanId = saleman.getParentId();
        // 获取订单分账金额
        LOGGER.info("获取订单分账金额 userConfigRpcDTOQry：{}", JSON.toJSONString(userConfigRpcDTOQry));
        LOGGER.info("获取订单分账金额 customerDataDO：{}", JSON.toJSONString(customerDataDO));
        LOGGER.info("获取订单分账金额 customerCostDO：{}", JSON.toJSONString(customerCostDO));
        LOGGER.info("获取订单分账金额 orderDistributorCostDO：{}", JSON.toJSONString(orderDistributorCostDO));
        BigDecimal orderSubAccountAmount =
            getOrderSubAccountAmount(userConfigRpcDTOQry, customerDataDO, customerCostDO, orderDistributorCostDO);
        if (orderSubAccountAmount.compareTo(BigDecimal.ZERO) < 1) {
            LOGGER.info("订单{}分账金额不大于0、不进行分账{}", customerDataDO.getOrderId(), orderSubAccountAmount);
            return;
        }
        LOGGER.info("查询回来的分账比例列表{}", JSON.toJSONString(ratioRpcDTOQryList));
        BigDecimal actualSubAccountAmount = BigDecimal.ZERO;
        for (int y = 0; y < ratioRpcDTOQryList.size(); y++) {
            DistributorSubAccountRatioRpcDTOQry ratioRpcDTOQry = ratioRpcDTOQryList.get(y);
            if (subAccountFlag) {
                // 上级
                DistributorSubAccountSalemanRpcDTOQry salemanRpcDTOQry = salemanRpcDTOQryMap.get(parentSalemanId);
                if (salemanRpcDTOQry == null) {
                    LOGGER.info("找不到上级业务员{}、不分账", parentSalemanId);
                    continue;
                }
                SubAccountReceiveCmd receiveCmd = new SubAccountReceiveCmd();
                receiveCmd.setMerchantNumber(salemanRpcDTOQry.getMerchantNumber());
                receiveCmd.setOpenId(salemanRpcDTOQry.getOpenId());
                BigDecimal subAccountAmount =
                    orderSubAccountAmount.multiply(ratioRpcDTOQry.getRatio()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                actualSubAccountAmount = actualSubAccountAmount.add(subAccountAmount);
                receiveCmd.setAmount(subAccountAmount);
                receiveCmd.setLevelId(ratioRpcDTOQry.getLevelId());
                receiveCmd.setLevelName(ratioRpcDTOQry.getLevelName());
                receiveCmd.setSalemanId(salemanRpcDTOQry.getId());
                receiveCmd.setSalemanName(salemanRpcDTOQry.getName());
                receiveCmd.setRatio(ratioRpcDTOQry.getRatio());
                parentSalemanId = salemanRpcDTOQry.getParentId();
                receiveCmdList.add(receiveCmd);
            }
            if (ratioRpcDTOQry.getLevelId().equals(saleman.getLevelId())) {
                SubAccountReceiveCmd receiveCmd = new SubAccountReceiveCmd();
                receiveCmd.setMerchantNumber(saleman.getMerchantNumber());
                receiveCmd.setOpenId(saleman.getOpenId());
                BigDecimal subAccountAmount =
                    orderSubAccountAmount.multiply(ratioRpcDTOQry.getRatio()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                actualSubAccountAmount = actualSubAccountAmount.add(subAccountAmount);
                receiveCmd.setAmount(subAccountAmount);
                receiveCmd.setSalemanId(saleman.getId());
                receiveCmd.setSalemanName(saleman.getName());
                receiveCmd.setLevelId(ratioRpcDTOQry.getLevelId());
                receiveCmd.setLevelName(ratioRpcDTOQry.getLevelName());
                receiveCmd.setRatio(ratioRpcDTOQry.getRatio());
                receiveCmdList.add(receiveCmd);
                if (parentSalemanId > 0) {
                    // 大于0表示有上级
                    subAccountFlag = true;
                }

            }
        }
        OrderInfoDO orderInfoDO = orderInfoServiceI.getById(customerDataDO.getOrderId());
        orderSubAccountCmd.setReceiveCmdList(receiveCmdList);
        orderSubAccountCmd.setDistributorId(customerDataDO.getDistributorId());
        orderSubAccountCmd.setDistributorName(customerDataDO.getDistributorName());
        orderSubAccountCmd.setShopId(shopDTORpcQry.getId());
        orderSubAccountCmd.setShopName(customerDataDO.getShopName());
        orderSubAccountCmd.setOrderId(customerDataDO.getOrderId());
        orderSubAccountCmd.setOrderNo(orderInfoDO.getOrderNo());
        orderSubAccountCmd.setPayAmount(customerCostDO.getPayAmount());
        // 处理精度问题
        orderSubAccountCmd.setMaxSubAccountAmount(actualSubAccountAmount);
        // 订单最大分账金额(可能有进度问题、最大分账必须要小于理论分账)
        orderSubAccountCmd.setActualSubAccountAmount(actualSubAccountAmount);
        if (DistributorConstant.SUB_ACCOUNT_TYPE_PROFIT.equals(userConfigRpcDTOQry.getAmountType())) {
            // 按照利润
            orderSubAccountCmd.setProfitAccount(orderSubAccountAmount);
        }
        // 发送消息
        messageSendService.orderSubAccount(orderSubAccountCmd);
    }

    /**
     * 获取分账金额
     * 
     * @param userConfigRpcDTOQry
     * @param orderCustomerDataDO
     * @param customerCostDO
     * @return
     */
    private BigDecimal getOrderSubAccountAmount(DistributorSubAccountUserConfigRpcDTOQry userConfigRpcDTOQry,
        OrderCustomerDataDO orderCustomerDataDO, OrderCustomerCostDO customerCostDO,
        OrderDistributorCostDO orderDistributorCostDO) {
        // 按实际金额付款
        if (DistributorConstant.SUB_ACCOUNT_TYPE_ACTUAL_PAYMENT.equals(userConfigRpcDTOQry.getAmountType())) {
            // 按照实际支付
            return customerCostDO.getPaidAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        // 应付款金额 不代表实际付了款
        if (orderDistributorCostDO == null || orderDistributorCostDO.getPayAmount() == null) {
            orderDistributorCostDO = orderDistributorCostServiceI
                .getByOrderIdAndDistributorId(orderCustomerDataDO.getOrderId(), orderCustomerDataDO.getDistributorId());
            if (orderDistributorCostDO == null || orderDistributorCostDO.getPayAmount() == null) {
                LOGGER.error("尚未生成分销订单数据 或者数据错误、C端订单id{}、分销商id{}", orderCustomerDataDO.getOrderId(),
                    orderCustomerDataDO.getDistributorId());
                throw OrderException
                    .buildException(OrderCustomerDataErrorCode.ORDER_SUB_ACCOUNT_FAIL_BY_DISTRIBUTION_COST_NULL);
            }
        }
        // if(orderDistributorCostDO.getDistributionAmount()==null){
        // orderDistributorCostDO.setDistributionAmount(BigDecimal.ZERO);
        // }
        // BigDecimal bigDecimal =
        // orderDistributorCostDO.getPayAmount().subtract(orderDistributorCostDO.getDistributionAmount());
        // 返回利润 = 订单实付金额 - 订单应付金额
        return customerCostDO.getPaidAmount().subtract(orderDistributorCostDO.getPayAmount()).setScale(4,
            BigDecimal.ROUND_HALF_UP);
    }
}
