package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoTradeOrderRelevanceDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeOrderRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaoBaoTradeOrderRelevanceCmdExe {

    @Autowired
    private TaobaoTradeOrderRelevanceDOMapper taobaoTradeOrderRelevanceDOMapper;

    public void save(TaobaoTradeOrderRelevanceDO record){
        taobaoTradeOrderRelevanceDOMapper.insert(record);
    }
}
