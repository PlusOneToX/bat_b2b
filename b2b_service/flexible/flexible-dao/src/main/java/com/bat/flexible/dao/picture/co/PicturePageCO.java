package com.bat.flexible.dao.picture.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
@ApiModel
public class PicturePageCO {

    @ApiModelProperty(value = "图片id")
    private Integer id;

    @ApiModelProperty(value = "图片编码")
    private String code;

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "图片分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "图片分类名称")
    private String categoryName;

    @ApiModelProperty(value = "图片分类名称英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "适用材质id、逗号隔开")
    private String materialIdS;

    @ApiModelProperty(value = "适用材质名称，逗号隔开")
    private String materialNameS;

}
