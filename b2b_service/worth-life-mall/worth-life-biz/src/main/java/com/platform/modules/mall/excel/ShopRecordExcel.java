package com.platform.modules.mall.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ShopRecordExcel {
    @Excel(name = "账号", width = 15)
    private String userName;
    @Excel(name = "等级名称", width = 15)
    private String levelName;
    @Excel(name = "邀请关系", width = 15,replace = { "1_直接", "2_间接" })
    private Integer relation;
    @ApiModelProperty("店铺用户下单总金额")
    @Excel(name = "店铺用户下单总金额", width = 15 )
    private BigDecimal actualPrice;
    @ApiModelProperty("返佣总金额")
    @Excel(name = "返佣总金额", width = 15 )
    private BigDecimal award;
    @Excel(name = "加入时间", width = 15 )
    @ApiModelProperty("加入时间")
    private Date joinTime;
}
