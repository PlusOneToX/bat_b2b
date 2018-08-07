package com.bat.system.api.storesetting.dto.data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:03
 */
@Data
@ApiModel(value = "ColumnDTO")
public class ColumnDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "栏目英文标题")
    private String titleEn;
    @ApiModelProperty(value = "栏目banner")
    private String bannerImg;
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    @ApiModelProperty(value = "发布状态，0,未发布，1 发布 2 取消发布")
    private Short releaseStatus;
    @ApiModelProperty(value = "栏目使用区域 0-国内 1 海外 2 国内和海外")
    private Short columnArea;
    @ApiModelProperty(value = "1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商")
    private Short distributorScope;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 绑定的新加商品
     */
    // @ApiModelProperty(value = "绑定的商品", dataType = "list")
    // protected List<ColumnGoodsItem> goodsIds;

    /**
     * 指定的分销商等级
     */
    @ApiModelProperty(value = "指定的分销商等级", dataType = "list")
    protected List<Integer> gradeIds;

    /**
     * 指定的销售部门
     */
    @ApiModelProperty(value = "指定的销售部门", dataType = "list")
    protected List<Integer> departmentIds;

    /**
     * 指定的业务员（后台用户）
     */
    @ApiModelProperty(value = "指定的业务员（后台用户）", dataType = "list")
    protected List<Integer> userIds;

    /**
     * 指定的分销商
     */
    @ApiModelProperty(value = "指定的分销商", dataType = "list")
    protected List<Integer> distributorIds;
}
