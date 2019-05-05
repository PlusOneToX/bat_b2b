package com.bat.promotion.dao.groupseckill;

import java.util.List;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsSortDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsDO;

@Mapper
public interface GroupSeckillGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillGoodsDO record);

    void insertList(List<GroupSeckillGoodsDO> records);

    int insertSelective(GroupSeckillGoodsDO record);

    GroupSeckillGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillGoodsDO record);

    int updateByPrimaryKey(GroupSeckillGoodsDO record);

    int updateRealSumList(List<GroupSeckillGoodsDO> records);

    void deleteByGroupSeckillId(Integer groupSeckillId);

    List<GroupSeckillGoodsDO> listByGroupSeckillId(Integer groupSeckillId);

    List<GroupSeckillGoodsDO> listByGroupSeckillIdsAndItemIds(@Param("groupSeckillIds") List<Integer> groupSeckillIds,
        @Param("itemIds") List<Integer> itemIds);

    List<Integer> groupGoodsIdsByGroupSeckillIds(@Param("groupSeckillIds") List<Integer> groupSeckillIds);

    List<Integer> seckillGoodsIdsByGroupSeckillIds(@Param("groupSeckillIds") List<Integer> groupSeckillIds);

    void updateGroupSeckillGoodsSort(List<GroupSeckillGoodsSortDO> goodsSortDOS);

    List<UserGoodsItemGroupSeckillDO> listGroupSeckillGoodsByGroupSeckillIdsAndGoodsIds(
        @Param("groupSeckillIds") List<Integer> groupSeckillIds, @Param("goodsIds") List<Integer> goodsIds);

    List<Integer> groupActivities();
}