package com.bat.financial.dao.deposit;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.deposit.dataobject.WithdrawDepositsDistributorApplyDO;
import org.apache.ibatis.annotations.Param;

public interface WithdrawDepositsDistributorApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawDepositsDistributorApplyDO record);

    int insertSelective(WithdrawDepositsDistributorApplyDO record);

    WithdrawDepositsDistributorApplyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawDepositsDistributorApplyDO record);

    int updateByPrimaryKey(WithdrawDepositsDistributorApplyDO record);

    List<WithdrawDepositsDistributorApplyDO> selectByParams(@Param("params") Map<String, Object> map);

    void deleteBatchByPrimaryKey(@Param("ids") List<Integer> ids);
}