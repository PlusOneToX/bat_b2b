package com.bat.flexible.manager.exchange;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.manager.exchange.executor.ExchangeSpecialCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeSpecialQryExe;
import com.bat.flexible.api.exchange.ExchangeSpecialServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialPageCO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeSpecialServiceImpl implements ExchangeSpecialServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeSpecialServiceImpl.class);

    @Autowired
    private ExchangeSpecialQryExe exchangeSpecialQryExe;

    @Autowired
    private ExchangeSpecialCmdExe exchangeSpecialCmdExe;


    @Override
    public PageInfo<List<ExchangeSpecialPageCO>> page(ExchangeSpecialPageQry exchangeSpecialPageQry) {
        PageHelper.startPage(exchangeSpecialPageQry.getPage(), exchangeSpecialPageQry.getSize());
        List<ExchangeSpecialPageCO> list = exchangeSpecialQryExe.listCOByCondition(exchangeSpecialPageQry.getTitle(), exchangeSpecialPageQry.getDistributorId(),exchangeSpecialPageQry.getActivityPlatform(),exchangeSpecialPageQry.getType());
        return new PageInfo(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response add(ExchangeSpecialCmd exchangeSpecialCmd, AdminResponse currentAdmin) {
        exchangeSpecialCmdExe.add(exchangeSpecialCmd);
        return Response.buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response update(ExchangeSpecialCmd exchangeSpecialCmd, AdminResponse currentAdmin) {
        exchangeSpecialCmdExe.update(exchangeSpecialCmd);
        return Response.buildSuccess();
    }

    @Override
    public Response<ExchangeSpecialDetailDTO> detailById(Integer id) {
        ExchangeSpecialDetailDTO detail = exchangeSpecialQryExe.detail(id);
        return Response.of(detail);
    }

    @Override
    public PageInfo<List<ExchangeSpecialDistributorCO>> distributorPage(ExchangeSpecialDistributorPageQry exchangeSpecialDistributorPageQry) {
        PageHelper.startPage(exchangeSpecialDistributorPageQry.getPage(), exchangeSpecialDistributorPageQry.getSize());
        List<ExchangeSpecialDistributorCO> list = exchangeSpecialQryExe.listDistributorCOByCondition(exchangeSpecialDistributorPageQry.getExchangeSpecialId(), exchangeSpecialDistributorPageQry.getDistributorId());
        return new PageInfo(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response distributorAdd(ExchangeSpecialDistributorAddCmd exchangeSpecialDistributorAddCmd) {
        exchangeSpecialCmdExe.distributorAdd(exchangeSpecialDistributorAddCmd);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorUpdate(ExchangeSpecialDistributorUpdateCmd exchangeSpecialDistributorUpdateCmd) {
        exchangeSpecialCmdExe.distributorUpdate(exchangeSpecialDistributorUpdateCmd);
        return Response.buildSuccess();
    }

    @Override
    public String specialDistributorQrCodeUrl(Integer exchangeSpecialDistributorId) {
        return exchangeSpecialCmdExe.qrCodeUrl(exchangeSpecialDistributorId);
    }

    @Override
    public String specialDistributorShortLink(Integer exchangeSpecialDistributorId) {
        return exchangeSpecialCmdExe.shortLink(exchangeSpecialDistributorId);
    }

    @Override
    public String specialDistributorLink(Integer exchangeSpecialDistributorId) {
        return exchangeSpecialCmdExe.link(exchangeSpecialDistributorId);
    }
}
