package com.bat.financial.api.export.dto;

import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/4 15:24
 */
@Data
public class ReconciliationExportDTO {
    @ExcelProperty("商户订单号")
    private String outTradeNo;
    @ExcelProperty("发生时间")
    private String payTime;
    @ExcelProperty("收款金额")
    private String payAmount;
    @ExcelProperty("退款金额")
    private String refundAmount;
    @ExcelProperty("B2B订单")
    private String b2bOrderNo;
    @ExcelProperty("ERP订单")
    private String erpOrderNo;
    @ExcelIgnore
    private List<Integer> orderIds;
}
