package com.bat.system.api.logistics.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "LogisticsUpdateCmd", description = "配送方式更新")
public class LogisticsUpdateCmd {

    @NotNull(message = "P_LOGISTICS_ID_NULL")
    @ApiModelProperty(value = "配送方式id", required = true, example = "1")
    private Integer id;

    @NotBlank(message = "P_LOGISTICS_NAME_NULL")
    @ApiModelProperty(value = "配送方式名称", required = true, example = "百世快递（在线定制专用）")
    private String name;

    @NotNull(message = "P_LOGISTICS_SORT_NULL")
    @ApiModelProperty(value = "排序值", required = true, example = "1")
    private Integer sort;

    @NotNull(message = "P_LOGISTICS_ENABLE_NULL")
    @ApiModelProperty(value = "状态值", required = true, example = "1")
    private Short enable;

    @ApiModelProperty(value = "详细介绍", example = "满2000免配送费")
    private String description;

    @NotBlank(message = "P_LOGISTICS_ERP_ID_NULL")
    @ApiModelProperty(value = "ERP配送方式ID", required = true, example = "百世快递（在线定制专用）")
    private String logisticsErpId;

    @NotNull(message = "P_LOGISTICS_MIN_WEIGHT_NULL")
    @ApiModelProperty(value = "最小重量", required = true, example = "1")
    private Double minWeight;

    @NotNull(message = "P_LOGISTICS_MIN_VOLUME_NULL")
    @ApiModelProperty(value = "最小体积", required = true, example = "1")
    private Double minVolume;

    @NotNull(message = "P_LOGISTICS_MAX_WEIGHT_NULL")
    @ApiModelProperty(value = "最大重量", required = true, example = "1")
    private Double maxWeight;

    @NotNull(message = "P_LOGISTICS_MAX_VOLUME_NULL")
    @ApiModelProperty(value = "最大体积", required = true, example = "1")
    private Double maxVolume;

    @NotNull(message = "P_LOGISTICS_MIN_COST_NULL")
    @ApiModelProperty(value = "最低费用（留空默认不限制）", required = true, example = "1")
    private Double minCost;

    // @NotBlank(message = "P_LOGISTICS_KDN_CODE_NULL")
    @ApiModelProperty(value = "快递鸟快递公司编码", required = true, example = "1")
    private String logisticsKdnCode;

    // @NotBlank(message = "P_LOGISTICS_KDN_NAME_NULL")
    @ApiModelProperty(value = "快递鸟快递公司名称", required = true, example = "1")
    private String logisticsKdnName;

    @ApiModelProperty(value = "工厂配送方式编号", example = "1")
    private String logisticsFactoryId;

    @NotNull(message = "P_LOGISTICS_APPOINT_AREA_FLAG_NULL")
    @ApiModelProperty(value = "是否指定地区 0为统一设置 1为指定地区", required = true, example = "1")
    private Short appointAreaFlag;

    // @NotBlank(message = "P_LOGISTICS_MANUFACTOR_NULL")
    @ApiModelProperty(value = "生产商 YC.云创 LHW.联辉王（同时支持，中间用\",\"号隔开）", required = true, example = "1")
    private String manufactors;

    @ApiModelProperty(value = "快递公司官网地址", required = true, example = "1")
    private String website;

    /**
     * 地区计费
     */
    @ApiModelProperty(value = "地区计费", required = true)
    private List<LogisticsAreaCmd> logisticsCost;

    @NotNull(message = "P_LOGISTICS_BILLING_METHOD_NULL")
    @ApiModelProperty(value = "计费方式（重量、体积）", required = true, example = "1")
    private Short billingMethod;

    @ApiModelProperty(value = "首重重量", example = "1")
    private Double firstWeight;

    @ApiModelProperty(value = "首重体积", example = "1")
    private Double firstVolume;

    @ApiModelProperty(value = "续重重量单位", example = "1")
    private Double additionalWeight;

    @ApiModelProperty(value = "续重体积单位", example = "1")
    private Double additionalVolume;

    @NotNull(message = "P_LOGISTICS_USE_RANGE_NULL")
    @ApiModelProperty(value = "适用范围 1普通商品 2定制商品 3普通商品和定制商品", required = true, example = "1")
    private Short useRange;

    @NotNull(message = "P_LOGISTICS_SCOPE_NULL")
    @ApiModelProperty(value = "指定分销商可视范围（1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商）", required = true, example = "1")
    private Short distributorScope;

    @ApiModelProperty(value = "定制订单材质id")
    private String materialId;

    /**
     * 指定的分销商等级
     */
    // @ApiModelProperty(value = "指定的分销商等级", example = "1")
    private List<Integer> gradeIds;

    /**
     * 指定的销售部门
     */
    // @ApiModelProperty(value = "指定的销售部门", example = "1")
    private List<Integer> departmentIds;

    /**
     * 指定的业务员（后台用户）
     */
    // @ApiModelProperty(value = "指定的业务员（后台用户）", example = "1")
    private List<Integer> userIds;

    /**
     * 指定的分销商
     */
    // @ApiModelProperty(value = "指定的分销商", example = "1")
    private List<Integer> distributorIds;

}
