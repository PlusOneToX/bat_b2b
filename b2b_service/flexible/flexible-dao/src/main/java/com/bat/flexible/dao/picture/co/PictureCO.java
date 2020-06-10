package com.bat.flexible.dao.picture.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel
public class PictureCO {

    @ApiModelProperty(value = "图片id")
    private Integer id;

    @ApiModelProperty(value = "原图")
    private String originImage;

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "图片英文")
    private String englishName;

    @ApiModelProperty(value = "图片产品原型图")
    private String thumbnail;

    @ApiModelProperty(value = "图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图")
    private Short type;


    @ApiModelProperty(value = "模板中心点x轴")
    private BigDecimal templateCenterX;

    @ApiModelProperty(value = "模板中心点y轴")
    private BigDecimal templateCenterY;

    @ApiModelProperty(value = "图片版权费用")
    private BigDecimal copyrightCost;

    @ApiModelProperty(value = "模板列表")
    private List<PictureTemplateEditCmd> templateEditList;

}
