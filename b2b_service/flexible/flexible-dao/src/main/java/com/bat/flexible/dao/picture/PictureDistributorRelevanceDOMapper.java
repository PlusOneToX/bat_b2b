package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PictureDistributorRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureDistributorRelevanceDO record);

    int insertSelective(PictureDistributorRelevanceDO record);

    PictureDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureDistributorRelevanceDO record);

    int updateByPrimaryKey(PictureDistributorRelevanceDO record);

    List<PictureDistributorRelevanceDO> listByPictureIdAAndDelFlag(@Param("pictureId") Integer pictureId, @Param("delFlag") Short delFlag);

    List<PictureDistributorRelevanceDO> listByCondition(@Param("pictureId")Integer pictureId, @Param("distributorIds") List<Integer> distributorIds);
}