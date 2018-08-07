package com.bat.order.api.order.dto.orderquery.user;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: lim
 * @description: 前台 订单列表 柔性订单列表
 * @date: 2018/6/21 16:25
 */
@Data
public class UserPCDistributorOrderInfoExportDTO {
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

    @ExcelProperty(value = "收货人")
    @ApiModelProperty("收货人")
    private String userName;

    @ExcelProperty(value = "付款方式")
    @ApiModelProperty("付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    private String payWay;

    @ExcelProperty(value = "订单币种")
    @ApiModelProperty("订单币种")
    private String currencyType;

    @ExcelProperty(value = "订单总额")
    @ApiModelProperty("订单总额")
    private BigDecimal payAmount;

    @ExcelProperty(value = "订单状态")
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货（已发货） 5已关闭 6已完成 7 待付款", example = "1")
    private String frontOrderStatus;

    @ExcelProperty(value = "付款状态")
    @ApiModelProperty("付款状态")
    private String payStatus;
    /**
     * 店铺订单参数 其中部分参数 需要通过dubbo接口获取
     * 
     * 
     * OrderInfoCustomerDiyDTO
     */
    @ExcelProperty(value = "来源平台")
    @ApiModelProperty("来源平台")
    private String userSource;

    @ExcelProperty(value = "来源平台名称")
    @ApiModelProperty("来源平台名称")
    private String userSourceName;

    @ExcelProperty(value = "店铺编码")
    @ApiModelProperty("店铺编码")
    private String shopCode;

    @ExcelProperty(value = "店铺名称")
    @ApiModelProperty("店铺名称")
    private String shopName;

    @ExcelProperty(value = "手机号")
    @ApiModelProperty("手机号")
    private String mobile;

    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;

}
