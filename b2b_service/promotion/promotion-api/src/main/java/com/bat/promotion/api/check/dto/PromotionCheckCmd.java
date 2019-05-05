package com.bat.promotion.api.check.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批")
public class PromotionCheckCmd {

    @NotNull(message = "P_DISTRIBUTOR_CHECK_ID_NULL")
    @ApiModelProperty(value = "审批单id ", required = true, example = "0")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_CHECK_STATUS_NULL")
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", required = true, example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "审批说明备注 ", required = false, example = "0")
    private String remark;
}
