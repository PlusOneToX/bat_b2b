package com.bat.flexible.api.material.dto;


import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class MaterialSaveCmd {

    @ApiModelProperty(value = "材质id",name = "id")
    private Integer id;

    @ApiModelProperty(value = "材质编码",name = "materialNo")
    private String materialNo;


    @ApiModelProperty(value = "材质名称")
    @NotBlank(message = "M_MATERIAL_NAME_NULL")
    private String name;

    @NotNull(message = "M_MATERIAL_CATEGORY_ID_NULL")
    @ApiModelProperty(value = "材质分类Id")
    private Integer categoryId;

    @ApiModelProperty(value = "材质英文名称")
    private String englishName;

    @ApiModelProperty(value = "第三方工厂")
    private String manufactor; //指定生产商

    @ApiModelProperty(value = "父类Id",notes = "一级分类传0")
    @NotNull(message = "M_PARENT_ID_NULL")
    private Integer parentId;


    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "颜色")
    //@NotBlank(message = "M_MATERIAL_COLOUR_NULL")
    private String colour;

    @ApiModelProperty(value = "是否最终材质",notes = "1,是 0、否")
    @NotNull(message = "M_MATERIAL_AT_LAST_TRADEMARK_NULL")
    private Short atLastTrademark;
    /** 材质术语*/
    @ApiModelProperty(value = "材质术语")
    @NotBlank(message = "M_STUFF_NAME_NULL")
    private String stuffName;
    /** 材质英文名称*/
    @ApiModelProperty(value = "材质英文术语")
    //@NotBlank(message = "M_STUFF_ENGLISH_NAME_NULL")
    private String stuffEnName;

    @ApiModelProperty(value = "副标题")
    private String subtitle;

    @ApiModelProperty(value = "关联的货品id",notes = "材质启用、该值不能为空")
    private Integer itemId;

    @ApiModelProperty(value ="关联的货品80码",notes = "材质启用、该值不能为空" )
    private String itemCode;

    @ApiModelProperty(value = "材料详情")
    private String content;

    @ApiModelProperty(value = "材料描述")
    private String description;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;

    @ApiModelProperty(value = "是否允许上传图片",notes = "1、是 0、否、最终材质必填")
    private Short allowUploadFlag;

   // @NotEmpty(message = "M_MODEL_MATERIAL_RELA_NULL")
    @Valid
    @ApiModelProperty(value = "材质型号关联关系列表")
    private List<ModelMaterialRelevanceDTO> materialRelevanceDTOList;


    @ApiModelProperty(value = "分销商不可用id列表")
    private List<Integer> distributorIdList;

    @ApiModelProperty(value = "关联的图片id列表")
    private List<Integer> pictureIdList;

    @ApiModelProperty(value = "是否强制铺满出血位 1、是 0、否")
    @NotNull(message = "M_MATERIAL_MANDATORY_COVERED_BLEED_FLAG_NULL")
    private Short mandatoryCoveredBleedFlag;

}
