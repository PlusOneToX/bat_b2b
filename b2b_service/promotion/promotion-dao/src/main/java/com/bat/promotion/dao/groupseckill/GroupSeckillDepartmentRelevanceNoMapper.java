package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDepartmentRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupSeckillDepartmentRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillDepartmentRelevanceNoDO record);

    int insertSelective(GroupSeckillDepartmentRelevanceNoDO record);

    GroupSeckillDepartmentRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillDepartmentRelevanceNoDO record);

    int updateByPrimaryKey(GroupSeckillDepartmentRelevanceNoDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createDepartmentRelevanceNoList(List<GroupSeckillDepartmentRelevanceNoDO> departmentRelevanceNoDOS);

    List<Integer> listDepartmentRelevanceNoIdByGroupSeckillId(Integer groupSeckillId);
}