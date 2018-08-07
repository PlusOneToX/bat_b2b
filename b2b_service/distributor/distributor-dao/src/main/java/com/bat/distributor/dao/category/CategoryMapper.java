package com.bat.distributor.dao.category;

import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.category.dataobject.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(CategoryDO record);

    CategoryDO selectByPrimaryKey(@Param("id") Integer id);

    List<CategoryDO> selectAll();

    int updateByPrimaryKey(CategoryDO record);

    Integer getCategoryDistributorsCount(@Param("categoryId") Integer categoryId);

    void openCategory(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<CategoryDO> listCategory(Map map);

}