package com.bat.order.dao.cart;

import java.util.List;

import com.bat.order.dao.cart.dataobject.ShoppingCartDiyDistributorDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartDiyDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCartDiyDistributorDO record);

    void insertList(List<ShoppingCartDiyDistributorDO> records);

    int insertSelective(ShoppingCartDiyDistributorDO record);

    ShoppingCartDiyDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCartDiyDistributorDO record);

    int updateByPrimaryKey(ShoppingCartDiyDistributorDO record);

    void deleteByCartId(Integer cartId);

    void deleteByCartIds(List<Integer> cartIds);

    List<ShoppingCartDiyDistributorDO> listByCartIds(List<Integer> cartIds);
}