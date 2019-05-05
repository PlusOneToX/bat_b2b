package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillScalePriceRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupSeckillScalePriceRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillScalePriceRelevanceNoDO record);

    int insertSelective(GroupSeckillScalePriceRelevanceNoDO record);

    GroupSeckillScalePriceRelevanceNoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillScalePriceRelevanceNoDO record);

    int updateByPrimaryKey(GroupSeckillScalePriceRelevanceNoDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createScalePriceRelevanceNoList(List<GroupSeckillScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS);

    List<Integer> listScalePriceRelevanceNoIdByGroupSeckillId(Integer groupSeckillId);
}