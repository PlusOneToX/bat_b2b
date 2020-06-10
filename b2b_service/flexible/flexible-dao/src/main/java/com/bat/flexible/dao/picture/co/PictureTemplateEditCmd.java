package com.bat.flexible.dao.picture.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PictureTemplateEditCmd implements Serializable {

    @ApiModelProperty(value = "图片模板id")
    private Integer id;

    @ApiModelProperty(value = "图片id")
    private Integer pictureId;

    @ApiModelProperty(value = "可编辑区域中心点X轴")
    private BigDecimal editCenterX;
    @ApiModelProperty(value = "可编辑区域中心点Y轴")
    private BigDecimal editCenterY;
    @ApiModelProperty(value = "可编辑区域长度（毫米）")
    private BigDecimal length;
    @ApiModelProperty(value = "可编辑区域宽度（毫米）")
    private BigDecimal width;

    @ApiModelProperty(value = "内部编辑图url")
    private String internalEditUrl;


}
