package com.bat.warehouse.manager.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.VoucherLogDTO;
import com.bat.dubboapi.warehouse.stock.dto.ErpItemStockChangeCmd;
import com.bat.warehouse.mq.dto.CommonLogDTO;
import com.bat.warehouse.mq.dto.ErpItemStockChangeDTO;

/**
 * @author bat(b2b_bat @ 163.com)
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

    public static List<ErpItemStockChangeDTO>
        toErpItemStockChangeDTOList(List<ErpItemStockChangeCmd> stockBillDetailList, String stockBillType) {
        List<ErpItemStockChangeDTO> stockChangeDTOS = new ArrayList<>();
        stockBillDetailList.forEach(stockBillDetail -> {
            ErpItemStockChangeDTO stockChangeDTO = new ErpItemStockChangeDTO();
            BeanUtils.copyProperties(stockBillDetail, stockChangeDTO);
            stockChangeDTO.setStockBillType(stockBillType);
            stockChangeDTOS.add(stockChangeDTO);
        });
        return stockChangeDTOS;
    }
}
