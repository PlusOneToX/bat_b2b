package com.bat.financial.subaccount;

import static com.bat.financial.common.constant.subaccount.OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_ALL;
import static com.bat.financial.common.constant.subaccount.OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_CLOSE;
import static com.bat.financial.common.error.subaccount.SubAccountErrorCode.ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_MORE_THAN_ONE_ORDER;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.Response;
import com.bat.financial.api.subaccount.OrderSubAccountBillServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillAmountCmd;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillBatchCmd;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillDTO;
import com.bat.financial.api.subaccount.dto.OrderSubAccountIdCmd;
import com.bat.financial.common.constant.subaccount.OrderSubAccountConstant;
import com.bat.financial.common.constant.third.OrderConstant;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.pay.WxPayPartnerServiceImpl;
import com.bat.financial.pay.executor.PayBillsCustomerQryExe;
import com.bat.financial.subaccount.convertor.SubAccountConvertor;
import com.bat.financial.subaccount.executor.OrderSubAccountBillCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountBillQryExe;
import com.bat.financial.subaccount.executor.OrderSubAccountCmdExe;
import com.bat.financial.subaccount.executor.OrderSubAccountQryExe;
import com.bat.financial.subaccount.validator.SubAccountValidator;

@Service
public class OrderSubAccountBillServiceImpl implements OrderSubAccountBillServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSubAccountBillServiceImpl.class);

    @Autowired
    private OrderSubAccountBillQryExe orderSubAccountBillQryExe;

    @Autowired
    private OrderSubAccountBillCmdExe orderSubAccountBillCmdExe;

    @Autowired
    private OrderSubAccountQryExe orderSubAccountQryExe;

    @Autowired
    private OrderSubAccountCmdExe orderSubAccountCmdExe;

    @Autowired
    private PayBillsCustomerQryExe payBillsCustomerQryExe;

    @Autowired
    private WxPayPartnerServiceImpl wxPayPartnerServiceImpl;

    @Autowired
    private SubAccountValidator subAccountValidator;

    @DubboReference(check = false, timeout = 9000, retries = 0)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    /**
     * 根据订单分账id查询分账流水列表
     * 
     * @param orderSubAccountId
     * @return
     */
    @Override
    public List<OrderSubAccountBillDTO> listDTOByOrderSubAccountId(Integer orderSubAccountId) {
        List<OrderSubAccountBillDO> list = orderSubAccountBillQryExe.listByCondition(orderSubAccountId, null);
        return SubAccountConvertor.toOrderSubAccountBillDTOList(list);
    }

    /**
     * 更新 分账明细 并分账
     * 
     * @param billBatchCmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response updateActualSubAccountAmount(OrderSubAccountBillBatchCmd billBatchCmd) {
        List<Integer> billIdList =
            billBatchCmd.getBillAmountCmdList().stream().map(OrderSubAccountBillAmountCmd::getId).collect(toList());
        List<OrderSubAccountBillDO> doList = orderSubAccountBillQryExe.listByIdList(billIdList);
        List<Integer> orderSubAccountIdList =
            doList.stream().map(OrderSubAccountBillDO::getOrderSubAccountId).distinct().collect(toList());
        if (orderSubAccountIdList.size() > 1) {
            LOGGER.error("修改分账金额的订单数量不能大于1{}", JSON.toJSONString(billBatchCmd));
            return Response.buildFailure(ORDER_SUB_ACCOUNT_AMOUNT_UPDATE_FAIL_MORE_THAN_ONE_ORDER);
        }
        OrderSubAccountDO orderSubAccountDO;
        try {
            // 判断订单是否分账完毕
            orderSubAccountDO = subAccountValidator.validOrderSubStatus(orderSubAccountIdList.get(0));
        } catch (FinancialException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }

        // key 分账明细id value 明细id与实际分账金额
        Map<Integer, OrderSubAccountBillAmountCmd> amountCmdMap = billBatchCmd.getBillAmountCmdList().stream()
            .collect(Collectors.toMap(OrderSubAccountBillAmountCmd::getId, cmd -> cmd));

        BigDecimal gapAmount = BigDecimal.ZERO;
        // 需要进行分账的列表(金额大于0)
        List<OrderSubAccountBillDO> subAccountBillDOList = new ArrayList<>();
        Date date = new Date();
        for (OrderSubAccountBillDO accountBillDO : doList) {
            // 实际金额
            OrderSubAccountBillAmountCmd billAmountCmd = amountCmdMap.get(accountBillDO.getId());
            SubAccountValidator.validAmountByUpdate(billAmountCmd, accountBillDO);
            // 实际金额差（之前的金额-实际金额）
            gapAmount = gapAmount
                .add(accountBillDO.getActualSubAccountAmount().subtract(billAmountCmd.getActualSubAccountAmount()));
            accountBillDO.setActualSubAccountAmount(billAmountCmd.getActualSubAccountAmount());
            // 当前分账人员 最大分账金额为0 或者 最大分账金额不为0 实际分账为0，直接成功。不参与微信实际分账
            if (accountBillDO.getMaxSubAccountAmount().compareTo(BigDecimal.ZERO) == 0
                || billAmountCmd.getActualSubAccountAmount().compareTo(BigDecimal.ZERO) == 0) {
                // 金额为0、直接修改为成功 实际分账改为0
                accountBillDO.setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_SUCCESS);
                accountBillDO.setStatus(ORDER_SUB_ACCOUNT_STATUS_ALL);
                accountBillDO.setActualSubAccountAmount(BigDecimal.ZERO);
                accountBillDO.setFailReason(null);
                accountBillDO.setSuccessTime(date);
            } else {
                subAccountBillDOList.add(accountBillDO);
            }
            accountBillDO.setUpdateTime(date);
            orderSubAccountBillCmdExe.update(accountBillDO);
        }

        // 实际分账金额差
        orderSubAccountDO.setActualSubAccountAmount(
            orderSubAccountDO.getActualSubAccountAmount().subtract(gapAmount).setScale(2, RoundingMode.HALF_UP));
        if (subAccountBillDOList.size() == 0) {
            // 全部为默认成功的情况
            orderSubAccountDO.setSubAccountFailFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_NO);
            // 判断是否全都分账成功了
            List<OrderSubAccountBillDO> billDOList =
                orderSubAccountBillQryExe.listByCondition(orderSubAccountDO.getId(), null);
            // 判断是否存在未分账的（已关闭默认是已经分账）
            if (SubAccountConvertor.checkExistUnSubAccount(billDOList)) {
                orderSubAccountDO.setStatus(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_SOME);
            } else {
                orderSubAccountDO.setStatus(ORDER_SUB_ACCOUNT_STATUS_ALL);
            }
            orderSubAccountCmdExe.update(orderSubAccountDO);
            return Response.buildSuccess();
        }

        orderSubAccountDO.setUpdateTime(date);
        orderSubAccountCmdExe.update(orderSubAccountDO);
        // 组装参数、进行分账
        LOGGER.info("组装参数、进行分账");
        LOGGER.info("分账 orderSubAccountDO:{}", JSON.toJSONString(orderSubAccountDO));
        LOGGER.info("分账流水 subAccountBillDOList:{}", JSON.toJSONString(subAccountBillDOList));
        subAccountToWx(subAccountBillDOList, orderSubAccountDO);
        return Response.buildSuccess();
    }

    /**
     * 重新分账
     * 
     * @param id
     *            分账记录id 并非明细id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response subAccountAgain(Integer id) {
        OrderSubAccountDO orderSubAccountDO;
        try {
            // 判断订单是否分账完毕
            orderSubAccountDO = subAccountValidator.validOrderSubStatus(id);
        } catch (FinancialException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
        List<OrderSubAccountBillDO> doList = orderSubAccountBillQryExe.listByCondition(id, null);
        //以流水来覆盖分账
        BigDecimal maxSubAccountAmount = doList.stream().map(OrderSubAccountBillDO::getMaxSubAccountAmount)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        orderSubAccountDO.setMaxSubAccountAmount(maxSubAccountAmount);
        BigDecimal actualSubAccountAmount = doList.stream().map(OrderSubAccountBillDO::getActualSubAccountAmount)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        orderSubAccountDO.setActualSubAccountAmount(actualSubAccountAmount);
        List<OrderSubAccountBillDO> subAccountBillDOList = new ArrayList<>();
        Date date = new Date();
        for (OrderSubAccountBillDO accountBillDO : doList) {
            if (accountBillDO.getStatus().equals(ORDER_SUB_ACCOUNT_STATUS_ALL)
                || accountBillDO.getStatus().equals(ORDER_SUB_ACCOUNT_STATUS_CLOSE)) {
                continue;
            }
            if (accountBillDO.getMaxSubAccountAmount().compareTo(BigDecimal.ZERO) == 0
                || accountBillDO.getActualSubAccountAmount().compareTo(BigDecimal.ZERO) == 0) {
                // 金额为0、直接修改为成功 实际分账改为0
                accountBillDO.setSuccessFlag(OrderSubAccountConstant.ORDER_SUB_ACCOUNT_BILL_SUCCESS_FLAG_SUCCESS);
                accountBillDO.setStatus(ORDER_SUB_ACCOUNT_STATUS_ALL);
                accountBillDO.setActualSubAccountAmount(BigDecimal.ZERO);
                accountBillDO.setSuccessTime(date);
            } else {
                subAccountBillDOList.add(accountBillDO);
            }
        }
        orderSubAccountDO.setUpdateTime(date);
        orderSubAccountCmdExe.update(orderSubAccountDO);
        LOGGER.info("组装参数、进行分账");
        LOGGER.info("分账 orderSubAccountDO:{}", JSON.toJSONString(orderSubAccountDO));
        LOGGER.info("分账流水 subAccountBillDOList:{}", JSON.toJSONString(subAccountBillDOList));
        subAccountToWx(subAccountBillDOList, orderSubAccountDO);
        return Response.buildSuccess();
    }

    private void subAccountToWx(List<OrderSubAccountBillDO> accountBillDOList, OrderSubAccountDO orderSubAccountDO) {
        OrderSubAccountIdCmd orderSubAccountIdCmd = new OrderSubAccountIdCmd();
        orderSubAccountIdCmd.setId(orderSubAccountDO.getId());
        orderSubAccountIdCmd.setOrderId(orderSubAccountDO.getOrderId());
        List<PayBillsCustomerDO> payBillsCustomerDOList =
            payBillsCustomerQryExe.listByOnlineTradeNo(orderSubAccountDO.getTransactionId());
        orderSubAccountIdCmd.setAppId(payBillsCustomerDOList.get(0).getAppId());
        List<Integer> billIdList = accountBillDOList.stream().map(OrderSubAccountBillDO::getId).collect(toList());
        orderSubAccountIdCmd.setBillIdList(billIdList);
        Response response =
            wxPayPartnerServiceImpl.subAccountToWxPartner(orderSubAccountIdCmd, orderSubAccountDO, accountBillDOList);
        if (!response.isSuccess()) {
            LOGGER.error("手动分账失败、返回{}", JSON.toJSONString(response));
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    @Override
    public void wxSubAccountTimer() {
        // 找到到时间的还可以分账的列表
        List<OrderSubAccountDO> orderSubAccountDOList = orderSubAccountQryExe.listSubAccountUsable();
        if (orderSubAccountDOList == null || orderSubAccountDOList.size() == 0) {
            LOGGER.info("没有待分账的数据");
            return;
        }
        for (int x = 0; x < orderSubAccountDOList.size(); x++) {
            OrderSubAccountDO orderSubAccountDO = orderSubAccountDOList.get(x);
            // 判断订单状态
            com.bat.dubboapi.order.common.Response<OrderDistributorDataRpcDTO> response =
                orderDistributorDataServiceRpc.getByOrderIdAndDistributorId(orderSubAccountDO.getOrderId(),
                    orderSubAccountDO.getDistributorId());
            if (!response.isSuccess()) {
                throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
            }
            OrderDistributorDataRpcDTO orderDistributorDataRpcDTO = response.getData();

            if (OrderConstant.ORDER_STATUS_CANCEL.equals(orderDistributorDataRpcDTO.getOrderStatus())) {
                orderSubAccountDO.setStatus(ORDER_SUB_ACCOUNT_STATUS_CLOSE);
                orderSubAccountDO.setUpdateTime(new Date());
                orderSubAccountCmdExe.update(orderSubAccountDO);
                List<OrderSubAccountBillDO> accountBillDOList = orderSubAccountBillQryExe
                    .listByCondition(orderSubAccountDO.getId(), OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);
                if (accountBillDOList != null && accountBillDOList.size() > 0) {
                    accountBillDOList.stream().forEach(orderSubAccountBillDO -> {
                        orderSubAccountBillDO.setStatus(ORDER_SUB_ACCOUNT_STATUS_CLOSE);
                        orderSubAccountBillDO.setUpdateTime(new Date());
                    });
                    orderSubAccountBillCmdExe.batchUpdate(accountBillDOList);
                }
                continue;
            }

            List<OrderSubAccountBillDO> billDOList = orderSubAccountBillQryExe
                .listByCondition(orderSubAccountDO.getId(), OrderSubAccountConstant.ORDER_SUB_ACCOUNT_STATUS_UN);
            if (billDOList == null || billDOList.size() == 0) {
                LOGGER.info("订单id{}没有待分账的明细、不进行分账", orderSubAccountDO.getOrderId());
                continue;
            }
            try {
                subAccountToWx(billDOList, orderSubAccountDO);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("分账失败、错误不跑出{}", orderSubAccountDO.getOrderId());
            }
        }
        LOGGER.info("执行完分账线程、共处理{}条", orderSubAccountDOList.size());
    }
}
