package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureLabelRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureLabelRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureLabelRelevanceDO record);

    int insertSelective(PictureLabelRelevanceDO record);

    PictureLabelRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureLabelRelevanceDO record);

    int updateByPrimaryKey(PictureLabelRelevanceDO record);



    List<PictureLabelRelevanceDO> listByLabelIdAndPictureId(@Param("labelId") Integer labelId,@Param("pictureId") Integer pictureId);

    void batchUpdate(@Param("updateList") List<PictureLabelRelevanceDO> updateList);
}