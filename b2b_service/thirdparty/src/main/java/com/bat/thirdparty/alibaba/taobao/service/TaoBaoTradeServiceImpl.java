package com.bat.thirdparty.alibaba.taobao.service;

import com.bat.thirdparty.alibaba.taobao.api.TaoBaoTradeServiceI;
import com.bat.thirdparty.alibaba.taobao.service.executor.TaoBaoTradeCmdExe;
import com.bat.thirdparty.alibaba.taobao.service.executor.TaoBaoTradeQryExe;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.taobao.TradeState;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaoBaoTradeServiceImpl implements TaoBaoTradeServiceI {

    @Autowired
    private TaoBaoTradeQryExe taoBaoTradeQryExe;

    @Autowired
    private TaoBaoTradeCmdExe taoBaoTradeCmdExe;

    @Override
    public TaobaoTradeDO getById(Long tid) {
        return taoBaoTradeQryExe.getById(tid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(TaobaoTradeDO taobaoTradeDO) {
        taoBaoTradeCmdExe.update(taobaoTradeDO);
    }

    @Override
    public void hasDown(String orderNo) {
        TaobaoTradeDO taobaoTradeDO = taoBaoTradeQryExe.getById(Long.valueOf(orderNo));
        if (taobaoTradeDO == null) {
            throw ThirdPartyException.buildException("找不到对应订单");
        }
        taobaoTradeDO.setStatus(TradeState.WAIT_BUYER_CONFIRM_GOODS.name());
        taoBaoTradeCmdExe.update(taobaoTradeDO);
    }
}
