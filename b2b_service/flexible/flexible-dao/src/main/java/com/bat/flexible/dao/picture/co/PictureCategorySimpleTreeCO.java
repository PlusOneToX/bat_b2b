package com.bat.flexible.dao.picture.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class PictureCategorySimpleTreeCO implements Serializable {

    private static final long serialVersionUID = -3351834340338316631L;
    @ApiModelProperty(value = "图片分类id")
    private Integer id;

    @ApiModelProperty(value = "图片分类名称")
    private String name;

    @ApiModelProperty(value = "图片分类英文")
    private String englishName;

    @ApiModelProperty(value = "图片分类父级id")
    private Integer parentId;

    @ApiModelProperty(value = "图片分类图片")
    private String image;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "是否最终分类 1、是 0、否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "图片列表")
    private List<PictureCO> pictureList;

    @ApiModelProperty(value = "图片分类子级列表")
    private List<PictureCategorySimpleTreeCO> childrenList;
}
