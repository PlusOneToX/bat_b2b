package com.bat.flexible.api.label.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelCmd {

    @ApiModelProperty(value = "标签id")
    private Integer id;

    @ApiModelProperty(value = "标签名称")
    @NotBlank(message = "L_LABEL_NAME_NULL")
    private String name;

    @ApiModelProperty(value = "标签模板文件路径")
    @NotBlank(message = "L_TEMPLATE_FILE_NULL")
    private String templateFile;

    @ApiModelProperty(value = "类型", notes = "1、普通标签，2、定制标签")
    @NotNull(message = "L_LABEL_TYPE_NULL")
    private Short type;

    @ApiModelProperty(value = "是否启用", notes = "1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "条形码位置横坐标值")
    @NotNull(message = "L_LABEL_BAR_POSITION_X_NULL")
    private BigDecimal barCodePositionX;

    @ApiModelProperty(value = "条形码位置纵坐标值")
    @NotNull(message = "L_LABEL_BAR_POSITION_Y_NULL")
    private BigDecimal barCodePositionY;

    @ApiModelProperty(value = "条形码位置长度值")
    @NotNull(message = "L_LABEL_BAR_WIDTH_NULL")
    private BigDecimal barCodePositionWidth;

    @ApiModelProperty(value = "条形码位置高度值")
    @NotNull(message = "L_LABEL_BAR_HEIGHT_NULL")
    private BigDecimal barCodePositionHeight;

    @ApiModelProperty(value = "产品名称位置X轴")
    private BigDecimal productNamePositionX;

    @ApiModelProperty(value = "产品名称位置Y轴")
    private BigDecimal productNamePositionY;

    @ApiModelProperty(value = "产品名称位置宽度")
    private BigDecimal productNamePositionWidth;

    @ApiModelProperty(value = "产品名称位置高度")
    private BigDecimal productNamePositionHeight;

    @ApiModelProperty(value = "产品名称位置字体信息")
    private Integer productNamePositionFont;

    @ApiModelProperty(value = "产品名称位置字体大小")
    private Integer productNamePositionFontSize;

    @ApiModelProperty(value = "产品型号位置X轴")
    private BigDecimal modelPositionX;

    @ApiModelProperty(value = "产品型号位置Y轴")
    private BigDecimal modelPositionY;

    @ApiModelProperty(value = "产品型号位置长度")
    private BigDecimal modelPositionWidth;

    @ApiModelProperty(value = "产品型号位置高度")
    private BigDecimal modelPositionHeight;

    @ApiModelProperty(value = "产品型号位置字体信息")
    private Integer modelPositionFont;

    @ApiModelProperty(value = "产品型号位置字体大小")
    private Integer modelPositionFontSize;

    @ApiModelProperty(value = "适用分销商范围", notes = "1为全部，2 为国内， 3为国外，4 为指定")
    private Short scope;

    @ApiModelProperty(value = "类别", notes = "10010")
    @NotNull(message = "COMMON_CATEGORY_NULL")
    private Integer categoryId;

    @ApiModelProperty(value = "英文名称位置X轴")
    private BigDecimal enNamePositionX;

    @ApiModelProperty(value = "英文名称位置Y轴")
    private BigDecimal enNamePositionY;

    @ApiModelProperty(value = "英文名称位置长度")
    private BigDecimal enNamePositionWidth;

    @ApiModelProperty(value = "英文名称位置高度")
    private BigDecimal enNamePositionHeight;

    @ApiModelProperty(value = "英文名称位置字体信息")
    private Integer enNamePositionFont;

    @ApiModelProperty(value = "英文名称位置字体大小")
    private Integer enNamePositionFontSize;

    @ApiModelProperty(value = "产品材质位置X轴")
    private BigDecimal stuffNamePositionX;

    @ApiModelProperty(value = "产品材质位置Y轴")
    private BigDecimal stuffNamePositionY;

    @ApiModelProperty(value = "产品材质位置长度")
    private BigDecimal stuffNamePositionWidth;

    @ApiModelProperty(value = "产品材质位置高度")
    private BigDecimal stuffNamePositionHeight;

    @ApiModelProperty(value = "产品材质位置字体信息")
    private Integer stuffNamePositionFont;

    @ApiModelProperty(value = "产品材质位置字体大小")
    private Integer stuffNamePositionFontSize;

    @ApiModelProperty(value = "英文材质位置X轴")
    private BigDecimal stuffEnNamePositionX;

    @ApiModelProperty(value = "英文材质位置Y轴")
    private BigDecimal stuffEnNamePositionY;

    @ApiModelProperty(value = "英文材质位置长度")
    private BigDecimal stuffEnNamePositionWidth;

    @ApiModelProperty(value = "英文材质位置高度")
    private BigDecimal stuffEnNamePositionHeight;

    @ApiModelProperty(value = "英文材质位置字体信息")
    private Integer stuffEnNamePositionFont;

    @ApiModelProperty(value = "英文材质位置字体大小")
    private Integer stuffEnNamePositionFontSize;

    @ApiModelProperty(value = "是否关联用户上传图片 1、是 0、否 定制标签必填")
    private Short relevanceUserUpload;

    @ApiModelProperty(value = "标签编码，多鸿工厂生产会用到")
    private String thirdSkuNo;

    @ApiModelProperty(value = "关联的图片id列表")
    @NotEmpty(message = "L_PICTURE_ID_LIST_NULL")
    private List<Integer> pictureIdList;

    @ApiModelProperty(value = "关联的分销商id列表")
    private List<Integer> distributorIdList;
}
