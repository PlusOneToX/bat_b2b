package com.bat.promotion.api.rebatevoucher.dto;

import com.bat.promotion.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 19:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "返利代金券使用记录列表查询")
public class RebateVoucherUsageRecordListQry extends BaseSearchQry {
    public RebateVoucherUsageRecordListQry() {}

    // @NotNull(message = "B_PROMOTION_REBATE_VOUCHER_ID_NULL")
    @ApiModelProperty(value = "代金券id", required = false, example = "1")
    private Integer rebateVoucherId;

    @ApiModelProperty(value = "订单id", required = false, example = "1")
    private Integer orderId;
}
