package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSharePageCO;
import com.bat.flexible.api.exchange.dto.*;

import java.util.List;

public interface ExchangeShareServiceI {
    
    PageInfo<List<ExchangeSharePageCO>> page(ExchangeSharePageQry exchangeSharePageQry);

    Response add(ExchangeShareCmd exchangeShareCmd);

    Response update(ExchangeShareCmd exchangeShareCmd);

    void delete(Integer id);

    Response<ExchangeShareDetailDTO> detailById(Integer id);

    ExchangeShareDTO find(ExchangeShareQry exchangeShareQry);

    ExchangeShareCarryOutDTO carryOut(ExchangeShareCarryOutCmd exchangeShareCarryOutCmd,Integer userId);

    void receive(Integer userId, Integer exchangeSpecialReleaseId, AdminResponse currentAdmin);

    ExchangeShareCarryOutDTO releaseDetail(Integer exchangeShareReleaseId,Integer userId);

    ExchangeShareCarryOutDTO secondCarryOut(Integer exchangeShareReleaseId, Integer userId);
}
