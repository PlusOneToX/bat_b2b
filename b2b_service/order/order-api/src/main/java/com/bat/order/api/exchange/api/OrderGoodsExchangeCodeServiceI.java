package com.bat.order.api.exchange.api;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.exchange.dto.ExchangeCodePageQry;
import com.bat.order.api.exchange.dto.OrderGoodsExchangeCodeListDTO;

public interface OrderGoodsExchangeCodeServiceI {

    /**
     * 分页查询兑换码兑换数据
     * @param exchangeCodePageQry
     * @return
     */
    PageInfo<OrderGoodsExchangeCodeListDTO> page(ExchangeCodePageQry exchangeCodePageQry);

    void test();
}
