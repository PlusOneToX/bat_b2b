package com.bat.goods.dao.brand;

import java.util.List;

import com.bat.goods.dao.brand.dataobject.BrandDistributorGroupRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrandDistributorGroupRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByBrandId(@Param("brandId") Integer brandId);

    int insert(BrandDistributorGroupRelevanceDO record);

    int insertList(List<BrandDistributorGroupRelevanceDO> records);

    BrandDistributorGroupRelevanceDO selectByPrimaryKey(Integer id);

    List<BrandDistributorGroupRelevanceDO> selectAll();

    List<Integer> listDistributorGroupIdByBrandId(@Param("brandId") Integer brandId);

    int updateByPrimaryKey(BrandDistributorGroupRelevanceDO record);

    List<BrandDistributorGroupRelevanceDO> listByDistributorGroupIds(@Param("distributorGroupIds") List<Integer> distributorGroupIds);
}