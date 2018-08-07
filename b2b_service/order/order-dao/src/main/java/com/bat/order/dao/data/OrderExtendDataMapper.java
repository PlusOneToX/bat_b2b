package com.bat.order.dao.data;

import java.util.List;

import com.bat.order.dao.data.co.OrderExtendDataCO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderExtendDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderExtendDataDO record);

    int insertSelective(OrderExtendDataDO record);

    OrderExtendDataDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderExtendDataDO record);

    int updateByPrimaryKey(OrderExtendDataDO record);

    OrderExtendDataDO getByOrderId(Integer id);

    List<OrderExtendDataCO> listExtendDataSimpleByOrderIdList(@Param("orderIdList") List<Integer> orderIdList);

    OrderExtendDataDO getByOrderErpNo(@Param("orderErpNo") String orderErpNo);

    List<OrderExtendDataDO> listByOrderThirdpartyNoAndDistributorId(
        @Param("orderThirdpartyNo") String orderThirdpartyNo, @Param("distributorId") Integer distributorId);

    OrderExtendDataDO getByOrderNo(@Param("orderNo") String orderNo);

    OrderExtendDataDO getByOrderFactoryNoAndFactoryCode(@Param("orderFactoryNo") String orderFactoryNo,
        @Param("factoryCode") String factoryCode);

    OrderExtendDataCO getExtendDataSimpleByOrderId(@Param("orderId") Integer orderId);

    OrderExtendDataDO getByOrderFactoryNo(@Param("orderFactoryNo") String orderFactoryNo);

    String queryOrderFactoryNoByOrderId(Integer orderId);

    void updateShoppingCartItemStatus(@Param("goodsId") Integer goodsId,
        @Param("cartItemStatus") Integer cartItemStatus);
}