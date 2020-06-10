package com.bat.flexible.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModelQry {

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商id列表")
    private List<Integer> distributorIds;

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId=1;

    @ApiModelProperty(value = "图片id")
    private Integer pictureId;

    @ApiModelProperty(value = "平台id")
    private String platform;

    @ApiModelProperty(value = "货品id、对应材质关联的货品id")
    private Integer itemId;

    @ApiModelProperty(value = "搜索关键词")
    private String search;

    @ApiModelProperty(value = "兑换卡活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

}
