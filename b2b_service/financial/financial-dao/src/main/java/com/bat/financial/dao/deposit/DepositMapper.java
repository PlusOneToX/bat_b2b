package com.bat.financial.dao.deposit;

import com.bat.financial.dao.deposit.dataobject.DepositDO;

public interface DepositMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepositDO record);

    int insertSelective(DepositDO record);

    DepositDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepositDO record);

    int updateByPrimaryKey(DepositDO record);
}