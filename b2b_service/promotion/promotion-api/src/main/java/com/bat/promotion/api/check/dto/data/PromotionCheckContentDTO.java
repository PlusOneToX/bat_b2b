package com.bat.promotion.api.check.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批详情")
public class PromotionCheckContentDTO {

    @ApiModelProperty(value = "修改字段", example = "小铭")
    private String field;

    @ApiModelProperty(value = "修改前内容", example = "小铭")
    private String beforeContent;

    @ApiModelProperty(value = "修改后内容", example = "小铭")
    private String afterContent;

}
