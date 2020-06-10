package com.bat.flexible.dao.label.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelRelaQry {

    @ApiModelProperty(value = "关联的id主键",notes = "不是标签id")
    private Integer id;


    @ApiModelProperty(value="标签id")
    private Integer labelId;

    @ApiModelProperty(value = "标签名称")
    private String labelName;

    @ApiModelProperty(value = "标签类型",notes = "1、定制标签")
    private Short labelType;

    @ApiModelProperty(value = "标签状态",notes = "1、启用 0、禁用")
    private Short openFlag;

}
