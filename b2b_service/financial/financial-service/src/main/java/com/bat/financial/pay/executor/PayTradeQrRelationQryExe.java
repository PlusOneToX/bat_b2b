package com.bat.financial.pay.executor;

import javax.annotation.Resource;

import com.bat.financial.dao.pay.PayTradeQrRelationMapper;
import com.bat.financial.dao.pay.dataobject.PayTradeQrRelationDO;
import org.springframework.stereotype.Component;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/6 12:26
 */
@Component
public class PayTradeQrRelationQryExe {

    @Resource
    private PayTradeQrRelationMapper mapper;

    public PayTradeQrRelationDO getByOutTradeNo(String outTradeNo) {
        return mapper.getByOutTradeNo(outTradeNo);
    }
}
