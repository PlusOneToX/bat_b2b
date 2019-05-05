package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoTradeDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaoBaoTradeCmdExe {

    @Autowired
    private TaobaoTradeDOMapper taobaoTradeDOMapper;

    public void update(TaobaoTradeDO taobaoTradeDO) {
        taobaoTradeDOMapper.updateByPrimaryKey(taobaoTradeDO);
    }

    public void save(TaobaoTradeDO taobaoTradeDO) {
        taobaoTradeDOMapper.insert(taobaoTradeDO);
    }
}
