package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorGoodsRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorGoodsRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorGoodsRelevanceDO record);

    DistributorGoodsRelevanceDO selectByPrimaryKey(Integer id);

    List<DistributorGoodsRelevanceDO> selectAll();

    int updateByPrimaryKey(DistributorGoodsRelevanceDO record);

    void deleteByDistributorId(Integer distributorId);

    List<DistributorGoodsRelevanceDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    DistributorGoodsRelevanceDO selectByDistributorId(Integer distributorId);

    List<DistributorGoodsRelevanceDO> listByGoodsId(String goodsId);

    void updateList(List<DistributorGoodsRelevanceDO> relevanceDOS);

    void insertList(List<DistributorGoodsRelevanceDO> relevanceDOS);

    int deleteByIds(@Param("ids") List<Integer> ids);
}