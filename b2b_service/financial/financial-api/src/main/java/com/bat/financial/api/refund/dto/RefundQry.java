package com.bat.financial.api.refund.dto;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:56
 */
@Data
@ApiModel(value = "RefundQry", description = "退款列表查询")
public class RefundQry extends BaseSearchQry {
    public RefundQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
    }

    @NotNull(message = "P_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "退款单状态,1待确认,2已确认,3已取消", example = "1")
    private Short refundStatus;

    @ApiModelProperty(value = "退款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付", example = "1")
    private Short refundWay;

    @ApiModelProperty(value = "1.分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "退款凭证", example = "1")
    private String outRefundNo;

}
