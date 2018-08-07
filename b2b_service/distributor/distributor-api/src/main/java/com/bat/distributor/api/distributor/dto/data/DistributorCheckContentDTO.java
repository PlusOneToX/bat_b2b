package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商资料审批详情")
public class DistributorCheckContentDTO {

    @ApiModelProperty(value = "修改字段", example = "小bat2018")
    private String field;

    @ApiModelProperty(value = "修改前内容", example = "小bat2018")
    private String beforeContent;

    @ApiModelProperty(value = "修改后内容", example = "小bat2018")
    private String afterContent;

}
