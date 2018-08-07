package com.bat.system.api.logistics.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/27 20:17
 */
@Data
@ApiModel(value = "LogisticsCalculationQry", description = "配送费用计算")
public class LogisticsCalculationQry {

    /**
     * RPC调用需要
     */
    private Integer logisticsId;

    @NotNull(message = "P_LOGISTICS_COUNTRY_ID_NULL")
    @ApiModelProperty(value = "国家id", required = true, example = "37")
    private Integer countryId;

    @NotNull(message = "P_LOGISTICS_PROVINCE_ID_NULL")
    @ApiModelProperty(value = "省id", required = true, example = "440000")
    private Integer provinceId;

    @NotNull(message = "P_LOGISTICS_CITY_ID_NULL")
    @ApiModelProperty(value = "城市id", required = true, example = "440300")
    private Integer cityId;

    @ApiModelProperty(value = "地区id(可以不传 传了也不会用)", example = "440307")
    private Integer districtId;

    @NotBlank(message = "P_LOGISTICS_USE_RANGE_NULL")
    @ApiModelProperty(value = "适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品", required = true, example = "1")
    private String useRange;

    @ApiModelProperty(value = "生产商 YC.云创 LHW.联辉王", example = "LHW")
    private String manufactors;

    // @ApiModelProperty(value = "订单类型 0.普通，3.定制，4.Mto，5.直运，6.预售")
    // private String orderType;

    @NotNull(message = "")
    @ApiModelProperty(value = "订单总金额", required = true, example = "30")
    private BigDecimal price;

    @ApiModelProperty(value = "定制订单材质id")
    private List<Integer> materialId;

    @NotNull(message = "")
    @ApiModelProperty(value = "订单结算方式，1重量计费，2体积计费", required = true, example = "1")
    private Short billingMethod;

    @ApiModelProperty(value = "订单总重量", example = "20")
    private Double weight;

    @ApiModelProperty(value = "订单总体积", example = "20")
    private Double volume;

    @ApiModelProperty(value = "分销商等级", example = "20")
    private Integer gradeId;
    @ApiModelProperty(value = "部门", example = "20")
    private Integer departmentId;
    @ApiModelProperty(value = "业务员", example = "20")
    private Integer userId;
    @ApiModelProperty(value = "分销商id", required = true, example = "20")
    private Integer distributorId;

}
