package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorNameDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorOneScalePriceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorOneScalePriceMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);

    int insert(DistributorOneScalePriceDO record);

    void insertList(List<DistributorOneScalePriceDO> doList);

    List<DistributorOneScalePriceDO> listByDistributorId(Integer distributorId);

    List<DistributorOneScalePriceDO> listByBrandId(Integer brandId);

    DistributorOneScalePriceDO selectByPrimaryKey(Integer id);

    List<DistributorOneScalePriceDO> listDefaultByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    DistributorOneScalePriceDO selectDefaultByDistributorId(Integer distributorId);

    List<DistributorOneScalePriceDO> selectAll();

    int updateByPrimaryKey(DistributorOneScalePriceDO record);

    void updateList(List<DistributorOneScalePriceDO> records);

    List<Integer> listIdByScalePriceIdsAndBrandId(@Param("ids") List<Integer> ids, @Param("brandId") Integer brandId);

    List<Integer> listDistributorIdByScalePriceIdsTwo(@Param("ids") List<Integer> ids);

    List<Integer> listDefaultByScalePriceIds(@Param("ids") List<Integer> ids);

    List<DistributorNameDO> listDistributorNameDefaultByScalePriceIds(@Param("ids")List<Integer> ids);

    void deleteByDistributorIdsAndBrandId(@Param("distributorIds") List<Integer> distributorIds,
        @Param("brandId") Integer brandId);

    void deleteByDistributorId(Integer distributorId);


}