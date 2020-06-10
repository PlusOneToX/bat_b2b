package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureModelRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureModelRelevanceDO record);

    int insertSelective(PictureModelRelevanceDO record);

    PictureModelRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureModelRelevanceDO record);

    int updateByPrimaryKey(PictureModelRelevanceDO record);

    List<PictureModelRelevanceDO> listByCondition(@Param("pictureId") Integer pictureId, @Param("modelId")Integer modelId,@Param("pictureIdList")List<Integer> pictureIdList);


}