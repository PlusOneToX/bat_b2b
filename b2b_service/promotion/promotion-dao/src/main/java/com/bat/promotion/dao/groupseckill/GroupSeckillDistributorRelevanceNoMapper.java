package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDistributorRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupSeckillDistributorRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillDistributorRelevanceNoDO record);

    int insertSelective(GroupSeckillDistributorRelevanceNoDO record);

    GroupSeckillDistributorRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillDistributorRelevanceNoDO record);

    int updateByPrimaryKey(GroupSeckillDistributorRelevanceNoDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createDistributorRelevanceNoList(List<GroupSeckillDistributorRelevanceNoDO> distributorRelevanceNoDOS);

    List<Integer> listDistributorRelevanceNoIdByGroupSeckillId(Integer groupSeckillId);
}