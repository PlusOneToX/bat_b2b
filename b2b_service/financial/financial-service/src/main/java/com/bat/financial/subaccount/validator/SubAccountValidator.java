package com.bat.financial.subaccount.validator;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillAmountCmd;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.common.constant.third.OrderConstant;
import com.bat.financial.common.error.subaccount.SubAccountErrorCode;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.subaccount.executor.OrderSubAccountBillCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountBillQryExe;
import com.bat.financial.subaccount.executor.OrderSubAccountCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountQryExe;

@Component
public class SubAccountValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubAccountValidator.class);

    @Autowired
    private OrderSubAccountBillQryExe orderSubAccountBillQryExe;

    @Autowired
    private OrderSubAccountQryExe orderSubAccountQryExe;

    @Autowired
    private OrderSubAccountBillCmdExe orderSubAccountBillCmdExe;

    @Autowired
    private OrderSubAccountCmdExe orderSubAccountCmdExe;

    @DubboReference(check = false, timeout = 6000, retries = 0)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    /**
     * 校验金额修改
     * 
     * @param billAmountCmd
     * @param accountBillDO
     */
    public static void validAmountByUpdate(OrderSubAccountBillAmountCmd billAmountCmd,
        OrderSubAccountBillDO accountBillDO) {
        if (billAmountCmd.getActualSubAccountAmount().compareTo(accountBillDO.getMaxSubAccountAmount()) > 0) {
            // 实际分账金额大于最大可分账金额
            LOGGER.error("分账金额大于可分账金额{}", JSON.toJSONString(billAmountCmd));
            throw FinancialException.buildException(SubAccountErrorCode.ORDER_SUB_ACCOUNT_AMOUNT_EXCESS_ERROR,
                MessageUtils.get(SubAccountErrorCode.ORDER_SUB_ACCOUNT_AMOUNT_EXCESS_ERROR) + "【"
                    + accountBillDO.getMaxSubAccountAmount() + "】");
        }
        if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL.equals(accountBillDO.getStatus())) {
            // 已分账不允许修改
            LOGGER.error("已分账的不允许修改实际分账金额{}", JSON.toJSONString(billAmountCmd));
            throw FinancialException
                .buildException(SubAccountErrorCode.ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_STATUS_SUCCESS);
        }
        if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE.equals(accountBillDO.getStatus())) {
            // 已关闭不允许修改
            LOGGER.error("已关闭的不允许修改实际分账金额{}", JSON.toJSONString(billAmountCmd));
            throw FinancialException
                .buildException(SubAccountErrorCode.ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_STATUS_CLOSE);
        }
    }

    /**
     * 判断分账流水状态 分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账
     * 
     * @param billIdList
     * @return
     */
    public List<OrderSubAccountBillDO> validBillStatus(List<Integer> billIdList) {
        List<OrderSubAccountBillDO> billDOList = orderSubAccountBillQryExe.listByIdList(billIdList);
        billDOList.forEach(orderSubAccountBillDO -> {
            if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE.equals(orderSubAccountBillDO.getStatus())) {
                LOGGER.error("流水已关闭、不能再分账{}", JSON.toJSONString(orderSubAccountBillDO));
                throw FinancialException.buildException(SubAccountErrorCode.BILL_SUB_ACCOUNT_STATUS_CLOSE,
                    "【" + orderSubAccountBillDO.getId() + "】"
                        + MessageUtils.get(SubAccountErrorCode.BILL_SUB_ACCOUNT_STATUS_CLOSE));
            }
            if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL.equals(orderSubAccountBillDO.getStatus())) {
                LOGGER.error("流水已分账、不能再分账{}", JSON.toJSONString(orderSubAccountBillDO));
                throw FinancialException.buildException(SubAccountErrorCode.BILL_SUB_ACCOUNT_STATUS_SUCCESS,
                    "【" + orderSubAccountBillDO.getId() + "】"
                        + MessageUtils.get(SubAccountErrorCode.BILL_SUB_ACCOUNT_STATUS_SUCCESS));
            }
        });
        return billDOList;
    }

    /**
     * 判断订单是否已分账或者已关闭
     * 
     * @param id
     * @return
     */
    public OrderSubAccountDO validOrderSubStatus(Integer id) {
        OrderSubAccountDO orderSubAccountDO = orderSubAccountQryExe.getById(id);
        List<OrderSubAccountBillDO> billDOList =
            orderSubAccountBillQryExe.listByCondition(id, OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);

        Response<OrderDistributorDataRpcDTO> response = orderDistributorDataServiceRpc
            .getByOrderIdAndDistributorId(orderSubAccountDO.getOrderId(), orderSubAccountDO.getDistributorId());
        if (!response.isSuccess()) {
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
        OrderDistributorDataRpcDTO orderDistributorDataRpcDTO = response.getData();
        Date date = new Date();
        if (OrderConstant.ORDER_STATUS_CANCEL.equals(orderDistributorDataRpcDTO.getOrderStatus())
            && !OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE.equals(orderSubAccountDO.getStatus())) {
            LOGGER.error("订单已取消、不能再分账、设置未分账的为已关闭");
            orderSubAccountDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE);
            orderSubAccountDO.setUpdateTime(date);
            orderSubAccountCmdExe.update(orderSubAccountDO);
            if (billDOList != null && billDOList.size() > 0) {
                billDOList.forEach(orderSubAccountBillDO -> {
                    orderSubAccountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE);
                    orderSubAccountBillDO.setFailReason("订单已取消、不能再分账、设置未分账的为已关闭");
                    orderSubAccountBillDO.setUpdateTime(date);
                });
                orderSubAccountBillCmdExe.batchUpdate(billDOList);
            }
            throw FinancialException.buildException(SubAccountErrorCode.ORDER_STATUS_CLOSE,
                "【" + orderSubAccountDO.getOrderId() + "】" + MessageUtils.get(SubAccountErrorCode.ORDER_STATUS_CLOSE));
        }
        if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE.equals(orderSubAccountDO.getStatus())) {
            LOGGER.error("订单已关闭、不能再分账{}", JSON.toJSONString(orderSubAccountDO));
            if (billDOList != null && billDOList.size() > 0) {
                billDOList.forEach(orderSubAccountBillDO -> {
                    orderSubAccountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE);
                    orderSubAccountBillDO.setFailReason("订单已关闭、不能再分账");
                    orderSubAccountBillDO.setUpdateTime(date);
                });
                orderSubAccountBillCmdExe.batchUpdate(billDOList);
            }
            throw FinancialException.buildException(SubAccountErrorCode.ORDER_SUB_ACCOUNT_STATUS_CLOSE,
                "【" + orderSubAccountDO.getOrderId() + "】"
                    + MessageUtils.get(SubAccountErrorCode.ORDER_SUB_ACCOUNT_STATUS_CLOSE));
        }
        if (OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL.equals(orderSubAccountDO.getStatus())) {
            LOGGER.error("订单已分账、不能再分账{}", JSON.toJSONString(orderSubAccountDO));
            if (billDOList != null && billDOList.size() > 0) {
                billDOList.forEach(orderSubAccountBillDO -> {
                    orderSubAccountBillDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE);
                    orderSubAccountBillDO.setFailReason("订单已分账、不能再分账");
                    orderSubAccountBillDO.setUpdateTime(date);
                });
                orderSubAccountBillCmdExe.batchUpdate(billDOList);
            }
            throw FinancialException.buildException(SubAccountErrorCode.ORDER_SUB_ACCOUNT_STATUS_SUCCESS,
                "【" + orderSubAccountDO.getOrderId() + "】"
                    + MessageUtils.get(SubAccountErrorCode.ORDER_SUB_ACCOUNT_STATUS_SUCCESS));
        }
        return orderSubAccountDO;
    }
}
