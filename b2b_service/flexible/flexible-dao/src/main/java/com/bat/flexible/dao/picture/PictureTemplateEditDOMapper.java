package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import com.bat.flexible.dao.picture.dataobject.PictureTemplateEditDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureTemplateEditDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureTemplateEditDO record);

    int insertSelective(PictureTemplateEditDO record);

    PictureTemplateEditDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureTemplateEditDO record);

    int updateByPrimaryKey(PictureTemplateEditDO record);

    List<PictureTemplateEditDO> listByPictureId(@Param("pictureId") Integer pictureId);


    List<PictureTemplateEditCmd> listSimpleByPictureId(@Param("pictureId")Integer pictureId);
}