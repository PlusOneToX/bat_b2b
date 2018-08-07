package com.bat.financial.subaccount.executor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.common.error.FinancialCommonErrorCode;
import com.bat.financial.dao.subaccount.OrderSubAccountBillDOMapper;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;

@Component
public class OrderSubAccountBillQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSubAccountBillQryExe.class);

    @Autowired
    private OrderSubAccountBillDOMapper orderSubAccountBillDOMapper;

    public List<OrderSubAccountBillDO> listByCondition(Integer orderSubAccountId, Short status) {
        return orderSubAccountBillDOMapper.listByCondition(orderSubAccountId, status);
    }

    public List<OrderSubAccountBillDO> listByIdList(List<Integer> billIdList) {
        List<OrderSubAccountBillDO> list = orderSubAccountBillDOMapper.listByIdList(billIdList);
        if (list == null || list.size() - billIdList.size() != 0) {
            LOGGER.error("流水id列表id错误{}", JSON.toJSONString(billIdList));
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_ID_ERROR);
        }
        return list;
    }

    public OrderSubAccountBillDO getById(Integer id) {
        if (id == null) {
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_ID_NULL);
        }
        OrderSubAccountBillDO accountBillDO = orderSubAccountBillDOMapper.selectByPrimaryKey(id);
        if (accountBillDO == null) {
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_ID_ERROR);
        }
        return accountBillDO;
    }
}
