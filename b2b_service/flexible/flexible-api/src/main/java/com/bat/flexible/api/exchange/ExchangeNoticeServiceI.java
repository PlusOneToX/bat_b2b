package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;

import java.util.List;

public interface ExchangeNoticeServiceI {


    PageInfo<List<ExchangeNoticeDO>> page(BasePageParamQry basePageParamQry);

    ExchangeNoticeDO create(ExchangeNoticeDO exchangeNotice);

    ExchangeNoticeDO update(ExchangeNoticeDO exchangeNotice);

    void delete(Integer id);

    List<ExchangeNoticeDO> noticeList();

    ExchangeNoticeDO getById(Integer id);
}
