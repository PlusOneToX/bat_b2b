package com.bat.order.api.order.dto.orderquery.admin;

import com.bat.order.api.common.constant.OrderStatusEnum;
import com.bat.order.api.common.constant.PayStatusEnum;
import com.bat.order.api.common.constant.PayWayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: lim
 * @description: 订单列表 行数据 为了简便 变量冗余了
 * @date: 2018/6/21 16:25
 */
@Data
@ApiModel(value = "OrderInfoListBaseDTO", description = "一级分销订单 订单列表")
public class AdminOrderInfoListDTO {

    /**
     * 前端订单状态 经过计算 与查询时相对应
     */
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成 7 待付款 8 已拒绝 9出库中", example = "1")
    private Short frontOrderStatus;
    /**
     * 公共参数
     */
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("下单时间")
    private Date createTime;
    @ApiModelProperty("直属分销商id")
    private Integer distributorId;
    @ApiModelProperty("直属分销商名称")
    private String distributorName;
    @ApiModelProperty("下单分销商id")
    private Integer orderDistributorId;
    @ApiModelProperty("下单分销商名称")
    private String orderDistributorName;
    @ApiModelProperty("同步erp分销商id")
    private Integer syncErpDistributorId;
    @ApiModelProperty("同步erp分销商名称")
    private String syncErpDistributorName;
    @ApiModelProperty("支付币种")
    private String currencyType;
    @ApiModelProperty("下单汇率")
    private BigDecimal currentRates;
    @ApiModelProperty("收货人")
    private String userName;
    @ApiModelProperty("物流状态")
    private Short deliverStatus;
    @ApiModelProperty("订单状态")
    private Short orderStatus;
    @ApiModelProperty("支付状态")
    private Short payStatus;
    @ApiModelProperty("支付方式")
    private Short payWay;
    @ApiModelProperty("订单总额")
    private BigDecimal payAmount;

    /**
     * OrderInfoErpDTO
     */
    @ApiModelProperty("ERP订单号")
    private String orderErpNo;
    @ApiModelProperty("第三方编号")
    private String orderThirdpartyNo;
    /**
     * OrderInfoDistributorDTO
     */
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("收款人")
    private String payee;
    /**
     * OrderInfoCustomerDiyDTO
     */
    @ApiModelProperty("来源平台")
    private String userSource;
    @ApiModelProperty("来源平台名称")
    private String userSourceName;
    @ApiModelProperty("店铺编码")
    private String shopCode;
    @ApiModelProperty("店铺名称")
    private String shopName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("备注")
    private String remark;
    /**
     * OrderInfoExceptionDTO
     */
    @ApiModelProperty("异常原因")
    private String exceptionCauses;

    @ApiModelProperty("支付凭证号(注意：全平台唯一)")
    private String outTradeNo;

    @ApiModelProperty("订单状态(中文解析)")
    private String orderStatusStr;

    @ApiModelProperty("支付状态(中文解析)")
    private String payStatusStr;

    @ApiModelProperty("支付方式(中文解析)")
    private String payWayStr;

    public String getOrderStatusStr() {
        return OrderStatusEnum.statusStr(orderStatus);
    }

    public String getPayStatusStr() {
        return PayStatusEnum.statusStr(payStatus);
    }

    public String getPayWayStr() {
        return PayWayEnum.statusStr(payWay);
    }
}
