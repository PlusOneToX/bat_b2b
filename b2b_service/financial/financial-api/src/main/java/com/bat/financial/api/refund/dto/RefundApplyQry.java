package com.bat.financial.api.refund.dto;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 15:51
 */
@Data
@ApiModel(value = "RefundApplyQry", description = "退款申请列表查询")
public class RefundApplyQry extends BaseSearchQry {
    public RefundApplyQry() {
        super.attributeMapper.put((short)1, "setDistributorName");
        super.attributeMapper.put((short)2, "setOperatorName");
        super.attributeMapper.put((short)3, "setId");
        super.attributeMapper.put((short)4, "setCustomerName");
    }

    @NotNull(message = "P_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

    @ApiModelProperty(value = "处理状态 0 未处理 1 已处理 2 已取消", example = "0")
    private Short applyStatus;

    @ApiModelProperty(value = "退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式", example = "2")
    private Short refundType;

    @ApiModelProperty(value = "业务类型 1订单取消 2订单变更", example = "1")
    private Short businessTypes;

    @ApiModelProperty(value = "业务id", example = "1")
    private String businessId;

    @ApiModelProperty(value = "1.分销商名称", example = "众芯")
    private String distributorName;

    @ApiModelProperty(value = "2.操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "3.id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "4.用户名", example = "1")
    private String customerName;

}
