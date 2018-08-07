package com.bat.order.dao.cart;

import java.util.List;
import java.util.Map;

import com.bat.order.dao.cart.dataobject.ShoppingCartDistributorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCartDistributorDO record);

    void insertList(List<ShoppingCartDistributorDO> records);

    int insertSelective(ShoppingCartDistributorDO record);

    ShoppingCartDistributorDO selectByPrimaryKey(Integer id);

    List<ShoppingCartDistributorDO> listByMap(Map map);

    int updateByPrimaryKeySelective(ShoppingCartDistributorDO record);

    int updateByPrimaryKey(ShoppingCartDistributorDO record);

    int updateList(List<ShoppingCartDistributorDO> records);

    void update(Map map);

    void deleteByIds(List<Integer> ids);

    List<ShoppingCartDistributorDO> listByDistributorId(Integer distributorId);

    void updateListOpenFlag(@Param("goodsIds") List<Integer> goodsIds, @Param("openFlag") Short OpenFlag);

    void updateListOpenFlagByItemIds(@Param("itemIds") List<Integer> itemIds, @Param("openFlag") Short OpenFlag);
}