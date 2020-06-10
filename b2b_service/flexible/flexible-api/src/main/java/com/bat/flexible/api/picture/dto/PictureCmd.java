package com.bat.flexible.api.picture.dto;

import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import java.util.List;

@Data
public class PictureCmd {

    @ApiModelProperty(value = "图片id主键",notes = "新增不要传、编辑必传")
    private Integer id;

    @ApiModelProperty(value = "图片编码")
    private String code;

    @ApiModelProperty(value = "图片名称")
    @NotBlank(message = "P_PICTURE_NAME_NULL")
    private String name;

    @ApiModelProperty(value = "图片英文名称")
    private String englishName;

    @ApiModelProperty(value = "图片类型",notes = "1、普通素材，2、IP素材，3、模板，4、贴图")
    @NotNull(message = "P_PICTURE_TYPE_NULL")
    private Short type;

    @ApiModelProperty(value = "图片分类id")
    @NotNull(message = "P_PICTURE_CATEGORY_NULL")
    private Integer categoryId;

    @ApiModelProperty(value = "原图")
    @NotBlank(message ="P_PICTURE_ORIGINAL_IMAGE_NULL")
    private String originImage;

    @ApiModelProperty(value = "缩略图")
    //@NotBlank(message ="P_PICTURE_THUMBNAIL_NULL")
    private String thumbnail;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    @NotNull(message = "P_PICTURE_OPEN_FLAG_NULL")
    private Short OpenFlag;

    @ApiModelProperty(value = "型号范围",notes = "1为全部可用，2为指定型号可用,3为指定类型可用")
    @NotNull(message = "P_PICTURE_MODEL_SCOPE_NULL")
    private Short modelScope;

    @ApiModelProperty(value = "分销商适用范围",notes = "1为全部，2为国内，3为国外，4 指定")
    @NotNull(message = "P_PICTURE_RESELLER_SCOPE_NULL")
    private Short resellerScope;

    @ApiModelProperty(value = "主题链接")
    private String themeUrl;

    @ApiModelProperty(value = "模板中心点x轴")
    private BigDecimal templateCenterX;

    @ApiModelProperty(value = "模板中心点Y轴")
    private BigDecimal templateCenterY;

    @ApiModelProperty(value = "版权费用、默认0")
    @NotNull(message = "P_PICTURE_COPYRIGHT_COST_NULL")
    @Min(value = 0L,message = "P_PICTURE_COPYRIGHT_COST_LESS_THEN_ZERO")
    private BigDecimal copyrightCost;

    @ApiModelProperty(value = "图片描述")
    private String description;


    @ApiModelProperty(value = "带背景色预览图")
    @NotBlank(message = "P_PICTURE_BACKGROUNDPREVIEWURL_NULL")
    private String backgroundPreviewUrl;

    @ApiModelProperty(value = "无相机空位预览图")
    @NotBlank(message = "P_PICTURE_NOCAMERAVACANCYPREVIEW_NULL")
    private String noCameraVacancyPreview;

    @ApiModelProperty(value = "关联材质id列表")
    @NotEmpty(message = "P_PICTURE_MATERIAL_ID_LIST_NULL")
    private List<Integer> materialIdList;

    @ApiModelProperty(value = "关联标签id列表")
    private List<Integer> labelIdList;

    @ApiModelProperty(value = "分销商不适用范围列表,分销商id列表")
    private List<Integer> distributorIdExcludeList;

    @ApiModelProperty(value = "分销商适用范围列表,分销商id列表、resellerScope为4时必传")
    private List<Integer> distributorIdApplyList;

    @ApiModelProperty(value = "型号适用范围列表,型号id列表、modelScope为2时必传")
    private List<Integer> modelIdApplyList;

    @ApiModelProperty(value = "产品适用类型id列表",notes = "类型id")
    private List<Integer> productCategoryIdList;

    @ApiModelProperty(value = "模板列表,类型为模板是必填")
    private List<PictureTemplateEditCmd> templateEditList;
}
