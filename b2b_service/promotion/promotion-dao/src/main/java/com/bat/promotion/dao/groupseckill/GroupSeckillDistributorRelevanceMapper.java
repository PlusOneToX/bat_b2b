package com.bat.promotion.dao.groupseckill;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDistributorRelevanceDO;

@Mapper
public interface GroupSeckillDistributorRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillDistributorRelevanceDO record);

    int insertSelective(GroupSeckillDistributorRelevanceDO record);

    GroupSeckillDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillDistributorRelevanceDO record);

    int updateByPrimaryKey(GroupSeckillDistributorRelevanceDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createDistributorRelevanceList(List<GroupSeckillDistributorRelevanceDO> distributorRelevanceDOS);

    List<GroupSeckillDistributorRelevanceDO> listDistributorRelevanceByGroupSeckillId(Integer groupSeckillId);
}