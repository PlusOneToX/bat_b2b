package com.platform.modules.mall.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BalanceDto {
    private Date updateTime;
    /**
     * 数量
     */
    private Object amount;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 冻结
     */
    private Object freeze;

    private Integer type;
    private Integer businessType;

    private String remark;

    private String label;

    private String pid;
}
