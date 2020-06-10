package com.bat.flexible.api.picture.dto;

import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.label.dto.LabelRelaSimpleDTO;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.api.product.dto.ProductCategoryRelaDTO;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PictureQry {

    @ApiModelProperty(value = "图片id主键",notes = "新增不要传、编辑必传")
    private Integer id;

    @ApiModelProperty(value = "图片编码")
    private String code;

    @ApiModelProperty(value = "图片名称")
    private String name;

    @ApiModelProperty(value = "图片英文名称")
    private String englishName;

    @ApiModelProperty(value = "图片类型",notes = "1、普通素材，2、IP素材，3、模板，4、贴图")
    private Short type;

    @ApiModelProperty(value = "图片分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "原图")
    private String originImage;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short OpenFlag;

    @ApiModelProperty(value = "型号范围",notes = "1为全部可用，2为指定型号可用,3为指定类型可用")
    private Short modelScope;

    @ApiModelProperty(value = "分销商适用范围",notes = "1为全部，2为国内，3为国外，4 指定")
    private Short resellerScope;

    @ApiModelProperty(value = "主题链接")
    private String themeUrl;

    @ApiModelProperty(value = "模板中心点x轴")
    private BigDecimal templateCenterX;

    @ApiModelProperty(value = "模板中心点Y轴")
    private BigDecimal templateCenterY;

    @ApiModelProperty(value = "版权费用、默认0")
    private BigDecimal copyrightCost;

    @ApiModelProperty(value = "图片描述")
    private String description;

    @ApiModelProperty(value = "带背景色预览图")
    private String backgroundPreviewUrl;

    @ApiModelProperty(value = "无相机空位预览图")
    private String noCameraVacancyPreview;

    @ApiModelProperty(value = "关联材质列表")
    private List<MaterialRelaSimpleDTO> materialRelaList;

    @ApiModelProperty(value = "关联标签列表")
    private List<LabelRelaSimpleDTO> labelRelaList;

    @ApiModelProperty(value = "分销商不适用范围列表",notes = "分销商列表")
    private List<DistributorSimpleRelaQry> distributorExcludeList;

    @ApiModelProperty(value = "分销商适用范围列表",notes = "分销商列表、resellerScope为4时必传")
    private List<DistributorSimpleRelaQry> distributorApplyList;

    @ApiModelProperty(value = "型号适用范围列表",notes = "型号id列表、modelScope为2时必传")
    private List<ModelRelaSimpleDTO> modelApplyList;

    @ApiModelProperty(value = "型号适用类型列表",notes = "类型id")
    private List<ProductCategoryRelaDTO> productCategoryRelaList;

    @ApiModelProperty(value = "模板列表",notes = "类型为模板是必填")
    private List<PictureTemplateEditCmd> templateEditList;
}
