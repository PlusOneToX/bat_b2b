package com.bat.flexible.dao.font;

import com.bat.flexible.dao.font.co.FontCO;
import com.bat.flexible.dao.font.dataobject.FontDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FontDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FontDO record);

    int insertSelective(FontDO record);

    FontDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FontDO record);

    int updateByPrimaryKey(FontDO record);

    List<FontCO> listCOByOpenFlagAndContent(@Param("openFlag") Short openFlag, @Param("content") String content);

    Integer findMaxSequence();


    FontDO getEffect(@Param("sequence") Integer sequence,@Param("flag") Boolean flag);
}