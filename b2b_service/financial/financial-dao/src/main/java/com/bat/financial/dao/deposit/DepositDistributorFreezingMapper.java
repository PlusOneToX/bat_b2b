package com.bat.financial.dao.deposit;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.deposit.dataobject.DepositDistributorFreezingDO;
import org.apache.ibatis.annotations.Param;

public interface DepositDistributorFreezingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepositDistributorFreezingDO record);

    int insertSelective(DepositDistributorFreezingDO record);

    DepositDistributorFreezingDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepositDistributorFreezingDO record);

    int updateByPrimaryKey(DepositDistributorFreezingDO record);

    DepositDistributorFreezingDO getByPrimaryKeyFreezing(Integer id);

    List<DepositDistributorFreezingDO> listFreezingByDistributorId(@Param("distributorName") String distributorName,
        @Param("distributorId") List<Integer> distributorId);

    List<DepositDistributorFreezingDO> selectByParams(@Param("params") Map<String, Object> map);
}