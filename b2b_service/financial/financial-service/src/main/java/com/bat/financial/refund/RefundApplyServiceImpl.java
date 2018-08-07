package com.bat.financial.refund;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.bat.financial.refund.constant.ReceiverType;
import com.bat.financial.refund.executor.RefundApplyCmdExc;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.refund.RefundApplyService;
import com.bat.financial.api.refund.RefundService;
import com.bat.financial.api.refund.dto.RefundApplyManualConfirmCmd;
import com.bat.financial.api.refund.dto.RefundApplyQry;
import com.bat.financial.api.refund.dto.data.OrderRefundDTO;
import com.bat.financial.api.refund.dto.data.RefundApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundCustomerApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundDistributorApplyDTO;
import com.bat.financial.dao.refund.dataobject.RefundCustomerApplyDO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorApplyDO;
import com.bat.financial.refund.constant.RefundApplyStatus;
import com.bat.financial.refund.executor.RefundApplyQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:47
 */
@Service
@Slf4j
public class RefundApplyServiceImpl implements RefundApplyService {

    @Resource
    private RefundApplyQryExc refundApplyQryExc;

    @Resource
    private RefundApplyCmdExc refundApplyCmdExc;

    @Resource
    private RefundService refundService;

    @Resource
    private CommonService commonService;

    @Override
    public PageInfo<RefundDistributorApplyDTO> listDistributorRefundApply(RefundApplyQry qry) {
        return refundApplyQryExc.listDistributorRefundApply(qry);
    }

    @Override
    public PageInfo<RefundCustomerApplyDTO> listCustomerRefundApply(RefundApplyQry qry) {
        return refundApplyQryExc.listCustomerRefundApply(qry);
    }

    @Override
    public RefundApplyDTO listRefundApply(RefundApplyQry qry) {
        return refundApplyQryExc.listRefundApply(qry);
    }

    public RefundDistributorApplyDO getDistributorRefundApply(Integer id) {
        return refundApplyQryExc.getDistributorRefundApply(id);
    }

    public RefundCustomerApplyDO getCustomerRefundApply(Integer id) {
        return refundApplyQryExc.getCustomerRefundApply(id);
    }

    @Override
    public void manualConfirmRefundApply(RefundApplyManualConfirmCmd cmd) {
        if (cmd.getApplyStatus() != null && cmd.getApplyStatus().equals(RefundApplyStatus.CANCELLED)) {
            refundApplyCmdExc.manualConfirmRefundApply(cmd);
        } else {
            // 手动确认是 只能是 退回用户余额 其他退款方式 没有支付凭证号(注意：全平台唯一)
            OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
            orderRefundDTO.setReceiverType(cmd.getReceiverType());
            if (orderRefundDTO.getReceiverType().equals(ReceiverType.DISTRIBUTOR)) {
                RefundDistributorApplyDO distributorRefundApply = getDistributorRefundApply(cmd.getId());
                BeanUtils.copyProperties(distributorRefundApply, orderRefundDTO);
                DistributorRpcDTO distributorInfo = commonService.getDistributorInfo(orderRefundDTO.getDistributorId());
                orderRefundDTO.setCompanyName(distributorInfo.getCompanyName());
                orderRefundDTO.setOrderId(distributorRefundApply.getBusinessId());
                orderRefundDTO.setRefundType(cmd.getRefundType());
                orderRefundDTO.setRemark(cmd.getRemark());
            } else if (orderRefundDTO.getReceiverType().equals(ReceiverType.CUSTOMER)) {
                RefundCustomerApplyDO customerRefundApply = getCustomerRefundApply(cmd.getId());
                BeanUtils.copyProperties(customerRefundApply, orderRefundDTO);
                orderRefundDTO.setOrderId(customerRefundApply.getBusinessId());
                orderRefundDTO.setRefundType(cmd.getRefundType());
                orderRefundDTO.setRemark(cmd.getRemark());
            }
            orderRefundDTO.setDepositAmount(orderRefundDTO.getDepositAmount().add(orderRefundDTO.getCashAmount()));
            orderRefundDTO.setCashAmount(BigDecimal.ZERO);
            orderRefundDTO.setOperatorId(cmd.getOperatorId());
            orderRefundDTO.setOperatorName(cmd.getOperatorName());
            orderRefundDTO.setRemark(cmd.getRemark());
            refundService.updateRefund(orderRefundDTO, cmd.getId());
        }
    }
    // @Override
    // public void createRefundApply(RefundApplyCreateCmd cmd) {
    // refundApplyCmdExc.createRefundApply(cmd);
    // }
    //
    // @Override
    // public void updateRefundApply(RefundApplyUpdateCmd cmd) {
    // refundApplyCmdExc.updateRefundApply(cmd);
    // }
    //
    // @Override
    // public void deleteRefundApply(Integer id) {
    // refundApplyCmdExc.deleteRefundApply(id);
    // }
}
