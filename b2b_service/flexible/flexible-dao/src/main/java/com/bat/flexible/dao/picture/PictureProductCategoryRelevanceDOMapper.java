package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureProductCategoryRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureProductCategoryRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureProductCategoryRelevanceDO record);

    int insertSelective(PictureProductCategoryRelevanceDO record);

    PictureProductCategoryRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureProductCategoryRelevanceDO record);

    int updateByPrimaryKey(PictureProductCategoryRelevanceDO record);

    List<PictureProductCategoryRelevanceDO> listByPictureId(@Param("pictureId") Integer pictureId);

    List<PictureProductCategoryRelevanceDO> listByCondition(@Param("pictureId") Integer pictureId,@Param("categoryId") Integer categoryId,
                                                            @Param("pictureIdList")List<Integer> pictureIdList);
}