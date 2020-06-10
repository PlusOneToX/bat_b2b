package com.bat.flexible.dao.picture.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class PictureCategoryPageCO implements Serializable {

    private static final long serialVersionUID = 136804626161768280L;
    @ApiModelProperty(value = "图片分类id、新增为空、修改必填")
    private Integer id;

    @ApiModelProperty(value = "父分类id、顶级分类填0")
    private Integer parentId;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "图片分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "分类图片")
    private String image;

    @ApiModelProperty(value = "是否最底级的分类 1、是、0否")
    private Short atLastTrademark;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;


    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "低级分类")
    private List<PictureCategoryPageCO> childrenList;
}
