package com.bat.order.api.order.dto.orderquery.common;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/6 16:27
 */
@Data
public class OrderCustomerDataDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("C端客户id")
    private Integer customerId;
    @ApiModelProperty("C端客户名称")
    private String customerName;
    @ApiModelProperty("店铺编码")
    private String shopCode;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("订单状态 1.待确认2.已确认 4.已取消 5.已完成")
    private Short orderStatus;
    @ApiModelProperty(value = "前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成", example = "1")
    private Short frontOrderStatus;
    @ApiModelProperty("付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款")
    private Short payStatus;
    @ApiModelProperty("付款时间")
    private Date payTime;
    @ApiModelProperty("付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    private Short payWay;
    @ApiModelProperty("订单备注（限制200个字符）")
    private String remark;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("归属分销商id")
    private Integer distributorId;
    @ApiModelProperty("归属分销商用户名(登录名)")
    private String distributorName;
    @ApiModelProperty("归属分销商公司名")
    private String companyName;

    @Override
    public OrderCustomerDataDTO clone() throws CloneNotSupportedException {
        return (OrderCustomerDataDTO)super.clone();
    }

}
