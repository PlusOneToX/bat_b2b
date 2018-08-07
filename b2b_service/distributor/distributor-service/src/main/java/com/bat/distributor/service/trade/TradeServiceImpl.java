package com.bat.distributor.service.trade;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.api.trade.TradeServiceI;
import com.bat.distributor.api.trade.dto.TradeCmd;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.TradeListQry;
import com.bat.distributor.api.trade.dto.TradeOpenCmd;
import com.bat.distributor.api.trade.dto.data.TradeDTO;
import com.bat.distributor.service.trade.executor.TradeCmdExe;
import com.bat.distributor.service.trade.executor.TradeQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

@Service
public class TradeServiceImpl implements TradeServiceI {

    @Resource
    private TradeCmdExe cmdExe;

    @Resource
    private TradeQryExe qryExe;

    @Override
    public void createTrade(TradeCmd cmd) {
        cmdExe.createTrade(cmd);
    }

    @Override
    public void updateTrade(TradeCmd cmd) {
        cmdExe.updateTrade(cmd);
    }

    @Override
    public void openTrade(TradeOpenCmd cmd) {
        cmdExe.openTrade(cmd);
    }

    @Override
    public PageInfo<TradeDTO> listTrade(TradeListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public void deleteTrade(TradeId cmd) {
        cmdExe.deleteTrade(cmd);
    }

    @Override
    public TradeDTO getTrade(TradeId qry) {
        return qryExe.execute(qry);
    }

}