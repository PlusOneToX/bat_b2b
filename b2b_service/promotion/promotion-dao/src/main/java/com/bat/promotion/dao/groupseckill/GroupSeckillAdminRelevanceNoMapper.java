package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillAdminRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupSeckillAdminRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillAdminRelevanceNoDO record);

    int insertSelective(GroupSeckillAdminRelevanceNoDO record);

    GroupSeckillAdminRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillAdminRelevanceNoDO record);

    int updateByPrimaryKey(GroupSeckillAdminRelevanceNoDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createAdminRelevanceNoList(List<GroupSeckillAdminRelevanceNoDO> adminRelevanceNoDOS);

    List<Integer> listAdminRelevanceNoIdByGroupSeckillId(Integer groupSeckillId);
}