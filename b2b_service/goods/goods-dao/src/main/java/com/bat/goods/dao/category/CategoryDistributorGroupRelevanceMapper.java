package com.bat.goods.dao.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.category.dataobject.CategoryDistributorGroupRelevanceDO;

@Mapper
public interface CategoryDistributorGroupRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByCategoryId(@Param("categoryId") Integer categoryId);

    int insert(CategoryDistributorGroupRelevanceDO record);

    int insertList(List<CategoryDistributorGroupRelevanceDO> records);

    CategoryDistributorGroupRelevanceDO selectByPrimaryKey(Integer id);

    List<Integer> listDistributorGroupIdByCategoryId(@Param("categoryId") Integer categoryId);

    List<CategoryDistributorGroupRelevanceDO> selectAll();

    int updateByPrimaryKey(CategoryDistributorGroupRelevanceDO record);
}