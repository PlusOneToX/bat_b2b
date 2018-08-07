package com.bat.goods.api.classify.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassifyCmd", description = "商品分类信息")
public class ClassifyCmd implements Serializable {

    @ApiModelProperty(value = "商品分类ID", required = false)
    private Integer id;
    @NotBlank(message = "P_GOODS_CLASSIFY_NAME_NULL")
    @ApiModelProperty(value = "商品分类名称,最长20个字符", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品分类英文名称，最长100个字符", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "商品分类描述,最长300个字符", required = false, example = "bat品牌")
    private String description;
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @NotNull(message = "P_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @ApiModelProperty(value = "父级ID", required = false, example = "1001")
    private Integer parentId;
    @ApiModelProperty(value = "分类图片地址", required = false, example = "http://bat")
    private String imageUrl;
    @ApiModelProperty(value = "分类英文图片地址", required = false, example = "http://bat")
    private String imageUrlEn;

    //@NotNull(message = "P_RECOMMEND_FLAG_NULL")
    @ApiModelProperty(value = "是否推荐分类 0否 1是", required = true, example = "0")
    private Short recommendFlag;

    @ApiModelProperty(value = "小程序分类名称", required = true, example = "0")
    private String appletName;

    @ApiModelProperty(value = "用作缓存key，一定不能传值", required = true, example = "78445")
    private String cacheKey = "userClassifyList";

    @ApiModelProperty(value = "banner图")
    private List<Banner> banners;

    @Data
    public static class Banner implements Serializable {
        @ApiModelProperty(value = "主键")
        private Integer id;
        @ApiModelProperty(value = "图片地址")
        private String imgUrl;
        @ApiModelProperty(value = "跳转链接")
        private String jumpUrl;
        @ApiModelProperty(value = "排序")
        private Short sort;

    }

}
