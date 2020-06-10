package com.bat.flexible.dao.third.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ThirdSkuRelevancePageCO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "产品适用类型id")
    private Integer  categoryId;

    @ApiModelProperty(value = "产品适用类型")
    private String categoryName;

    @ApiModelProperty(value = "材质分类")
    private String materialCategoryName;

    @ApiModelProperty(value = "材质分类编码")
    private String  materialCategoryNo;

    @ApiModelProperty(value = "父分类材质名称")
    private String  parentMaterialName;

    @ApiModelProperty(value = "材质名称")
    private String materialName;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "第三方材质名称")
    private String thirdMaterialName;

    @ApiModelProperty(value = "第三方材质编码")
    private String thirdMaterialNo;

    @ApiModelProperty(value = "第三方颜色编码")
    private String colourNo;

    @ApiModelProperty(value = "第三方颜色名称")
    private String colourName;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "型号编码")
    private String modelNo;

    @ApiModelProperty(value = "第三方型号编码")
    private String thirdModelNo;

    @ApiModelProperty(value = "第三方型号名称")
    private String thirdModelName;

    @ApiModelProperty
    private String series;

    @ApiModelProperty(value = "第三方品牌编码")
    private String thirdBrandNo;

    @ApiModelProperty(value = "第三方品牌名称")
    private String thirdBrandName;

    @ApiModelProperty(value = "第三方sku编码")
    private  String thirdSkuNo;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private  Short openFlag;

    @ApiModelProperty(value = "父型号名称")
    private String parentModelName;
}
