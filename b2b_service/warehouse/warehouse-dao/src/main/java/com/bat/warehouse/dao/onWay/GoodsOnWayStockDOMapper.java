package com.bat.warehouse.dao.onWay;

import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsOnWayStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOnWayStockDO record);

    int insertSelective(GoodsOnWayStockDO record);

    GoodsOnWayStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOnWayStockDO record);

    int updateByPrimaryKey(GoodsOnWayStockDO record);

    GoodsOnWayStockDO findByItemId(@Param("itemId")Integer itemId);

    List<GoodsOnWayStockDO> findByItemIds(@Param("itemIds")List<Integer> itemIds);

    void deleteByItemId(@Param("itemId")Integer itemId);

    List<GoodsOnWayStockDO> listAll();

    void updateList(@Param("updateList") List<GoodsOnWayStockDO> goodsOnWayStockDOList);
}