package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MaterialTreeQry {

    @ApiModelProperty(value = "型号id")
    private Integer modelId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商列表")
    private List<Integer> distributorIds;

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "图片id")
    private Integer pictureId;

    @ApiModelProperty(value = "平台id")
    private String platform;

    @ApiModelProperty(value = "货品id、最底级的材质对应的货品id")
    private Integer itemId;

    @ApiModelProperty(value = "兑换卡活动Id")
    private Integer exchangeId;

   // @ApiModelProperty(value = "指定的材质id列表")
   // private List<Integer> materialIdList=new ArrayList<>();
}
