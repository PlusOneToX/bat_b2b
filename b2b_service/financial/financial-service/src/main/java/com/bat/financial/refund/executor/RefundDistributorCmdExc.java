package com.bat.financial.refund.executor;

import java.util.Date;

import javax.annotation.Resource;

import com.bat.financial.message.MessageSendService;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.financial.dao.refund.RefundDistributorMapper;
import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:59
 */
@Component
@Slf4j
public class RefundDistributorCmdExc {
    @Resource
    private RefundDistributorMapper refundDistributorMapper;

    @Resource
    private MessageSendService sendService;

    public void createRefund(RefundDistributorDO aDo) {
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        log.info("refundDistributorMapper.insert:{}", JSON.toJSONString(aDo));
        refundDistributorMapper.insert(aDo);
        // sendService.refundLogPackage(aDo.getId(), null, "退款单生成", "生成成功", JSONObject.toJSONString(aDo));
    }

    public void updateRefund(RefundDistributorDO aDo) {
        refundDistributorMapper.updateByPrimaryKey(aDo);
        // sendService.refundLogPackage(aDo.getId(), null, "退款单更新", "更新成功", JSONObject.toJSONString(aDo));
    }

    // public void createRefund(RefundDistributorDO aDo) {
    // Date date = new Date();
    // aDo.setCreateTime(date);
    // aDo.setUpdateTime(date);
    // refundDistributorMapper.insert(aDo);
    // }

    // public void createRefund(RefundCreateCmd cmd) {
    // refundDistributorMapper.insert(RefundDistributorConvertor.toRefundDistributorDO(cmd));
    // }
    //
    // public void updateRefund(RefundUpdateCmd cmd) {
    // RefundDistributorDO refundDistributorDO = refundDistributorMapper.selectByPrimaryKey(cmd.getId());
    // if (refundDistributorDO == null) {
    // throw FinancialException.buildException(ErrorCode.B_REFUND_APPLY_ID_NOT_EXISTS);
    // }
    // refundDistributorMapper.updateByPrimaryKey(RefundDistributorConvertor.toRefundDistributorDO(cmd));
    // }
    //
    // public void deleteRefund(Integer id) {
    // RefundDistributorDO refundDistributorDO = refundDistributorMapper.selectByPrimaryKey(id);
    // if (refundDistributorDO == null) {
    // throw FinancialException.buildException(ErrorCode.B_REFUND_APPLY_ID_NOT_EXISTS);
    // }
    // refundDistributorMapper.deleteByPrimaryKey(id);
    // }
}
