package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillScalePriceRelevanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupSeckillScalePriceRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillScalePriceRelevanceDO record);

    int insertSelective(GroupSeckillScalePriceRelevanceDO record);

    GroupSeckillScalePriceRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillScalePriceRelevanceDO record);

    int updateByPrimaryKey(GroupSeckillScalePriceRelevanceDO record);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    void createScalePriceRelevanceList(List<GroupSeckillScalePriceRelevanceDO> scalePriceRelevanceDOS);

    List<Integer> listScalePriceRelevanceIdByGroupSeckillId(Integer groupSeckillId);
}