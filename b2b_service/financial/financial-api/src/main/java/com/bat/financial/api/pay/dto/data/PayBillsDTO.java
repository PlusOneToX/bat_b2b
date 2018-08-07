package com.bat.financial.api.pay.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/31 22:51
 */
@Data
public class PayBillsDTO {
    @ApiModelProperty(value = "")
    protected Integer id;

    @ApiModelProperty(value = "支付凭证号(注意：全平台唯一)")
    protected String outTradeNo;

    @ApiModelProperty(value = "支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    protected Short payType;

    @ApiModelProperty(value = "业务类型 1订单收款2在线充值收款")
    protected Short businessType;

    @ApiModelProperty(value = "业务单号(业务类型为1时为订单id)")
    protected String businessId;

    @ApiModelProperty(value = "订单状态: 0未支付， 1已支付，2已取消")
    protected Short payStatus;

    @ApiModelProperty(value = "支付金额")
    protected BigDecimal totalFee;

    @ApiModelProperty(value = "订单标题")
    protected String orderTitle;

    @ApiModelProperty(value = "订单描述")
    protected String orderDescribe;

    @ApiModelProperty(value = "商品id")
    protected String productId;

    @ApiModelProperty(value = "支付平台凭证号")
    protected String onlineTradeNo;

    @ApiModelProperty(value = "失效时间")
    protected Date expireTime;

    @ApiModelProperty(value = "支付成功时间")
    protected Date payTime;

    @ApiModelProperty(value = "创建时间")
    protected Date createTime;

    @ApiModelProperty(value = "更新时间")
    protected Date updateTime;

    @ApiModelProperty(
        value = "扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)")
    protected String payMethod;

    @ApiModelProperty(value = "扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)")
    protected Short tradeMode;

    @ApiModelProperty(value = "扩展：为了定位收款账户。2 自己收款(分销商自己收款) 收款人id 分销商id")
    protected Integer payeeId;

    @ApiModelProperty(value = "扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款) 平台收款时 组织id")
    protected Integer organizationId;

    @ApiModelProperty(value = "扩展：定位的收款账户appid 可能错误。目前支付以前端传的appid为主，此appid 为前端传的appid")
    protected String appId;
}
