package com.bat.order.api.data.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 15:42
 */
@Data
public class OrderDistributorDataDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("分销商id")
    private Integer distributorId;
    @ApiModelProperty("分销商名称")
    private String distributorName;
    @ApiModelProperty("下单分销商联系人id(联系人登录情况有值)")
    private Integer distributorContactId;
    @ApiModelProperty("下单分销商联系人名称(联系人登录情况有值)")
    private String distributorContactName;
    @ApiModelProperty("公司名")
    private String companyName;
    @ApiModelProperty("上级分销商id")
    private Integer distributorAncestorId;
    @ApiModelProperty("上级分销商用户名(登录名)")
    private String distributorAncestorName;
    @ApiModelProperty("上级分销商公司名")
    private String distributorAncestorCompanyName;
    @ApiModelProperty("是否直接下单 1.是认0.否")
    private Short directFlag;
    @ApiModelProperty("是否同步到erp 1.是 0.否")
    private Short erpFlag;
    @ApiModelProperty("多级分销级数")
    private Integer treeNode;
    @ApiModelProperty("订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成")
    private Short orderStatus;
    @ApiModelProperty("付款状态 1.未付款，2.部分付款 3. 已付款 4.部分退款 5.退款申请中 6.已退款")
    private Short payStatus;
    @ApiModelProperty("付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付")
    private Short payWay;
    @ApiModelProperty("分销模式： 1 平台方收款(比如：bat收款，bat收款，如果现款情况，二级以上分销商只支持现款线上支付，一级分销商可支持现款线下支付及线下银行转账), 2 自己收款(分销商自己收款)")
    private Short distributionMode;
    @ApiModelProperty("订单币种")
    private String currencyType;
    @ApiModelProperty("订单汇率")
    private BigDecimal currentRates;
    @ApiModelProperty("订单备注（限制200个字符）")
    private String remark;
    @ApiModelProperty("付款时间")
    private Date payTime;
    @ApiModelProperty("取消订单备注（限制200个字符）")
    private String cancelRemark;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Override
    public OrderDistributorDataDTO clone() throws CloneNotSupportedException {
        return (OrderDistributorDataDTO)super.clone();
    }
}
