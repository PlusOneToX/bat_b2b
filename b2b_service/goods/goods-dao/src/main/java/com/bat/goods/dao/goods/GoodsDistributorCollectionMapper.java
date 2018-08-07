package com.bat.goods.dao.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsDistributorCollectionDO;

@Mapper
public interface GoodsDistributorCollectionMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByMap(Map map);

    void deleteByGoodsId(@Param("goodsId") Integer goodsId);

    int insert(GoodsDistributorCollectionDO record);

    GoodsDistributorCollectionDO selectByPrimaryKey(Integer id);

    List<GoodsDistributorCollectionDO> selectAll();

    List<Integer> listByGoodsIdsAndDistributorId(@Param("goodsIds") List<Integer> goodsIds,
        @Param("distributorId") Integer distributorId);

    Integer listByGoodsIdAndDistributorId(@Param("goodsId") Integer goodsId,
        @Param("distributorId") Integer distributorId);

    int updateByPrimaryKey(GoodsDistributorCollectionDO record);
}