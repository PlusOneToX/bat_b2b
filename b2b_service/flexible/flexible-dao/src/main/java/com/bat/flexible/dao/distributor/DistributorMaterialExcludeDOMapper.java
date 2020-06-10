package com.bat.flexible.dao.distributor;

import com.bat.flexible.dao.distributor.dataobject.DistributorMaterialExcludeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorMaterialExcludeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorMaterialExcludeDO record);

    int insertSelective(DistributorMaterialExcludeDO record);

    DistributorMaterialExcludeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorMaterialExcludeDO record);

    int updateByPrimaryKey(DistributorMaterialExcludeDO record);


    List<DistributorMaterialExcludeDO> listByMaterialIdAndDelFlag(@Param("materialId") Integer materialId,@Param("delFlag") Short delFlag);

    List<DistributorMaterialExcludeDO> listByDistributorIds(@Param("distributorIds")List<Integer> distributorIds);
}