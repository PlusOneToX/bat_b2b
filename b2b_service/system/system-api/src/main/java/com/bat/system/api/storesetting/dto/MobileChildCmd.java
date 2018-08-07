package com.bat.system.api.storesetting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MobileChildCmd {

    private Integer id;

    private Integer parentId;

    @ApiModelProperty(value = "指定类型 1.指定分类 2.指定品牌 3.指定商品")
    private Short appointType;

    @ApiModelProperty(value = "分类标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private List<MobilePointCmd> mobilePoints=new ArrayList<>();

    private List<MobileGoodsItem> goodsIds=new ArrayList<>();

}