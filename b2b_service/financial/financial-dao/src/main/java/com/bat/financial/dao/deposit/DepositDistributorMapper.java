package com.bat.financial.dao.deposit;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.deposit.dataobject.DepositDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface DepositDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepositDistributorDO record);

    int insertSelective(DepositDistributorDO record);

    DepositDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepositDistributorDO record);

    int updateByPrimaryKey(DepositDistributorDO record);

    void updateBatchByPrimaryKey(@Param("depositDistributor") List<DepositDistributorDO> dos);

    void insertBatch(@Param("dos") List<DepositDistributorDO> dos1);

    List<DepositDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

    DepositDistributorDO getByDistributorId(Integer distributorId);

    DepositDistributorDO getByErpDistributorId(@Param("erpDistributorId") String erpDistributorId);
}