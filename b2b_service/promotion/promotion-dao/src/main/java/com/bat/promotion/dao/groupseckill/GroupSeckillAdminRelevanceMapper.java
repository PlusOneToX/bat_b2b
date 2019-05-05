package com.bat.promotion.dao.groupseckill;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillAdminRelevanceDO;

@Mapper
public interface GroupSeckillAdminRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillAdminRelevanceDO record);

    int insertSelective(GroupSeckillAdminRelevanceDO record);

    GroupSeckillAdminRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillAdminRelevanceDO record);

    int updateByPrimaryKey(GroupSeckillAdminRelevanceDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createAdminRelevanceList(List<GroupSeckillAdminRelevanceDO> adminRelevanceDOS);

    List<Integer> listAdminRelevanceIdByGroupSeckillId(Integer groupSeckillId);
}