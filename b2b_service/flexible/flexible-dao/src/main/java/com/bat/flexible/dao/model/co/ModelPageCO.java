package com.bat.flexible.dao.model.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel
public class ModelPageCO implements Serializable {

    private static final long serialVersionUID = 8869305597654334449L;
    @ApiModelProperty(value = "型号id")
    private Integer id;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "英文")
    private String englishName;

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "产品类型")
    private String categoryName;

    @ApiModelProperty(value = "产品类型英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "父节点名称")
    private String parentName;

    @ApiModelProperty(value = "父节点英文")
    private String parentEnglishName;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否为品牌 1、是 0、否")
    private Short atLastTrademark;
    
    @ApiModelProperty(value = "型号编码")
    private String modelNo;


    @ApiModelProperty(value = "查询条件")
    private String content;


    @ApiModelProperty(value = "型号列表")
    private List<ModelPageCO> childrenList;
}
