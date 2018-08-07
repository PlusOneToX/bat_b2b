package com.bat.financial.subaccount.executor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.common.error.FinancialCommonErrorCode;
import com.bat.financial.dao.subaccount.OrderSubAccountDOMapper;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;

@Component
public class OrderSubAccountQryExe {

    @Autowired
    private OrderSubAccountDOMapper orderSubAccountDOMapper;

    public List<OrderSubAccountDO> pageByCondition(Integer distributorId, Short status, String orderNo,
        Short subAccountFailFlag, Date startTime, Date endTime, Short contentType, String content) {

        return orderSubAccountDOMapper.pageByCondition(distributorId, status, orderNo, subAccountFailFlag, startTime,
            endTime, contentType, content);
    }

    public OrderSubAccountDO getById(Integer id) {
        OrderSubAccountDO orderSubAccountDO = orderSubAccountDOMapper.selectByPrimaryKey(id);
        if (orderSubAccountDO == null) {
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_ID_ERROR);
        }
        return orderSubAccountDO;
    }

    /**
     * 查询到时间但还没关闭或者全部成功的
     * 
     * @return
     */
    public List<OrderSubAccountDO> listSubAccountUsable() {
        return orderSubAccountDOMapper.listSubAccountUsable();
    }
}
