package com.bat.system.api.storesetting.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 21:02
 */
@Data
@ApiModel(value = "ColumnCreateCmd", description = "栏目新增")
public class ColumnCreateCmd {

    @NotBlank(message = "P_COLUMN_TITLE_NULL")
    @ApiModelProperty(value = "栏目标题", required = true, example = "切膜机")
    private String title;

    @NotBlank(message = "P_COLUMN_TITLE_EN_NULL")
    @ApiModelProperty(value = "栏目英文标题", required = true, example = "Cut Film Machine")
    private String titleEn;

    @NotBlank(message = "P_COLUMN_BANNER_NULL")
    @ApiModelProperty(value = "栏目banner")
    private String bannerImg;

    @NotNull(message = "P_COLUMN_SORT_NULL")
    @ApiModelProperty(value = "栏目排序值 越小越靠前", required = true, example = "0")
    private Integer sort;

    @NotNull(message = "P_COLUMN_RELEASE_STATUS_NULL")
    @ApiModelProperty(value = "栏目发布状态", required = true, example = "1")
    private Short releaseStatus;

    @NotNull(message = "P_COLUMN_AREA_NULL")
    @ApiModelProperty(value = "栏目发布区域 0国内 1国外 2 国内和国外", required = true, example = "2")
    private Short columnArea;

    @NotNull(message = "P_COLUMN_DISTRIBUTOR_SCOPE_NULL")
    @ApiModelProperty(value = "指定分销商可视范围 1全部分销商,2等级分销商,3事业部（取消）,4指定业务部门,5指定业务员,6指定分销商", required = true, example = "2")
    private Short distributorScope;

    /**
     * 绑定的商品
     */
    // @ApiModelProperty(value = "绑定的商品", dataType = "list")
    protected List<ColumnGoodsItem> goodsIds;

    /**
     * 指定的分销商等级
     */
    // @ApiModelProperty(value = "指定的分销商等级", dataType = "list")
    protected List<Integer> gradeIds;

    /**
     * 指定的销售部门
     */
    // @ApiModelProperty(value = "指定的销售部门", dataType = "list")
    protected List<Integer> departmentIds;

    /**
     * 指定的业务员（后台用户）
     */
    // @ApiModelProperty(value = "指定的业务员（后台用户）", dataType = "list")
    protected List<Integer> userIds;

    /**
     * 指定的分销商
     */
    // @ApiModelProperty(value = "指定的分销商", dataType = "list")
    protected List<Integer> distributorIds;

}
