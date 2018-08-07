package com.bat.system.api.storesetting.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:03
 */
@Data
@ApiModel(value = "SectionDTO")
public class SectionDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "板块英文标题")
    private String titleEn;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "发布状态，0,未发布，1 发布")
    private Short releaseStatus;
    @ApiModelProperty(value = "推广图片地址")
    private String imageUrl;
    @ApiModelProperty(value = "推广链接地址")
    private String extensionUrl;
    @ApiModelProperty(value = "板块英文图片链接")
    private String imageUrlEn;
    @ApiModelProperty(value = "板块英文链接")
    private String extensionUrlEn;
    @ApiModelProperty(value = "1.样式1 2.样式2 3.样式3 4.样式4")
    private Short styleType;
    @ApiModelProperty(value = "1.样式1 2.样式2 3.样式3 4.样式4")
    private Short styleTypeEn;
    @ApiModelProperty(value = "板块使用区域 0-国内 1 海外 2 国内和海外")
    private Short sectionArea;
    @ApiModelProperty(value = "样式图片1地址(国内1)")
    private String styleUrl;
    @ApiModelProperty(value = "样式连接1地址")
    private String styleExtensionUrl;
    @ApiModelProperty(value = "样式图片2地址(国内2)")
    private String styleUrl1;
    @ApiModelProperty(value = "样式连接2地址")
    private String styleExtensionUrl1;
    @ApiModelProperty(value = "样式图片3地址(国内3)")
    private String styleUrl2;
    @ApiModelProperty(value = "样式连接3地址")
    private String styleExtensionUrl2;
    @ApiModelProperty(value = "样式图片4地址(国内4)")
    private String styleUrl3;
    @ApiModelProperty(value = "样式连接4地址")
    private String styleExtensionUrl3;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}