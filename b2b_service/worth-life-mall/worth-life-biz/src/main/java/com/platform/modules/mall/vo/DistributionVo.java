package com.platform.modules.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DistributionVo {

    @ApiModelProperty("直推人数")
    private Integer directCount;
    @ApiModelProperty("间推人数")
    private Integer indirectCount;
    @ApiModelProperty("店铺数量")
    private Integer shopCount;
    @ApiModelProperty("奖励总额")
    private BigDecimal award;
}
