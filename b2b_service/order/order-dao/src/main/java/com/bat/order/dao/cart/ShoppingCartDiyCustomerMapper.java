package com.bat.order.dao.cart;

import com.bat.order.dao.cart.dataobject.ShoppingCartDiyCustomerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartDiyCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCartDiyCustomerDO record);

    void insertList(List<ShoppingCartDiyCustomerDO> records);

    int insertSelective(ShoppingCartDiyCustomerDO record);

    ShoppingCartDiyCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCartDiyCustomerDO record);

    int updateByPrimaryKey(ShoppingCartDiyCustomerDO record);

    void deleteByCartId(Integer cartId);

    void deleteByCartIds(List<Integer> cartIds);

    // 循环查询客户购物车定制信息
    List<ShoppingCartDiyCustomerDO> listByCartIds(List<Integer> cartIds);

    // 单个查询客户购物车定制信息
    ShoppingCartDiyCustomerDO getByCardId(Integer id);
}