package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.ExchangeExportCmd;
import com.bat.flexible.api.exchange.dto.ExchangeExportPageQry;
import com.bat.flexible.dao.exchange.co.ExchangeExportPageCO;

import java.util.List;

public interface ExchangeExportServiceI {

    PageInfo<List<ExchangeExportPageCO>> page(ExchangeExportPageQry exchangeExportPageQry);

    Response add(ExchangeExportCmd exchangeExportCmd);

    Response update(ExchangeExportCmd exchangeExportCmd);

    Response<String> export(Integer id, AdminResponse currentAdmin);

    void delete(Integer id);
}
