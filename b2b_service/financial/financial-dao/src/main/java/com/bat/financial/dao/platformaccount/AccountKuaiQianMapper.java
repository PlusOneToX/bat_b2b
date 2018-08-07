package com.bat.financial.dao.platformaccount;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.platformaccount.dataobject.AccountKuaiQianDO;
import org.apache.ibatis.annotations.Param;

public interface AccountKuaiQianMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountKuaiQianDO record);

    int insertSelective(AccountKuaiQianDO record);

    AccountKuaiQianDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountKuaiQianDO record);

    int updateByPrimaryKeyWithBLOBs(AccountKuaiQianDO record);

    int updateByPrimaryKey(AccountKuaiQianDO record);

    AccountKuaiQianDO getByOrganizationId(@Param("organizationId") Integer organizationId);

    List<AccountKuaiQianDO> selectByParams(@Param("params") Map<String, Object> map);
}