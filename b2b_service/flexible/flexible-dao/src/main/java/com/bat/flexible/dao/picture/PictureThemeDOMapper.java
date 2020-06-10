package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureThemeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureThemeDO record);

    int insertSelective(PictureThemeDO record);

    PictureThemeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureThemeDO record);

    int updateByPrimaryKey(PictureThemeDO record);

    List<PictureThemeDO> listByOpenFlagAndContent(@Param("openFlag") Short openFlag,@Param("content") String content);
}