package com.bat.promotion.api.check.dto;

import javax.validation.constraints.NotNull;

import com.bat.promotion.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批列表查询")
public class PromotionCheckListQry extends BasePage {

    @NotNull(message = "P_DISTRIBUTOR_CHECK_LABEL_TYPE_NULL")
    @ApiModelProperty(value = "标签类型：1,我发起的 2,待我审批 3,我审批的 ", required = true, example = "1")
    private Short labelType;
    @NotNull(message = "P_DISTRIBUTOR_CHECK_LABEL_TYPE_NULL")
    @ApiModelProperty(value = "活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券", required = true, example = "1")
    private Short promotionType;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "查询内容，活动名称/活动ID/发起人名称", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型，1 活动名称 2 活动ID 3 发起人名称 (查询内容不为空是必填) 4审批id", required = false, example = "bat")
    private Short contentType;
}
