package com.bat.financial.refund.executor;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.financial.api.refund.dto.RefundApplyManualConfirmCmd;
import com.bat.financial.dao.refund.RefundCustomerApplyMapper;
import com.bat.financial.dao.refund.RefundDistributorApplyMapper;
import com.bat.financial.dao.refund.dataobject.RefundBaseApplyDO;
import com.bat.financial.dao.refund.dataobject.RefundCustomerApplyDO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorApplyDO;
import com.bat.financial.refund.constant.ReceiverType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:59
 */
@Component
@Slf4j
public class RefundApplyCmdExc {

    @Resource
    private RefundDistributorApplyMapper refundDistributorApplyMapper;

    @Resource
    private RefundCustomerApplyMapper refundCustomerApplyMapper;

    // public void createRefundApply(RefundApplyCreateCmd cmd) {
    // refundDistributorApplyMapper.insert(RefundDistributorApplyConvertor.toRefundDistributorApplyDO(cmd));
    // }

    public void createOrUpdateRefundApply(RefundBaseApplyDO baseApplyDO, Short receiverType, Integer id, String name,
        Integer refundApplyId) {
        Date date = new Date();
        if (receiverType.equals(ReceiverType.DISTRIBUTOR)) {
            RefundDistributorApplyDO applyDO = refundDistributorApplyMapper.selectByPrimaryKey(refundApplyId);
            if (applyDO == null) {
                applyDO = new RefundDistributorApplyDO();
                BeanUtils.copyProperties(baseApplyDO, applyDO);
                applyDO.setDistributorId(id);
                applyDO.setDistributorName(name);
                applyDO.setCreateTime(date);
                applyDO.setUpdateTime(date);
                log.info("refundDistributorApplyMapper.insert:{}", JSON.toJSONString(applyDO));
                refundDistributorApplyMapper.insert(applyDO);
            } else {
                applyDO.setUpdateTime(new Date());
                applyDO.setOperatorId(baseApplyDO.getOperatorId());
                applyDO.setOperatorName(baseApplyDO.getOperatorName());
                applyDO.setApplyStatus(baseApplyDO.getApplyStatus());
                applyDO.setRefundType(baseApplyDO.getRefundType());
                applyDO.setRemark(baseApplyDO.getRemark());
                log.info("refundDistributorApplyMapper.updateByPrimaryKey:{}", JSON.toJSONString(applyDO));
                refundDistributorApplyMapper.updateByPrimaryKey(applyDO);
            }
        } else if (receiverType.equals(ReceiverType.CUSTOMER)) {
            RefundCustomerApplyDO applyDO = refundCustomerApplyMapper.selectByPrimaryKey(refundApplyId);
            if (applyDO == null) {
                applyDO = new RefundCustomerApplyDO();
                BeanUtils.copyProperties(baseApplyDO, applyDO);
                applyDO.setCustomerId(id);
                applyDO.setCustomerName(name);
                applyDO.setCreateTime(date);
                applyDO.setUpdateTime(date);
                log.info("refundCustomerApplyMapper.insert:{}", JSON.toJSONString(applyDO));
                refundCustomerApplyMapper.insert(applyDO);
            } else {
                applyDO.setUpdateTime(new Date());
                applyDO.setOperatorId(baseApplyDO.getOperatorId());
                applyDO.setOperatorName(baseApplyDO.getOperatorName());
                applyDO.setApplyStatus(baseApplyDO.getApplyStatus());
                applyDO.setRefundType(baseApplyDO.getRefundType());
                applyDO.setRemark(baseApplyDO.getRemark());
                log.info("refundCustomerApplyMapper.updateByPrimaryKey:{}", JSON.toJSONString(applyDO));
                refundCustomerApplyMapper.updateByPrimaryKey(applyDO);
            }

        }
    }

    public void manualConfirmRefundApply(RefundApplyManualConfirmCmd cmd) {
        if (cmd.getReceiverType().equals(ReceiverType.DISTRIBUTOR)) {
            RefundDistributorApplyDO refundDistributorApplyDO =
                refundDistributorApplyMapper.selectByPrimaryKey(cmd.getId());
            refundDistributorApplyDO.setApplyStatus(cmd.getApplyStatus());
            refundDistributorApplyDO.setUpdateTime(new Date());
            refundDistributorApplyDO.setOperatorId(cmd.getOperatorId());
            refundDistributorApplyDO.setOperatorName(cmd.getOperatorName());
            refundDistributorApplyMapper.updateByPrimaryKey(refundDistributorApplyDO);
        } else if (cmd.getReceiverType().equals(ReceiverType.CUSTOMER)) {
            RefundCustomerApplyDO refundCustomerApplyDO = refundCustomerApplyMapper.selectByPrimaryKey(cmd.getId());
            refundCustomerApplyDO.setApplyStatus(cmd.getApplyStatus());
            refundCustomerApplyDO.setUpdateTime(new Date());
            refundCustomerApplyDO.setOperatorId(cmd.getOperatorId());
            refundCustomerApplyDO.setOperatorName(cmd.getOperatorName());
            refundCustomerApplyMapper.updateByPrimaryKey(refundCustomerApplyDO);
        }
    }

    // public void updateRefundApply(RefundApplyUpdateCmd cmd) {
    // RefundDistributorApplyDO refundDistributorApplyDO =
    // refundDistributorApplyMapper.selectByPrimaryKey(cmd.getId());
    // if (refundDistributorApplyDO == null) {
    // throw FinancialException.buildException(ErrorCode.B_REFUND_APPLY_ID_NOT_EXISTS);
    // }
    // RefundDistributorApplyDO refundDistributorApplyDO1 =
    // RefundDistributorApplyConvertor.toRefundDistributorApplyDO(cmd);
    // refundDistributorApplyDO1.setCreateTime(refundDistributorApplyDO.getCreateTime());
    // refundDistributorApplyMapper.updateByPrimaryKey(refundDistributorApplyDO1);
    // }
    //
    // public void deleteRefundApply(Integer id) {
    // RefundDistributorApplyDO refundDistributorApplyDO = refundDistributorApplyMapper.selectByPrimaryKey(id);
    // if (refundDistributorApplyDO == null) {
    // throw FinancialException.buildException(ErrorCode.B_REFUND_APPLY_ID_NOT_EXISTS);
    // }
    // refundDistributorApplyMapper.deleteByPrimaryKey(id);
    // }
}
