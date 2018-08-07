package com.bat.financial.dao.platformaccount;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.platformaccount.dataobject.AccountOfflineDO;
import org.apache.ibatis.annotations.Param;

public interface AccountOfflineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountOfflineDO record);

    int insertSelective(AccountOfflineDO record);

    AccountOfflineDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountOfflineDO record);

    int updateByPrimaryKey(AccountOfflineDO record);

    AccountOfflineDO getAccountByOrganization(Integer id);

    List<AccountOfflineDO> selectByParams(@Param("params") Map<String, Object> map);
}