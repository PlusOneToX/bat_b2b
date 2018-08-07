package com.bat.distributor.api.group.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GroupAddCmd", description = "分销商分组信息")
public class GroupCmd {

    @ApiModelProperty(value = "分销商分组ID", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_GROUP_NAME_NULL")
    @ApiModelProperty(value = "分销商分组名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商分组描述", required = false, example = "bat")
    private String description;
    @ApiModelProperty(value = "ERP分销商分组编号", required = false, example = "bat")
    private String erpGroupNo;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
