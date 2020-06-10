package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDOKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureCategoryThemeRelevanceDOMapper {
    int deleteByPrimaryKey(PictureCategoryThemeRelevanceDOKey key);

    int insert(PictureCategoryThemeRelevanceDO record);

    int insertSelective(PictureCategoryThemeRelevanceDO record);

    PictureCategoryThemeRelevanceDO selectByPrimaryKey(PictureCategoryThemeRelevanceDOKey key);

    int updateByPrimaryKeySelective(PictureCategoryThemeRelevanceDO record);

    int updateByPrimaryKey(PictureCategoryThemeRelevanceDO record);

    void deleteById(@Param("id") Integer id);

    List<PictureCategoryThemeRelevanceDO> listByDistributorIdsAndCategoryType(@Param("distributorIds") List<Integer> distributorIds, @Param("type")Short type);
}