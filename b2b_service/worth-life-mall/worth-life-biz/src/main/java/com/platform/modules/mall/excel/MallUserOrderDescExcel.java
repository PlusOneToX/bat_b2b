package com.platform.modules.mall.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class MallUserOrderDescExcel {

    @Excel(name = "序号", width = 15)
    @TableId
    private String id;

    @Excel(name = "订单编号", width = 15)
    private String pid;

    @Excel(name = "订单实付金额(￥)", width = 15)
    private BigDecimal orderPrice;

    @Excel(name = "奖励金额(￥)", width = 15)
    private BigDecimal amount;

    @Excel(name = "奖励比例", width = 15)
    private String discount;

    @Excel(name = "奖励时间", width = 15)
    private Date createTime;

}
