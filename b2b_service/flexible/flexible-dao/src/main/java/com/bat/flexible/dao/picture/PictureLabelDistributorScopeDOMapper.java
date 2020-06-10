package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureLabelDistributorScopeDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PictureLabelDistributorScopeDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PictureLabelDistributorScopeDO record);

    int insertSelective(PictureLabelDistributorScopeDO record);

    PictureLabelDistributorScopeDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PictureLabelDistributorScopeDO record);

    int updateByPrimaryKey(PictureLabelDistributorScopeDO record);
}