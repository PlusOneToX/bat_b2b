package com.bat.order.dao.cart;

import java.util.List;
import java.util.Map;

import com.bat.order.dao.cart.dataobject.ShoppingCartCustomerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingCartCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCartCustomerDO record);

    void insertList(List<ShoppingCartCustomerDO> records);

    int insertSelective(ShoppingCartCustomerDO record);

    ShoppingCartCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCartCustomerDO record);

    int updateByPrimaryKey(ShoppingCartCustomerDO record);

    void update(Map map);

    int updateList(List<ShoppingCartCustomerDO> records);

    void deleteByIds(List<Integer> ids);

    // 通过用户编号和分销商编号查询数据
    List<ShoppingCartCustomerDO> listByCustomerIdAndDistributorId(@Param("customerId") Integer customerId,
        @Param("distributorId") Integer distributorId);

    List<ShoppingCartCustomerDO> listByMap(Map map);

    void updateListOpenFlag(@Param("goodsIds") List<Integer> goodsIds, @Param("openFlag") Short OpenFlag);

    void updateListOpenFlagByItemIds(@Param("itemIds") List<Integer> itemIds, @Param("openFlag") Short OpenFlag);
}