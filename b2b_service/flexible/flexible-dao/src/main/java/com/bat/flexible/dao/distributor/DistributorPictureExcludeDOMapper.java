package com.bat.flexible.dao.distributor;

import com.bat.flexible.dao.distributor.dataobject.DistributorPictureExcludeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorPictureExcludeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorPictureExcludeDO record);

    int insertSelective(DistributorPictureExcludeDO record);

    DistributorPictureExcludeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorPictureExcludeDO record);

    int updateByPrimaryKey(DistributorPictureExcludeDO record);

    List<DistributorPictureExcludeDO> listByPictureIdAndDelFlag(@Param("pictureId") Integer pictureId,@Param("delFlag") Short delFlag);

    List<DistributorPictureExcludeDO> listByDistributorId(@Param("distributorIds") List<Integer> distributorIds);
}