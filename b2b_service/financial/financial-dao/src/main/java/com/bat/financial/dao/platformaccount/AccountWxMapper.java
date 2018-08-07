package com.bat.financial.dao.platformaccount;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.platformaccount.dataobject.AccountWxDO;

public interface AccountWxMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountWxDO record);

    int insertSelective(AccountWxDO record);

    AccountWxDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountWxDO record);

    int updateByPrimaryKeyWithBLOBs(AccountWxDO record);

    int updateByPrimaryKey(AccountWxDO record);

    AccountWxDO getAccountByOrganizationAndAppType(@Param("organizationId") Integer organizationId,
        @Param("appType") Short appType);

    List<AccountWxDO> selectByParams(@Param("params") Map<String, Object> map);

    AccountWxDO getByDistributorIdAndAppType(@Param("organizationId") Integer organizationId,
        @Param("appType") Short appType);
}