package com.bat.financial.dao.platformaccount;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.platformaccount.dataobject.AccountAlipayDO;

public interface AccountAlipayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountAlipayDO record);

    int insertSelective(AccountAlipayDO record);

    AccountAlipayDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountAlipayDO record);

    int updateByPrimaryKeyWithBLOBs(AccountAlipayDO record);

    int updateByPrimaryKey(AccountAlipayDO record);

    AccountAlipayDO getByOrganizationId(Integer organizationId);

    List<AccountAlipayDO> selectByParams(@Param("params") Map<String, Object> map);
}