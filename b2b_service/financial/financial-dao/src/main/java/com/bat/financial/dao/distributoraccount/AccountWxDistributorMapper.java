package com.bat.financial.dao.distributoraccount;

import java.util.List;
import java.util.Map;

import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface AccountWxDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountWxDistributorDO record);

    int insertSelective(AccountWxDistributorDO record);

    AccountWxDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountWxDistributorDO record);

    int updateByPrimaryKey(AccountWxDistributorDO record);

    AccountWxDistributorDO getByDistributorId(Integer distributorId);

    List<AccountWxDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

    void insertBatch(@Param("dos") List<AccountWxDistributorDO> dos);

    void deleteByAppidAndSubMchid(@Param("appId") String appId, @Param("subMchid") String subMchid);

    List<AccountWxDistributorDO> listByIdsAndApptype(@Param("ids") List<Integer> ids, @Param("appType") Short appType);

    List<AccountWxDistributorDO> listByAppIdAndSubMchid(@Param("appId") String appId,
        @Param("subMchid") String subMchid);

    void deleteByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    AccountWxDistributorDO getByDistributorIdAndAppType(@Param("distributorId") Integer payeeId,
        @Param("appType") Short appType);

    List<AccountWxDistributorDO> listByCondition(@Param("distributorId") Integer distributorId,
        @Param("appType") Short appType, @Param("appId") String appId, @Param("accountType") Short accountType);
}