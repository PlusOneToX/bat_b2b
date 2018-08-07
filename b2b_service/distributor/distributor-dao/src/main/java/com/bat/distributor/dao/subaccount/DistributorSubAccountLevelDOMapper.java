package com.bat.distributor.dao.subaccount;

import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorSubAccountLevelDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSubAccountLevelDO record);

    int insertSelective(DistributorSubAccountLevelDO record);

    DistributorSubAccountLevelDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSubAccountLevelDO record);

    int updateByPrimaryKey(DistributorSubAccountLevelDO record);

    List<DistributorSubAccountLevelDO> listByDistributorId(@Param("distributorId") Integer distributorId);

    DistributorSubAccountLevelDO findParentLevel(@Param("levelId") Integer sonLevelId);


}