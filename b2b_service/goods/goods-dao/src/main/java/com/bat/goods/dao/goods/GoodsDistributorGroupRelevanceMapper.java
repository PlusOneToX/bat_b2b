package com.bat.goods.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.GoodsDistributorGroupRelevanceDO;

@Mapper
public interface GoodsDistributorGroupRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByGoodsId(@Param("goodsId") Integer goodsId);

    int insert(GoodsDistributorGroupRelevanceDO record);

    int insertList(List<GoodsDistributorGroupRelevanceDO> records);

    GoodsDistributorGroupRelevanceDO selectByPrimaryKey(Integer id);

    List<GoodsDistributorGroupRelevanceDO> selectAll();

    List<Integer> listDistributorGroupIdByGoodsId(@Param("goodsId") Integer goodsId);

    int updateByPrimaryKey(GoodsDistributorGroupRelevanceDO record);
}