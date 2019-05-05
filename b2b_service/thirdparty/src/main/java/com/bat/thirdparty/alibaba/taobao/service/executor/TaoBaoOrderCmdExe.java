package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoOrderDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaoBaoOrderCmdExe {

    @Autowired
    private TaobaoOrderDOMapper taobaoOrderDOMapper;

    public void save(TaobaoOrderDO taobaoOrderDO){
        taobaoOrderDOMapper.insert(taobaoOrderDO);
    }
}
