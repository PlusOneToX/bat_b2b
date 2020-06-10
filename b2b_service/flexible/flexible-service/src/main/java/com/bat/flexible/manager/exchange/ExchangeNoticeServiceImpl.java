package com.bat.flexible.manager.exchange;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import com.bat.flexible.manager.exchange.executor.ExchangeNoticeCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeNoticeQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeNoticeServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ExchangeNoticeServiceImpl implements ExchangeNoticeServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeNoticeServiceImpl.class);

    @Autowired
    private ExchangeNoticeQryExe exchangeNoticeQryExe;

    @Autowired
    private ExchangeNoticeCmdExe exchangeNoticeCmdExe;


    @Override
    public PageInfo<List<ExchangeNoticeDO>> page(BasePageParamQry basePageParamQry) {
        PageHelper.startPage(basePageParamQry.getPage(),basePageParamQry.getSize());
        List<ExchangeNoticeDO> list = exchangeNoticeQryExe.listAllDesc();
        return new PageInfo(list);
    }

    @Override
    public ExchangeNoticeDO create(ExchangeNoticeDO exchangeNotice) {
        exchangeNotice.setCreateTime(new Date());
        if(exchangeNotice.getEndTime()<=exchangeNotice.getStartTime()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME);
        }
        exchangeNoticeCmdExe.create(exchangeNotice);
        return exchangeNotice;
    }

    @Override
    public ExchangeNoticeDO update(ExchangeNoticeDO exchangeNotice) {

        if(exchangeNotice.getEndTime()<=exchangeNotice.getStartTime()){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENDTIME_LESSTHEN_STARTTIME);
        }
        ExchangeNoticeDO exchangeNoticeDO = exchangeNoticeQryExe.getById(exchangeNotice.getId());
        exchangeNotice.setCreateTime(exchangeNoticeDO.getCreateTime());
        exchangeNoticeCmdExe.update(exchangeNotice);
        return exchangeNotice;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) {
        exchangeNoticeCmdExe.deleteById(id);
    }

    @Override
    public List<ExchangeNoticeDO> noticeList() {
        List<ExchangeNoticeDO> list = exchangeNoticeQryExe.listAllDesc();
        return list;
    }

    @Override
    public ExchangeNoticeDO getById(Integer id) {
        ExchangeNoticeDO exchangeNotice=exchangeNoticeQryExe.getById(id);
        return exchangeNotice;
    }
}
