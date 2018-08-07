package com.bat.goods.api.classify.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "商品推荐分类信息封装")
public class ClassifyRecommendDTO implements Serializable {

    @ApiModelProperty(value = "推荐分类信息")
    private List<Classify> classifies;

    @ApiModelProperty(value = "banner图")
    private List<ClassifyBanner> classifyBanners;

    @Data
    public static class Classify implements Serializable {

        private Integer id;
        @ApiModelProperty(value = "商品分类名称", required = true, example = "bat")
        private String name;
        @ApiModelProperty(value = "商品分类英文名称", required = false, example = "bat")
        private String nameEn;

        @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
        private Short openFlag;

        @ApiModelProperty(value = "是否推荐分类 0否 1是", required = true, example = "0")
        private Short recommendFlag;

        @ApiModelProperty(value = "小程序分类名称", required = true, example = "0")
        private String appletName;

        @ApiModelProperty(value = "分类图片地址")
        private String imageUrl;

        @ApiModelProperty(value = "分类英文图片地址")
        private String imageUrlEn;

    }

    @Data
    public static class ClassifyBanner implements Serializable {

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
}
