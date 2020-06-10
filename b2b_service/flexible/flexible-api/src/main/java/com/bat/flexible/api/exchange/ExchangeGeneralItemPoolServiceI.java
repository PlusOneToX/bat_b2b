package com.bat.flexible.api.exchange;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.exchange.dto.ExchangeGeneralItemQry;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;

public interface ExchangeGeneralItemPoolServiceI {
    PageInfo<GoodsItemSimplePageBean> page(ExchangeGeneralItemQry exchangeGeneralItemQry);
}
