package com.bat.financial.refund;

import java.util.Date;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.RefundBillsCustomerMapper;
import com.bat.financial.dao.refund.RefundBillsDistributorMapper;
import com.bat.financial.dao.refund.dataobject.RefundBillsDistributorDO;
import com.bat.financial.refund.constant.ReceiverType;
import com.bat.financial.refund.executor.ErrorCode;
import com.bat.financial.refund.executor.RefundDistributorCmdExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.refund.api.FinancialRefundServiceRpc;
import com.bat.dubboapi.financial.refund.dto.data.ErpRefundBillDetailsDTO;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.OrderPayStatusCmd;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.dao.refund.dataobject.RefundBillsCustomerDO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.refund.constant.RefundBillsStatus;
import com.bat.financial.refund.executor.RefundDistributorQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 退款“单” 服务
 * @date: 2018/6/12 15:47
 */
@DubboService
@Slf4j
public class RefundDistributorServiceRpcImpl implements FinancialRefundServiceRpc {

    @Resource
    private RefundDistributorQryExc refundQryExc;

    @Resource
    private RefundDistributorCmdExc refundCmdExc;

    @Resource
    private RefundBillsCustomerMapper refundBillsCustomerMapper;

    @Resource
    private RefundBillsDistributorMapper refundBillsDistributorMapper;

    @DubboReference(check = false, timeout = 30000)
    private OrderServiceRpc orderServiceRpc;

    @Override
    public Response<RefundBillSyncDTO> listErpRefundBillById(Integer id) {
        RefundDistributorDO refundDO = refundQryExc.getRefundDO(id);
        RefundBillSyncDTO refundDistributor = null;
        try {
            refundDistributor = refundQryExc.getRefundDistributor(refundDO.getOutRefundNo());
            log.info("财务获取退款单数据：{}", refundDistributor);
            return Response.of(refundDistributor);
        } catch (FinancialException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_REFUND_ERROR, MessageUtils.get(ErrorCode.B_REFUND_ERROR));
        }
    }

    @Override
    public Response updateRefund(ErpRefundBillDetailsDTO erpRefundBillDetailsDTO) {
        try {
            log.info("同步退款单：{}", erpRefundBillDetailsDTO);
            RefundDistributorDO refundDO =
                refundQryExc.getRefundDO(Integer.valueOf(erpRefundBillDetailsDTO.getRefundId()));
            if (refundDO.getRefundStatus().equals(RefundBillsStatus.PROCESS)) {
                refundDO.setRefundErpNo(erpRefundBillDetailsDTO.getRefundBillNo());
                refundDO.setRefundStatus(RefundBillsStatus.SUCCESS);
                refundDO.setUpdateTime(new Date());
                refundCmdExc.updateRefund(refundDO);
                log.info("更新退款单：{} erp退款单号：{} 为已确认", erpRefundBillDetailsDTO.getRefundId(),
                    erpRefundBillDetailsDTO.getRefundBillNo());
                OrderPayStatusCmd cmd = new OrderPayStatusCmd();
                if (refundDO.getCustomerFlag().equals(CustomerFlag.IS_CUSTOMER)) {
                    cmd.setReceiverType(ReceiverType.CUSTOMER);
                    RefundBillsCustomerDO byOutRefundNo =
                        refundBillsCustomerMapper.getByOutRefundNo(refundDO.getOutRefundNo());
                    cmd.setCustomerId(byOutRefundNo.getCustomerId());
                } else {
                    cmd.setReceiverType(ReceiverType.DISTRIBUTOR);
                    RefundBillsDistributorDO byOutRefundNo =
                        refundBillsDistributorMapper.getByOutRefundNo(refundDO.getOutRefundNo());
                    cmd.setDistributorId(byOutRefundNo.getDistributorId());

                }
                String[] split = refundDO.getBusinessId().split(",");
                cmd.setRefundedAmount(refundDO.getAmount());
                cmd.setOrderId(Integer.valueOf(split[0]));
                cmd.setPayStatus((short)6);
                com.bat.dubboapi.order.common.Response response = orderServiceRpc.orderPayStatus(cmd);
                if (response.isSuccess()) {
                    return Response.buildSuccess();
                } else {
                    return Response.buildFailure(response.getErrCode(), response.getErrMessage());
                }
            } else {
                log.info("待更新的退款单 状态不处于 退款处理中，具体状态：{}", refundDO.getRefundStatus());
                return Response.buildFailure(ErrorCode.B_REFUND_STATUS_ERROR,
                    "待更新的退款单 状态不处于 退款处理中，具体状态:" + refundDO.getRefundStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_REFUND_ERROR, MessageUtils.get(ErrorCode.B_REFUND_ERROR));
        }
    }
}
