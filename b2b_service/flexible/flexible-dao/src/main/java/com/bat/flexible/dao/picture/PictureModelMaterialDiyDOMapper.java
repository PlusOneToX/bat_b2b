package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureModelMaterialDiyDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureModelMaterialDiyDO record);

    int insertSelective(PictureModelMaterialDiyDO record);

    PictureModelMaterialDiyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureModelMaterialDiyDO record);

    int updateByPrimaryKey(PictureModelMaterialDiyDO record);

    PictureModelMaterialDiyDO getByMaterialIdAndModelIdAndPictureId(@Param("materialId") Integer materialId,@Param("modelId") Integer modelId,@Param("pictureId") Integer pictureId);

    List<PictureModelMaterialDiyDO> listByCondition(@Param("materialId") Integer materialId,@Param("modelId") Integer modelId,@Param("pictureId") Integer pictureId);
}