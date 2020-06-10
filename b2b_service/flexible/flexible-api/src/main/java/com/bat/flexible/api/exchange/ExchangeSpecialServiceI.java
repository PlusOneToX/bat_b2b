package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialPageCO;
import com.bat.flexible.api.exchange.dto.*;

import java.util.List;

public interface ExchangeSpecialServiceI {


    PageInfo<List<ExchangeSpecialPageCO>> page(ExchangeSpecialPageQry exchangeSpecialPageQry);

    Response add(ExchangeSpecialCmd exchangeSpecialCmd, AdminResponse currentAdmin);

    Response update(ExchangeSpecialCmd exchangeSpecialCmd, AdminResponse currentAdmin);

    Response<ExchangeSpecialDetailDTO> detailById(Integer id);

    PageInfo<List<ExchangeSpecialDistributorCO>> distributorPage(ExchangeSpecialDistributorPageQry exchangeSpecialDistributorPageQry);

    Response distributorAdd(ExchangeSpecialDistributorAddCmd exchangeSpecialDistributorAddCmd);

    Response distributorUpdate(ExchangeSpecialDistributorUpdateCmd exchangeSpecialDistributorUpdateCmd);

    String specialDistributorQrCodeUrl(Integer exchangeSpecialDistributorId);

    String specialDistributorShortLink(Integer exchangeSpecialDistributorId);

    String specialDistributorLink(Integer exchangeSpecialDistributorId);
}
