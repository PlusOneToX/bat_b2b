package com.bat.financial.pay.executor;

import java.util.List;

import com.bat.financial.dao.pay.PayBillsCustomerMapper;
import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayBillsCustomerQryExe {

    @Autowired
    private PayBillsCustomerMapper payBillsCustomerMapper;

    public List<PayBillsCustomerDO> listByOnlineTradeNo(String onlineTradeNo) {
        return payBillsCustomerMapper.listByOnlineTradeNo(onlineTradeNo);
    }
}
