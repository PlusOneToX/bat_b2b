package com.bat.flexible.api.exchange.dto.order;

import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import lombok.Data;

import java.util.List;

/**
 * 订单预添加返回实体
 */
@Data
public class ExchangePreReturnDTO {

    private List<ExchangeCodeUserCO> exchangeCodeBeans;

    /**
     * 组合好数据的商品块
     */
    private List<ComposeQry> composes;




}
