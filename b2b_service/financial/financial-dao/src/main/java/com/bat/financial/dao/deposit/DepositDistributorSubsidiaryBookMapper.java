package com.bat.financial.dao.deposit;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.deposit.dataobject.DepositDistributorSubsidiaryBookDO;
import org.apache.ibatis.annotations.Param;

public interface DepositDistributorSubsidiaryBookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DepositDistributorSubsidiaryBookDO record);

    int insertSelective(DepositDistributorSubsidiaryBookDO record);

    DepositDistributorSubsidiaryBookDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DepositDistributorSubsidiaryBookDO record);

    int updateByPrimaryKey(DepositDistributorSubsidiaryBookDO record);

    void insertBatch(@Param("dos") List<DepositDistributorSubsidiaryBookDO> dos);

    List<DepositDistributorSubsidiaryBookDO> selectByParams(@Param("params") Map<String, Object> map);

}