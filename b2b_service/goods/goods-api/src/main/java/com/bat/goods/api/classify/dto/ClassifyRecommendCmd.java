package com.bat.goods.api.classify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "商品推荐分类信息封装修改")
public class ClassifyRecommendCmd {

    @ApiModelProperty(value = "推荐分类ids")
    private List<Integer> classifyIds =new ArrayList<>();

    @ApiModelProperty(value = "banner图")
    private List<ClassifyBanner> classifyBanners=new ArrayList<>();

    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = true, example = "78445")
    private String cacheKey = "userClassifyList";


    @Data
    public static class ClassifyBanner {

        @ApiModelProperty(value = "主键")
        private Integer id;

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
