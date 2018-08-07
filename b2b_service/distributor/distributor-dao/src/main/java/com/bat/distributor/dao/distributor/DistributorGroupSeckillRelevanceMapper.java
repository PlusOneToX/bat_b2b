package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorGroupSeckillRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorGroupSeckillRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorGroupSeckillRelevanceDO record);

    DistributorGroupSeckillRelevanceDO selectByPrimaryKey(Integer id);

    void deleteByDistributorId(Integer distributorId);

    List<DistributorGroupSeckillRelevanceDO> selectAll();

    int updateByPrimaryKey(DistributorGroupSeckillRelevanceDO record);

    List<DistributorGroupSeckillRelevanceDO>
        listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    DistributorGroupSeckillRelevanceDO selectByDistributorId(Integer distributorId);

    List<DistributorGroupSeckillRelevanceDO> listByGroupSeckillId(String groupSeckillId);

    void updateList(List<DistributorGroupSeckillRelevanceDO> relevanceDOS);

    void insertList(List<DistributorGroupSeckillRelevanceDO> relevanceDOS);

    int deleteByIds(@Param("ids") List<Integer> ids);
}