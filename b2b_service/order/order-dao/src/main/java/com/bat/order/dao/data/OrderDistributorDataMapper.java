package com.bat.order.dao.data;

import java.util.Date;
import java.util.List;

import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDistributorDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDistributorDataDO record);

    int insertSelective(OrderDistributorDataDO record);

    OrderDistributorDataDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDistributorDataDO record);

    int updateByPrimaryKey(OrderDistributorDataDO record);

    void updateOrderPayStatus(@Param("orderId") Integer orderId, @Param("distributorId") Integer distributorId,
        @Param("payStatus") Short payStatus);

    void updateList(List<OrderDistributorDataDO> records);

    List<OrderDistributorDataDO> listByOrderId(@Param("orderId") Integer orderId);

    List<OrderDistributorDataDO> listByOrderErpNoAndErpFlag(@Param("orderErpNo") String orderErpNo,
        @Param("erpFlag") Short erpFlag);

    List<OrderDistributorDataDO> listByOrderErpNo(@Param("orderErpNo") String orderErpNo);

    OrderDistributorDataDO getByOrderIdAndDistributorId(@Param("orderId") Integer orderId,
        @Param("distributorId") Integer distributorId);

    OrderDistributorDataDO getByOrderIdAndDistributorAncestorId(@Param("orderId") Integer orderId,
        @Param("distributorAncestorId") Integer distributorId);

    List<OrderDistributorDataDO> listByOrderIdsAndDistributorId(@Param("orderIds") List<Integer> orderIds,
        @Param("distributorId") Integer distributorId);

    List<OrderDistributorDataDO> listByOrderIdsAndAncestorDistributor(@Param("orderIds") List<Integer> orderIds,
        @Param("ancestorDistributorId") Integer ancestorDistributorId);

    OrderDistributorDataDO getDistributorDataByOrderIdAndFlag(@Param("orderId") Integer id,
        @Param("orderFlag") Short directFlag);

    List<OrderDistributorDataDO> listByCondition(@Param("orderId") Integer orderId,
        @Param("distributorId") Integer distributorId, @Param("erpFlag") Short erpFlag);

    /**
     * 发货时 更新订单状态
     *
     * @param orderId
     * @param payStatus
     */
    void updateOrderPayStatusFormDeliver(@Param("orderId") Integer orderId, @Param("payStatus") Short payStatus);

    OrderDistributorDataDO getFirstTreeNode(@Param("orderId") Integer orderId);

    List<Integer> getNotErpOrderIds(@Param("startTime") Date startTime);

    List<OrderDistributorDataDO> queryOrderDistributorDataByOrderId(@Param("orderId") Integer orderId,
        @Param("treeNode") Integer treeNode);

    void updateOrderDistributorDataByDistributorIdAndOrderId(@Param("distributorId") Integer distributorId,
        @Param("orderId") Integer orderId, @Param("distributionPayWay") Short distributionPayWay);

    void modifyPaymentTime(@Param("primaryDistributorId") Integer primaryDistributorId,
        @Param("orderId") Integer orderId);

    OrderDistributorDataDO findATier1DistributorOrderDistributorData(
        @Param("primaryDistributorId") Integer primaryDistributorId, @Param("orderId") Integer orderId);
}