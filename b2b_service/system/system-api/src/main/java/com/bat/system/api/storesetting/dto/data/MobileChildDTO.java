package com.bat.system.api.storesetting.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MobileChildDTO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "父级id")
    private Integer parentId;

    @ApiModelProperty(value = "指定类型 1.指定分类 2.指定品牌 3.指定商品 4.指定全部商品")
    private Short appointType;

    @ApiModelProperty(value = "分类标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "分类信息")
    private List<Classify> classifies;

    @ApiModelProperty(value = "品牌信息")
    private List<Brand>  brands;

    @ApiModelProperty(value = "指定数据")
    private List<Integer> pointIds;

    @Data
    public static class Classify{
        @ApiModelProperty(value = "id")
        private Integer id;
        @ApiModelProperty(value = "分类名称")
        private String name;
        @ApiModelProperty(value = "分类英文名")
        private String nameEn;
    }

    @Data
    public static class Brand{
        @ApiModelProperty(value = "id")
        private Integer id;
        @ApiModelProperty(value = "品牌名称")
        private String name;
        @ApiModelProperty(value = "品牌英文名")
        private String nameEn;
    }

}