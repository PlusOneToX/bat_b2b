package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoOrderDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaoBaoOrderQryExe {

    @Autowired
    private TaobaoOrderDOMapper taobaoOrderDOMapper;

    public TaobaoOrderDO findOne(Long oid) {
        return taobaoOrderDOMapper.selectByPrimaryKey(oid);
    }
}
