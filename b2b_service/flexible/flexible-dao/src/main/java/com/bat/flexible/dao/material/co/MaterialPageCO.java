package com.bat.flexible.dao.material.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class MaterialPageCO implements Serializable {

    private static final long serialVersionUID = -6139763683998105297L;
    @ApiModelProperty(value = "材质id")
    private Integer id;

    @ApiModelProperty(value = "材质父id、顶级材质父id为0")
    private Integer parentId;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "材质名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "产品分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "产品类型")
    private String categoryName;

    @ApiModelProperty(value = "产品类型英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "图片")
    private String image;


    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;


    @ApiModelProperty(value = "是否为最终材质 1、是 0、否")
    private Short atLastTrademark;


    @ApiModelProperty(value = "关联的货品80码")
    private String itemCode;



    @ApiModelProperty(value = "子材质列表")
    private List<MaterialPageCO> childrenList;

}
