package com.platform.modules.mall.excel;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TeamDetailsExcel {

    @Excel(name = "序号", width = 15)
    @TableId
    private String id;

    @Excel(name = "邀请用户账号", width = 15)
    private String userId;

    @Excel(name = "邀请用户等级", width = 15)
    private String levelName;

    @Excel(name = "邀请关系", width = 15)
    private Integer relation;

    @Excel(name = "下单总金额", width = 15)
    private BigDecimal orderActualPrice;

    @Excel(name = "返佣总金额", width = 15)
    private BigDecimal ratioSum;

    @Excel(name = "加入时间", width = 15)
    private Date joinTime;
}
