package com.bat.order.api.order.dto.orderquery.admin;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 沙漠
 */
@Data
@ApiModel(value = "AdminOrderInfoListExcelDTO", description = "一级分销订单 订单列表")
public class AdminOrderInfoListExcelDTO {
    /**
     * 公共参数
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("id")
    private Integer id;

    @ExcelProperty(value = "订单编号")
    @ApiModelProperty("订单编号")
    private String orderNo;

    @ExcelProperty(value = "下单时间")
    @ApiModelProperty("下单时间")
    private Date createTime;

    @ExcelProperty(value = "直属分销商id")
    @ApiModelProperty("直属分销商id")
    private Integer distributorId;

    @ExcelProperty(value = "直属分销商名称")
    @ApiModelProperty("直属分销商名称")
    private String distributorName;

    @ExcelProperty(value = "支付币种")
    @ApiModelProperty("支付币种")
    private String currencyType;

    @ExcelProperty(value = "下单汇率")
    @ApiModelProperty("下单汇率")
    private BigDecimal currentRates;

    @ExcelProperty(value = "订单状态")
    @ApiModelProperty("订单状态(中文解析)")
    private String orderStatusStr;

    @ExcelProperty(value = "支付状态")
    @ApiModelProperty("支付状态(中文解析)")
    private String payStatusStr;

    @ExcelProperty(value = "支付方式")
    @ApiModelProperty("支付方式(中文解析)")
    private String payWayStr;

    @ExcelProperty(value = "订单总额")
    @ApiModelProperty("订单总额")
    private BigDecimal payAmount;

    @ExcelProperty(value = "异常原因")
    @ApiModelProperty("异常原因")
    private String exceptionCauses;

    @ExcelProperty(value = "支付凭证号(注意：全平台唯一)")
    @ApiModelProperty("支付凭证号(注意：全平台唯一)")
    private String outTradeNo;

}
