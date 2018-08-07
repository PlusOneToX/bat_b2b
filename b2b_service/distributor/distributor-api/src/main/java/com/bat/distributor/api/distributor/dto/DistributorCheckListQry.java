package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import com.bat.distributor.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商资料审批列表查询")
public class DistributorCheckListQry extends BasePage {

    @NotNull(message = "P_DISTRIBUTOR_CHECK_LABEL_TYPE_NULL")
    @ApiModelProperty(value = "标签类型：1,我发起的 2,待我审批 3,我审批的 ", required = true, example = "1")
    private Short labelType;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "查询内容，分销商用户名/客户名/分销商ID/分销商ERP编号/发起人名称", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型，1 分销商用户名 2 客户名 3 分销商ID 4 分销商ERP编号 5 发起人名称 (查询内容不为空是必填)", required = false,
        example = "bat")
    private Short contentType;
}
