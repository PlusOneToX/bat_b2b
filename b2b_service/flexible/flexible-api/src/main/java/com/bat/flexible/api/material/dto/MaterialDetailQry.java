package com.bat.flexible.api.material.dto;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MaterialDetailQry {

    @ApiModelProperty(value = "材质id",name = "id")
    private Integer id;

    @ApiModelProperty(value = "材质编码",name = "materialNo")
    private String materialNo;

    @ApiModelProperty(value = "材质第三方code",notes = "最终材质必填")
    private String code;

    @ApiModelProperty(value = "材质名称")
    private String name;

    @ApiModelProperty(value = "材质分类Id")
    private Integer categoryId;

    @ApiModelProperty(value = "材质英文名称")
    private String englishName;

    @ApiModelProperty(value = "第三方工厂")
    private String manufactor; //指定生产商

    @ApiModelProperty(value = "父类Id",notes = "一级分类传0")
    private Integer parentId;


    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "颜色")
    private String colour;

    @ApiModelProperty(value = "是否最终材质",notes = "1,是 0、否")
    private Short atLastTrademark;
    /** 材质术语*/
    @ApiModelProperty(value = "材质术语")
    private String stuffName;
    /** 材质英文名称*/
    @ApiModelProperty(value = "材质英文术语")
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
    private Short openFlag;

    @ApiModelProperty(value = "是否允许上传图片，是 1，0、否")
    private Short allowUploadFlag;

    @ApiModelProperty(value = "材质和型号的关联列表")
    private List<ModelMaterialRelevanceCO> materialRelevanceDTOList;


    @ApiModelProperty(value = "分销商不可用列表")
    private List<DistributorSimpleRelaQry> distributorExcludeList;

    @ApiModelProperty(value = "材质关联的图片列表")
    private List<PictureRelaSimpleDTO> pictureRelaList;

    @ApiModelProperty(value = "是否强制铺满出血位 1、是 0、否")
    private Short mandatoryCoveredBleedFlag;

    @ApiModelProperty(value = "最底级材质关联的货品信息")
    private GoodsItemSimplePageBean itemSimplePageBean;

    @ApiModelProperty(value = "父级机型")
    private List<ModelSimpleCO> parentModel;
}
