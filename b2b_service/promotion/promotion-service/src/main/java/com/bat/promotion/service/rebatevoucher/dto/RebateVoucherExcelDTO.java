package com.bat.promotion.service.rebatevoucher.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/2/18 14:54
 */
@Data
public class RebateVoucherExcelDTO {
    @ExcelProperty("B2B编码")
    private Integer distributorId;

    @ExcelProperty("ERP分销商ID")
    private String erpDistributorNo;

    @ExcelProperty("* 返利代金券券名")
    private String name;

    @ExcelProperty("* 返利代金券面值")
    private BigDecimal faceValue;

    @ExcelProperty("* 有效期开始时间")
    private Date startTime;

    @ExcelProperty("* 有效期结束时间")
    private Date endTime;

    @ExcelProperty("备注")
    private String remark;
}
