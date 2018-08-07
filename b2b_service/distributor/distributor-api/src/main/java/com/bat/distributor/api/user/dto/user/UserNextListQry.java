package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserNextDistributorListQry", description = "查询下架分销商列表")
public class UserNextListQry {
    @ApiModelProperty(value = "查询内容，分销商名称/手机号", required = false, example = "bat")
    private String content;
    @NotNull(message = "P_DISTRIBUTOR_USER_NEXT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "上级分销商id", required = true, example = "bat")
    private Integer distributorId;
    @NotNull(message = "P_DISTRIBUTOR_USER_APPLY_STATUS_NULL")
    @ApiModelProperty(value = "申请状态 0 全部 1申请中 2申请通过 3申请失败（不填也为全部）", required = false, example = "bat")
    private Short applyStatus;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
