package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureMateriaRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureMaterialRelevanceDO record);

    int insertSelective(PictureMaterialRelevanceDO record);

    PictureMaterialRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureMaterialRelevanceDO record);

    int updateByPrimaryKey(PictureMaterialRelevanceDO record);

    List<PictureMaterialRelevanceDO> listByPictureIdAndDelFlag(@Param("pictureId") Integer pictureId,@Param("delFlag") Short delFlag);

    List<PictureMaterialRelevanceDO> listByPictureIdAndMaterialId(@Param("pictureId") Integer pictureId,@Param("materialId") Integer materialId);

    void batchUpdate(@Param("updateList") List<PictureMaterialRelevanceDO> updateList);
}