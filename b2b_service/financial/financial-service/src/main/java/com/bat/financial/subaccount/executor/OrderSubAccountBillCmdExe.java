package com.bat.financial.subaccount.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.financial.dao.subaccount.OrderSubAccountBillDOMapper;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;

@Component
public class OrderSubAccountBillCmdExe {

    @Autowired
    private OrderSubAccountBillDOMapper orderSubAccountBillDOMapper;

    public void batchCreate(List<OrderSubAccountBillDO> list) {
        orderSubAccountBillDOMapper.batchCreate(list);
    }

    public void batchUpdate(List<OrderSubAccountBillDO> billDOList) {
        orderSubAccountBillDOMapper.batchUpdate(billDOList);
    }

    public void update(OrderSubAccountBillDO accountBillDO) {
        orderSubAccountBillDOMapper.updateByPrimaryKey(accountBillDO);
    }
}
