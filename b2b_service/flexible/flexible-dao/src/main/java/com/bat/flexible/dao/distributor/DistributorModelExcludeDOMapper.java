package com.bat.flexible.dao.distributor;

import com.bat.flexible.dao.distributor.dataobject.DistributorModelExcludeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DistributorModelExcludeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorModelExcludeDO record);

    int insertSelective(DistributorModelExcludeDO record);

    DistributorModelExcludeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorModelExcludeDO record);

    int updateByPrimaryKey(DistributorModelExcludeDO record);

    List<DistributorModelExcludeDO> listByModelId(@Param("modelId") Integer modelId);

    List<DistributorModelExcludeDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);
}