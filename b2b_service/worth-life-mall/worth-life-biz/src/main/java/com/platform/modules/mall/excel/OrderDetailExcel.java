package com.platform.modules.mall.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDetailExcel {
    @Excel(name = "订单编号", width = 15)
    private String pid;
    @ApiModelProperty("订单金额")
    @Excel(name = "订单金额", width = 15)
    private BigDecimal actualPrice;
    @Excel(name = "奖励金额", width = 15)
    private BigDecimal award;
    @Excel(name = "奖励比例", width = 15)
    private BigDecimal ratio;
    @ApiModelProperty("创建时间")
    @Excel(name = "奖励时间", width = 15)
    private Date createTime;
}
