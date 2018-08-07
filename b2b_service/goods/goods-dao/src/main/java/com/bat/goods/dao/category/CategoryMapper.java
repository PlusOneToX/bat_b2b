package com.bat.goods.dao.category;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.category.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.category.dataobject.*;

@Mapper
public interface CategoryMapper {

    Integer createCategory(CategoryDO categoryDO);

    CategoryDO getById(@Param("id") Integer id);

    Integer updateCategory(CategoryDO categoryDO);

    void openCategory(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    void deleteCategory(@Param("id") Integer id);

    List<CategoryDO> listCategory(Map map);

    List<Integer> listByAllDistributor();

    Integer listCount(@Param("content") String content);

    void createCategoryScalePriceRelevanceList(List<CategoryScalePriceRelevanceDO> scalePriceRelevanceDOS);

    List<Integer> listCategoryScalePriceRelevanceId(@Param("categoryId") Integer categoryId);

    void deleteCategoryScalePriceRelevance(@Param("categoryId") Integer categoryId);

    void createCategoryScalePriceRelevanceNoList(List<CategoryScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS);

    List<Integer> listCategoryScalePriceRelevanceIdNo(@Param("categoryId") Integer categoryId);

    void deleteCategoryScalePriceRelevanceNo(@Param("categoryId") Integer categoryId);

    void createCategoryDepartmentRelevanceList(List<CategoryDepartmentRelevanceDO> departmentRelevanceDOS);

    List<Integer> listCategoryDepartmentRelevanceId(@Param("categoryId") Integer categoryId);

    void deleteCategoryDepartmentRelevance(@Param("categoryId") Integer categoryId);

    void createCategoryDepartmentRelevanceNoList(List<CategoryDepartmentRelevanceNoDO> departmentRelevanceNoDOS);

    List<Integer> listCategoryDepartmentRelevanceIdNo(@Param("categoryId") Integer categoryId);

    void deleteCategoryDepartmentRelevanceNo(@Param("categoryId") Integer categoryId);

    void createCategoryAdminRelevanceList(List<CategoryAdminRelevanceDO> adminRelevanceDOS);

    List<Integer> listCategoryAdminRelevanceId(@Param("categoryId") Integer categoryId);

    void deleteCategoryAdminRelevance(@Param("categoryId") Integer categoryId);

    void createCategoryAdminRelevanceNoList(List<CategoryAdminRelevanceNoDO> adminRelevanceNoDOS);

    List<Integer> listCategoryAdminRelevanceIdNo(@Param("categoryId") Integer categoryId);

    void deleteCategoryAdminRelevanceNo(@Param("categoryId") Integer categoryId);

    void createCategoryDistributorRelevanceList(List<CategoryDistributorRelevanceDO> distributorRelevanceDOS);

    List<Integer> listCategoryDistributorRelevanceId(@Param("categoryId") Integer categoryId);

    void deleteCategoryDistributorRelevance(@Param("categoryId") Integer categoryId);

    void createCategoryDistributorRelevanceNoList(List<CategoryDistributorRelevanceNoDO> distributorRelevanceNoDOS);

    List<Integer> listCategoryDistributorRelevanceIdNo(@Param("categoryId") Integer categoryId);

    void deleteCategoryDistributorRelevanceNo(@Param("categoryId") Integer categoryId);

    Integer getCategoryGoodsCount(@Param("categoryId") Integer categoryId);

}
