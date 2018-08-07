package com.bat.distributor.dao.subaccount;

import com.bat.distributor.dao.subaccount.co.SubAccountUserConfigCO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorSubAccountUserConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSubAccountUserConfigDO record);

    int insertSelective(DistributorSubAccountUserConfigDO record);

    DistributorSubAccountUserConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSubAccountUserConfigDO record);

    int updateByPrimaryKey(DistributorSubAccountUserConfigDO record);

    List<SubAccountUserConfigCO> listCOByDistributorId(@Param("distributorId") String distributorId);

    List<SubAccountUserConfigCO> listCOByCondition(@Param("searchType") Short searchType,@Param("content") String content,@Param("idList")List<Integer> idList);

    List<DistributorSubAccountUserConfigDO> listByCondition(@Param("distributorId") Integer distributorId);
}