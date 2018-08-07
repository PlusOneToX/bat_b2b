package com.bat.warehouse.dao.vmi;

import java.util.List;

import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsVmiStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsVmiStockDO record);

    void insertList(List<GoodsVmiStockDO> records);

    int insertSelective(GoodsVmiStockDO record);

    GoodsVmiStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsVmiStockDO record);

    int updateByPrimaryKey(GoodsVmiStockDO record);

    GoodsVmiStockDO findByItemId(@Param("itemId") Integer itemId);

    List<GoodsVmiStockDO> listByItemIds(@Param("itemIds") List<Integer> itemIds);

    List<GoodsVmiStockDO> listAll();

    void deleteByItemId(@Param("itemId") Integer itemId);

    void updateList(@Param("updateList") List<GoodsVmiStockDO> goodsVmiStockDOList);

    void deleteByIds(@Param("itemIds") List<Integer> itemIds);

    List<GoodsVmiStockDO> getNoItemVmiStock(@Param("itemIds") List<Integer> itemIds);
}