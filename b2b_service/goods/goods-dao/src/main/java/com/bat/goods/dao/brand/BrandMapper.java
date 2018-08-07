package com.bat.goods.dao.brand;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.brand.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.brand.dataobject.*;

@Mapper
public interface BrandMapper {

    Integer createBrand(BrandDO brandDO);

    void createBrands(List<BrandDO> brandDOs);

    BrandDO getById(@Param("id") Integer id);

    Integer updateBrand(BrandDO brandDO);

    void openBrand(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    void deleteBrand(@Param("id") Integer id);

    List<BrandDO> listBrand(Map map);

    List<BrandDO> listBrandByBrandIds(@Param("brandIds") List<Integer> brandIds);

    List<Integer> listByAllDistributor();

    List<UserBrandDO> listBrandByDistributorId(Map map);

    Integer listCount(@Param("content") String content);

    void createBrandScalePriceRelevanceList(List<BrandScalePriceRelevanceDO> scalePriceRelevanceDOS);

    List<Integer> listBrandScalePriceRelevanceId(@Param("brandId") Integer brandId);

    void deleteBrandScalePriceRelevance(@Param("brandId") Integer brandId);

    void createBrandScalePriceRelevanceNoList(List<BrandScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS);

    List<Integer> listBrandScalePriceRelevanceIdNo(@Param("brandId") Integer brandId);

    void deleteBrandScalePriceRelevanceNo(@Param("brandId") Integer brandId);

    void createBrandDepartmentRelevanceList(List<BrandDepartmentRelevanceDO> departmentRelevanceDOS);

    List<Integer> listBrandDepartmentRelevanceId(@Param("brandId") Integer brandId);

    void deleteBrandDepartmentRelevance(@Param("brandId") Integer brandId);

    void createBrandDepartmentRelevanceNoList(List<BrandDepartmentRelevanceNoDO> departmentRelevanceNoDOS);

    List<Integer> listBrandDepartmentRelevanceIdNo(@Param("brandId") Integer brandId);

    void deleteBrandDepartmentRelevanceNo(@Param("brandId") Integer brandId);

    void createBrandAdminRelevanceList(List<BrandAdminRelevanceDO> adminRelevanceDOS);

    List<Integer> listBrandAdminRelevanceId(@Param("brandId") Integer brandId);

    void deleteBrandAdminRelevance(@Param("brandId") Integer brandId);

    void createBrandAdminRelevanceNoList(List<BrandAdminRelevanceNoDO> adminRelevanceNoDOS);

    List<Integer> listBrandAdminRelevanceIdNo(@Param("brandId") Integer brandId);

    void deleteBrandAdminRelevanceNo(@Param("brandId") Integer brandId);

    void createBrandDistributorRelevanceList(List<BrandDistributorRelevanceDO> distributorRelevanceDOS);

    void createBrandDistributorRelevance(BrandDistributorRelevanceDO distributorRelevanceDO);

    List<Integer> listBrandDistributorRelevanceId(@Param("brandId") Integer brandId);

    List<Integer> listBrandIdByDistributorId(@Param("distributorId") Integer distributorId);

    void deleteBrandDistributorRelevance(@Param("brandId") Integer brandId);

    void deleteBrandDistributorRelevanceByDistributorId(@Param("distributorId") Integer distributorId);

    void createBrandDistributorRelevanceNoList(List<BrandDistributorRelevanceNoDO> distributorRelevanceNoDOS);

    List<Integer> listBrandDistributorRelevanceIdNo(@Param("brandId") Integer brandId);

    void deleteBrandDistributorRelevanceNo(@Param("brandId") Integer brandId);

    void deleteBrandDistributorRelevanceNoByDistributorId(@Param("brandId") Integer brandId,
        @Param("distributorId") Integer distributorId);

    void deleteBrandDistributorRelevanceByDistributorIdAndBrandId(@Param("brandId") Integer brandId,
        @Param("distributorId") Integer distributorId);

    Integer getBrandGoodsCount(@Param("brandId") Integer brandId);
}
