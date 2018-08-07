package com.bat.goods.dao.stock;

import java.util.List;

import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsStockFlagDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsStockFlagDO record);

    int insertSelective(GoodsStockFlagDO record);

    GoodsStockFlagDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsStockFlagDO record);

    int updateByPrimaryKey(GoodsStockFlagDO record);

    void batchCreate(@Param("goodsStockFlagDOList") List<GoodsStockFlagDO> goodsStockFlagDOList);

    void batchUpdate(@Param("updateList") List<GoodsStockFlagDO> updateList);

    GoodsStockFlagDO getByItemIdAndWarehouseIdArr(@Param("itemId") Integer itemId,
        @Param("warehouseIdArr") String warehouseIdArr);

    void deleteByItemIdList(@Param("itemIdList") List<Integer> itemIdList);

    List<GoodsStockFlagDO> listByItemIds(@Param("itemIds") List<Integer> itemIds);
}