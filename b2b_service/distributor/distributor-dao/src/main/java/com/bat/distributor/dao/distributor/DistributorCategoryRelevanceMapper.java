package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorCategoryRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorCategoryRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorCategoryRelevanceDO record);

    DistributorCategoryRelevanceDO selectByPrimaryKey(Integer id);

    DistributorCategoryRelevanceDO selectByDistributorId(Integer distributorId);

    List<DistributorCategoryRelevanceDO> selectAll();

    int updateByPrimaryKey(DistributorCategoryRelevanceDO record);

    void deleteByDistributorId(Integer distributorId);

    List<DistributorCategoryRelevanceDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorCategoryRelevanceDO> listByCategoryId(String categoryId);

    List<Integer> listDistributorIdsByCategoryId(String categoryId);

    void updateList(List<DistributorCategoryRelevanceDO> relevanceDOS);

    void insertList(List<DistributorCategoryRelevanceDO> relevanceDOS);

    int deleteByIds(@Param("ids") List<Integer> ids);
}