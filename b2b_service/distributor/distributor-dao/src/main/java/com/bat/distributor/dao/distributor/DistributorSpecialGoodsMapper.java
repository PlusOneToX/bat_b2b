package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorSpecialGoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorSpecialGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByIds(@Param("ids") List<Integer> ids);

    int insert(DistributorSpecialGoodsDO record);

    void insertList(List<DistributorSpecialGoodsDO> doList);

    DistributorSpecialGoodsDO selectByPrimaryKey(Integer id);

    List<DistributorSpecialGoodsDO> listByDistributorId(Integer distributorId);

    List<DistributorSpecialGoodsDO> listByDistributorIdAndGoodsItemIds(@Param("distributorId") Integer distributorId,
        @Param("goodsItemIds") List<Integer> goodsItemIds);

    List<DistributorSpecialGoodsDO> listByDistributorIdAndGoodsIds(@Param("distributorId") Integer distributorId,
        @Param("goodsIds") List<Integer> goodsIds);

    List<DistributorSpecialGoodsDO> selectAll();

    int updateByPrimaryKey(DistributorSpecialGoodsDO record);

    void updateList(List<DistributorSpecialGoodsDO> records);

    void deleteByDistributorId(Integer distributorId);
}