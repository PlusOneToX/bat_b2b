package com.bat.promotion.dao.groupseckill;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDepartmentRelevanceDO;

@Mapper
public interface GroupSeckillDepartmentRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillDepartmentRelevanceDO record);

    int insertSelective(GroupSeckillDepartmentRelevanceDO record);

    GroupSeckillDepartmentRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillDepartmentRelevanceDO record);

    int updateByPrimaryKey(GroupSeckillDepartmentRelevanceDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createDepartmentRelevanceList(List<GroupSeckillDepartmentRelevanceDO> departmentRelevanceDOS);

    List<Integer> listDepartmentRelevanceIdByGroupSeckillId(Integer groupSeckillId);
}