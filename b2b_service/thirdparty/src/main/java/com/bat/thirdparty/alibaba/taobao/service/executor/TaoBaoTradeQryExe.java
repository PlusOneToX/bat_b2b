package com.bat.thirdparty.alibaba.taobao.service.executor;

import com.bat.thirdparty.alibaba.taobao.dao.TaobaoTradeDOMapper;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaoBaoTradeQryExe {

    @Autowired
    private TaobaoTradeDOMapper taobaoTradeDOMapper;

    public TaobaoTradeDO getById(Long tid) {
        return taobaoTradeDOMapper.selectByPrimaryKey(tid);
    }

    public List<TaobaoTradeDO> findBySellerNickAndStatus(String sellerNick, String name) {
        return taobaoTradeDOMapper.findBySellerNickAndStatus(sellerNick, name);
    }
}
