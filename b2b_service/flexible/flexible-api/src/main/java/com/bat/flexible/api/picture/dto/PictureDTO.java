package com.bat.flexible.api.picture.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PictureDTO {

    @ApiModelProperty(value = "图片id")
    private Integer id;

    @ApiModelProperty(value = "编码，用于承接外部业务的专用")
    private String code;

    @ApiModelProperty(value = "名称")
    private String pictureName;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "原图")
    private String originImage;

    @ApiModelProperty(value = "版权费用")
    private BigDecimal copyrightCost;


}