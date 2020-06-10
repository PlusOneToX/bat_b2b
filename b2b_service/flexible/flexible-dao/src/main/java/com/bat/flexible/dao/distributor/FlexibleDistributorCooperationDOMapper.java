package com.bat.flexible.dao.distributor;

import com.bat.flexible.dao.distributor.dataobject.FlexibleDistributorCooperationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlexibleDistributorCooperationDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FlexibleDistributorCooperationDO record);

    int insertSelective(FlexibleDistributorCooperationDO record);

    FlexibleDistributorCooperationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FlexibleDistributorCooperationDO record);

    int updateByPrimaryKey(FlexibleDistributorCooperationDO record);

    List<FlexibleDistributorCooperationDO> listByOpenFlagAndContent(@Param("openFlag") Short openFlag,@Param("content") String content);

    Integer findMaxSequence();
}