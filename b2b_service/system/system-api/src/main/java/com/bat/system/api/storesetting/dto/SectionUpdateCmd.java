package com.bat.system.api.storesetting.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:03
 */
@Data
@ApiModel(value = "SectionUpdateCmd", description = "板块更新")
public class SectionUpdateCmd {

    @NotNull(message = "P_SECTION_ID_NULL")
    @ApiModelProperty(value = "板块ID", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "板块标题", required = true, example = "1")
    private String title;

    @ApiModelProperty(value = "板块英文标题", required = true, example = "1")
    private String titleEn;

    @NotNull(message = "P_SECTION_SORT_NULL")
    @ApiModelProperty(value = "板块排序", required = true, example = "1")
    private Integer sort;

    @NotNull(message = "P_SECTION_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "板块发布状态", required = true, example = "1")
    private Short releaseStatus;

    @ApiModelProperty(value = "推广图片URL", required = true, example = "1")
    private String imageUrl;

    @ApiModelProperty(value = "推广调转链接", required = true, example = "1")
    private String extensionUrl;

    @ApiModelProperty(value = "推广图片URL英文", required = true, example = "1")
    private String imageUrlEn;

    @ApiModelProperty(value = "推广调转链接英文", required = true, example = "1")
    private String extensionUrlEn;

    @NotNull(message = "P_SECTION_AREA_NULL")
    @ApiModelProperty(value = "板块发布区域 0 国内 1 ", required = true, example = "1")
    private Short sectionArea;

    @ApiModelProperty(value = "样式类型", required = true, example = "1")
    private Short styleType;

    @ApiModelProperty(value = "样式类型英文", required = true, example = "1")
    private Short styleTypeEn;

    @ApiModelProperty(value = "样式图片url1", required = true, example = "1")
    private String styleUrl;

    @ApiModelProperty(value = "样式板块调转url1", required = true, example = "1")
    private String styleExtensionUrl;

    @ApiModelProperty(value = "样式图片url2", required = true, example = "1")
    private String styleUrl1;

    @ApiModelProperty(value = "样式板块调转url2", required = true, example = "1")
    private String styleExtensionUrl1;

    @ApiModelProperty(value = "样式图片url3", required = true, example = "1")
    private String styleUrl2;

    @ApiModelProperty(value = "样式板块调转url3", required = true, example = "1")
    private String styleExtensionUrl2;

    @ApiModelProperty(value = "样式图片url4", required = true, example = "1")
    private String styleUrl3;

    @ApiModelProperty(value = "样式板块调转url4", required = true, example = "1")
    private String styleExtensionUrl3;

    /**
     * 绑定的新加商品
     */
    // @ApiModelProperty(value = "绑定的商品", dataType = "list")
    protected List<SectionGoodsItem> goodsIds;

}