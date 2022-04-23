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
public class ShopUserRecord {
    @Excel(name = "账号", width = 15)
    private String userName;
    @Excel(name = "等级", width = 15)
    private String levelName;
    @Excel(name = "邀请关系", width = 15,replace = { "1_直接", "2_间接" })
    private Integer relation;
    private String userId;
    @Excel(name = "下单总金额", width = 15)
    private BigDecimal actualPrice;
    @Excel(name = "返佣总金额", width = 15)
    private BigDecimal award;
    @Excel(name = "加入时间", width = 15)
    private Date createTime;
}
