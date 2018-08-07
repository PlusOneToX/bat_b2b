package com.bat.system.api.logistics.dto.data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "LogisticsDTO")
public class LogisticsDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "物流名称")
    private String name;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否启用")
    private Short enable;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "erp配送方式编号")
    private String logisticsErpId;

    @ApiModelProperty(value = "计费方式 1 重量计费(默认) 2 体积计费")
    private Short billingMethod;

    @ApiModelProperty(value = "首重重量")
    private Double firstWeight;

    @ApiModelProperty(value = "首重体积")
    private Double firstVolume;

    @ApiModelProperty(value = "续重重量")
    private Double additionalWeight;

    @ApiModelProperty(value = "续重体积")
    private Double additionalVolume;

    @ApiModelProperty(value = "最低重量")
    private Double minWeight;

    @ApiModelProperty(value = "最低体积")
    private Double minVolume;

    @ApiModelProperty(value = "最高重量")
    private Double maxWeight;

    @ApiModelProperty(value = "最高体积")
    private Double maxVolume;

    @ApiModelProperty(value = "最低运费")
    private Double minCost;

    @ApiModelProperty(value = "适用范围，1.普通商品，2.定制商品 3普通商品和定制商品")
    private Short useRange;

    @ApiModelProperty(value = "工厂配送方式编号")
    private String logisticsFactoryId;

    @ApiModelProperty(value = "快递鸟快递公司编码")
    private String logisticsKdnCode;

    @ApiModelProperty(value = "快递鸟快递公司名称")
    private String logisticsKdnName;

    @ApiModelProperty(value = "是否指定地区 0为统一设置 1为指定地区")
    private Short appointAreaFlag;

    @ApiModelProperty(value = "1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商")
    private Short distributorScope;

    @ApiModelProperty(value = "生产商 YC.云创 LHW.联辉王（同时支持，中间用\",\"号隔开） 没用")
    private String manufactors;

    @ApiModelProperty(value = "快递官网地址")
    private String website;

    @ApiModelProperty(value = "材质id 多个材质逗号分隔")
    private String materialId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 地区计费
     */
    @ApiModelProperty(value = "地区计费")
    private List<LogisticsAreaDTO> logisticsCost;

    /**
     * 指定的分销商类型
     */
    @ApiModelProperty(value = "指定的分销商类型")
    private List<Integer> gradeIds;

    /**
     * 指定的销售部门
     */
    @ApiModelProperty(value = "指定的销售部门")
    private List<Integer> departmentIds;

    /**
     * 指定的业务员（后台用户）
     */
    @ApiModelProperty(value = "指定的业务员（后台用户）")
    private List<Integer> userIds;

    /**
     * 指定的分销商
     */
    @ApiModelProperty(value = "指定的分销商")
    private List<Integer> distributorIds;

}
