package com.platform.modules.mall.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ShopListExcel {
    @TableId(type = IdType.AUTO)
    @Excel(name = "序号", width = 15)
    private String id;

    @Excel(name = "店铺账号", width = 15)
    private String mobile;

    @Excel(name = "店铺用户下单总金(￥)", width = 15)
    private BigDecimal orderActualPrice;

    @Excel(name = "店铺返佣总金额(￥)", width = 15)
    private BigDecimal multiplySum;

}
