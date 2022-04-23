package com.platform.modules.mall.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ShopDetailExcel {

    @Excel(name = "邀请账号", width = 15)
    private String userName;
    private String userId;
    @Excel(name = "店铺用户下单总金额", width = 15)
    private BigDecimal actualPrice;
    @Excel(name = "返佣总金额", width = 15)
    private BigDecimal award;
    @Excel(name = "店铺入驻时间", width = 15)
    private Date joinTime;
}
