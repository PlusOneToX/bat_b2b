package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoTradeOrderRelevanceDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeOrderRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaoBaoTradeOrderRelevanceQryExe {

    @Autowired
    private TaobaoTradeOrderRelevanceDOMapper taobaoTradeOrderRelevanceDOMapper;

    public List<TaobaoTradeOrderRelevanceDO> findByTid(Long tid) {
       return taobaoTradeOrderRelevanceDOMapper.findByTid(tid);
    }
}
