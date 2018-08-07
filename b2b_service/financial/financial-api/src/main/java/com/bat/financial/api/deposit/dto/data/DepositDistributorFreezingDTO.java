package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 11:55
 */
@Data
public class DepositDistributorFreezingDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "分销客户预存款账户id")
    private Integer depositDistributorId;
    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
    @ApiModelProperty(value = "分销商名称")
    private String distributorName;
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezingAmount;
    // @ApiModelProperty(value = "")
    // private BigDecimal thawAmount;
    @ApiModelProperty(value = "业务类型 1,提现冻结 2,其他冻结")
    private Short businessType;
    @ApiModelProperty(value = "业务id")
    private Short businessId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}
