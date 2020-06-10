package com.bat.flexible.dao.model.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelSimpleCO {

    @ApiModelProperty(value = "型号id")
    private Integer id;

    @ApiModelProperty(value = "型号名称",notes = "中文名称")
    private String name;

    @ApiModelProperty(value = "型号英文名称",notes = "英文")
    private String englishName;

    @ApiModelProperty(value = "型号类别中文名",notes = "中文")
    private String categoryName;

    @ApiModelProperty(value = "型号类别英文名称",notes = "英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "型号编码")
    private String modelNo;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否最终类型 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "产品分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "父id")
    private Integer parentId;
}
