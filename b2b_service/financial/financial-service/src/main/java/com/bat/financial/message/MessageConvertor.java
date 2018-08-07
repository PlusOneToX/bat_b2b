package com.bat.financial.message;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.RefundLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.VoucherLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.WithdrawApplyLogDTO;
import com.bat.financial.mq.dto.CommonLogDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/7/1 11:55
 */
public class MessageConvertor {

    public static CommonLogDTO toCommonLogDTOList(String businessModule, String businessFunction, Integer businessId,
        String operateSource, Integer operateId, String operator, String operateType, String operateDes,
        String operateData, Date time) {
        if (StringUtils.isBlank(operator)) {
            operator = "系统";
        }
        CommonLogDTO dto = new CommonLogDTO();
        dto.setBusinessModule(businessModule);
        dto.setBusinessFunction(businessFunction);
        dto.setBusinessId(businessId);
        dto.setOperateSource(operateSource);
        dto.setOperateId(operateId);
        dto.setOperator(operator);
        dto.setOperateType(operateType);
        dto.setOperateDes(operateDes);
        dto.setOperateData(operateData);
        dto.setOperateTime(time);
        return dto;
    }

    public static VoucherLogDTO toVoucherLogDTO(Integer voucherId, Integer orderId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        VoucherLogDTO voucherLogDTO = new VoucherLogDTO();
        voucherLogDTO.setVoucherId(voucherId);
        voucherLogDTO.setOrderId(orderId);
        voucherLogDTO.setOperateSource(operateSource);
        voucherLogDTO.setOperateId(operateId);
        voucherLogDTO.setOperator(operator);
        voucherLogDTO.setOperateType(operateType);
        voucherLogDTO.setOperateDes(operateDes);
        voucherLogDTO.setOperateData(operateData);
        voucherLogDTO.setOperateTime(time);
        return voucherLogDTO;
    }

    public static RefundLogDTO toRefundLogDTO(Integer refundId, Integer orderId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        RefundLogDTO refundLogDTO = new RefundLogDTO();
        refundLogDTO.setRefundId(refundId);
        refundLogDTO.setOrderId(orderId);
        refundLogDTO.setOperateSource(operateSource);
        refundLogDTO.setOperateId(operateId);
        refundLogDTO.setOperator(operator);
        refundLogDTO.setOperateType(operateType);
        refundLogDTO.setOperateDes(operateDes);
        refundLogDTO.setOperateData(operateData);
        refundLogDTO.setOperateTime(time);
        return refundLogDTO;
    }

    public static WithdrawApplyLogDTO toWithdrawApplyLogDTO(Integer withdrawApplyId, Integer distributorId,
        String operateSource, Integer operateId, String operator, String operateType, String operateDes,
        String operateData, Date time) {
        WithdrawApplyLogDTO voucherLogDTO = new WithdrawApplyLogDTO();
        voucherLogDTO.setWithdrawApplyId(withdrawApplyId);
        voucherLogDTO.setDistributorId(distributorId);
        voucherLogDTO.setOperateSource(operateSource);
        voucherLogDTO.setOperateId(operateId);
        voucherLogDTO.setOperator(operator);
        voucherLogDTO.setOperateType(operateType);
        voucherLogDTO.setOperateDes(operateDes);
        voucherLogDTO.setOperateData(operateData);
        voucherLogDTO.setOperateTime(time);
        return voucherLogDTO;
    }
}
