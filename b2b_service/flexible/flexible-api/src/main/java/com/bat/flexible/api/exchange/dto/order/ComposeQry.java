package com.bat.flexible.api.exchange.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class ComposeQry {
    /**
     * 缺少兑换码的个数
     */
    private Integer needNum = 0;


    /**
     * 已选择兑换码的材料型号实体
     */
    private List<ExchangeCodeSelectQry> selects;
}
