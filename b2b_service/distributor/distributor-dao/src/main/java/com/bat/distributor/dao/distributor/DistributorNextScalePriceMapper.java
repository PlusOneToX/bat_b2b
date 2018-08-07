package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorNextScalePriceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorNextScalePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorNextScalePriceDO record);

    DistributorNextScalePriceDO selectByPrimaryKey(Integer id);

    List<DistributorNextScalePriceDO> selectAll();

    int updateByPrimaryKey(DistributorNextScalePriceDO record);

    DistributorNextScalePriceDO selectBydistributorId(Integer distributorId);

    List<DistributorNextScalePriceDO> listByNextScalePriceId(Integer nextScalePriceId);

    void deleteByDistributorId(Integer distributorId);
}