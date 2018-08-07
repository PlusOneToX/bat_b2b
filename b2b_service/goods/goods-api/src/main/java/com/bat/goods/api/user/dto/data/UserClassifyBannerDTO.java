package com.bat.goods.api.user.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "商品分类banner信息")
public class UserClassifyBannerDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "分类id")
    private Integer classifyId;

    @ApiModelProperty(value = "轮播图地址")
    private String imgUrl;

    @ApiModelProperty(value = "跳转地址")
    private String jumpUrl;

    @ApiModelProperty(value = "排序")
    private Short sort;

    @ApiModelProperty(value = "是否推荐轮播图 0否 1是")
    private Short recommendFlag;
}
