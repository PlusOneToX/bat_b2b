package com.bat.thirdparty.message.service;

import java.util.Date;

import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.*;
import com.bat.thirdparty.log.api.dto.CommonLogDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 沙漠
 */
public class MessageConvertor {

    public static CommonLogDTO toCommonLogDTOList(String businessModule, String businessFunction, Integer businessId,
        String operateSource, Integer operateId, String operator, String operateType, String operateDes,
        String operateData, Date time) {
        if(StringUtils.isBlank(operator)){
            operator="系统";
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

    public static OrderDeliverBillLogDTO toOrderDeliverBillLogDTO(Integer orderDeliverBillId, Integer orderId, String operateSource, Integer operateId,
                                                String operator, String operateType, String operateDes, String operateData, Date time) {
        OrderDeliverBillLogDTO orderDeliverBillLogDTO = new OrderDeliverBillLogDTO();
        orderDeliverBillLogDTO.setOrderDeliverBillId(orderDeliverBillId);
        orderDeliverBillLogDTO.setOrderId(orderId);
        orderDeliverBillLogDTO.setOperateSource(operateSource);
        orderDeliverBillLogDTO.setOperateId(operateId);
        orderDeliverBillLogDTO.setOperator(operator);
        orderDeliverBillLogDTO.setOperateType(operateType);
        orderDeliverBillLogDTO.setOperateDes(operateDes);
        orderDeliverBillLogDTO.setOperateData(operateData);
        orderDeliverBillLogDTO.setOperateTime(time);
        return orderDeliverBillLogDTO;
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

    public static RefundLogDTO toRefundLogDTO(Integer refundId, Integer orderId, String operateSource, Integer operateId,
                                              String operator, String operateType, String operateDes, String operateData, Date time) {
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

    public static DistributorErpRpcCmd toDistributorErpRpcCmd(Integer distributorId, Integer erpId, String erpNo) {
        DistributorErpRpcCmd erpRpcCmd = new DistributorErpRpcCmd();
        erpRpcCmd.setDistributorId(distributorId);
        erpRpcCmd.setErpId(erpId);
        erpRpcCmd.setErpNo(erpNo);
        return erpRpcCmd;
    }

    public static List<OrderLogDTO> toOrderLogDTOList(List<Integer> orderIds, String operateSource, Integer operateId,
                                                      String operator, String operateType, String operateDes, String operateData, Date time) {
        List<OrderLogDTO> dtos = new ArrayList<>();
        orderIds.forEach(orderId -> {
            OrderLogDTO orderLogDTO = new OrderLogDTO();
            orderLogDTO.setOrderId(orderId);
            orderLogDTO.setOperateSource(operateSource);
            orderLogDTO.setOperateId(operateId);
            orderLogDTO.setOperator(operator);
            orderLogDTO.setOperateType(operateType);
            orderLogDTO.setOperateDes(operateDes);
            orderLogDTO.setOperateData(operateData);
            orderLogDTO.setOperateTime(time);
            dtos.add(orderLogDTO);
        });
        return dtos;
    }

    public static DistributorLogDTO toDistributorLogDTO(Integer distributorId, String operateSource, Integer operateId,
                                                        String operator, String operateType, String operateDes, String operateData, Date time) {
        DistributorLogDTO distributorLogDTO = new DistributorLogDTO();
        distributorLogDTO.setDistributorId(distributorId);
        distributorLogDTO.setOperateSource(operateSource);
        distributorLogDTO.setOperateId(operateId);
        distributorLogDTO.setOperator(operator);
        distributorLogDTO.setOperateType(operateType);
        distributorLogDTO.setOperateDes(operateDes);
        distributorLogDTO.setOperateData(operateData);
        distributorLogDTO.setOperateTime(time);
        return distributorLogDTO;
    }

}
