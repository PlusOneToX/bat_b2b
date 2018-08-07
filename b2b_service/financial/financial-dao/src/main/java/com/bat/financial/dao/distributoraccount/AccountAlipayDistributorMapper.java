package com.bat.financial.dao.distributoraccount;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.distributoraccount.dataobject.AccountAlipayDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface AccountAlipayDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountAlipayDistributorDO record);

    int insertSelective(AccountAlipayDistributorDO record);

    AccountAlipayDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountAlipayDistributorDO record);

    int updateByPrimaryKeyWithBLOBs(AccountAlipayDistributorDO record);

    int updateByPrimaryKey(AccountAlipayDistributorDO record);

    AccountAlipayDistributorDO getByDistributorId(Integer distributorId);

    List<AccountAlipayDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

    void insertBatch(@Param("dos") List<AccountAlipayDistributorDO> dos);

    void deleteByAppid(String appId);

    List<AccountAlipayDistributorDO> listByIds(@Param("ids") List<Integer> ids);

    List<AccountAlipayDistributorDO> listByAppId(String id);

    void deleteByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);
}