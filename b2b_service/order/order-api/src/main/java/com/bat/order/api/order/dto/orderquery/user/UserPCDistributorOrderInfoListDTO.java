package com.bat.order.api.order.dto.orderquery.user;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 前台 订单列表 柔性订单列表
 * @date: 2018/6/21 16:25
 */
@Data
public class UserPCDistributorOrderInfoListDTO {
    /**
     * 公共参数
     */
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("下单时间")
    private Date createTime;
    @ApiModelProperty("收货人")
    private String userName;
    @ApiModelProperty("付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    private Short payWay;
    @ApiModelProperty("付款状态")
    private Short payStatus;
    @ApiModelProperty("订单状态")
    private Short orderStatus;
    @ApiModelProperty("物流状态")
    private Short deliverStatus;
    @ApiModelProperty("订单币种")
    private String currencyType;
    @ApiModelProperty("订单总额")
    private BigDecimal payAmount;
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货（已发货） 5已关闭 6已完成 7 待付款", example = "1")
    private Short frontOrderStatus;
    /**
     * 店铺订单参数 其中部分参数 需要通过dubbo接口获取
     * 
     * 
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

}
