package com.bat.order.dao.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.order.dao.order.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.order.co.OrderExcelCO;

@Mapper
public interface OrderInfoDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfoDO record);

    int insertSelective(OrderInfoDO record);

    OrderInfoDO selectByPrimaryKey(Integer id);

    List<OrderInfoDO> listByIds(@Param("ids") List<Integer> ids);

    int updateByPrimaryKeySelective(OrderInfoDO record);

    int updateByPrimaryKey(OrderInfoDO record);

    /**
     * 查询分销商订单后台
     * 
     * @param map
     * @return
     */
    List<AdminOrderInfoListDO> listAdminDistributorOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 查询柔性订单后台
     * 
     * @param map
     * @return
     */
    List<AdminOrderInfoListDO> listAdminCustomerOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 查询异常订单后台
     * 
     * @param map
     * @return
     */
    List<AdminOrderInfoListDO> listAdminExceptionOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 
     * 查询柔性订单
     *
     * @param map
     * @return
     */
    List<OrderInfoDO> listUserCustomerOrderByParams(@Param("params") Map<String, Object> map);

    /**
     *
     * 查询分销订单
     *
     * @param map
     * @return
     */
    List<UserDistributorOrderInfoListDO> listUserPCDistributorOrderByParams(@Param("params") Map<String, Object> map);

    /**
     *
     * 查询柔性店铺订单
     *
     * @param map
     * @return
     */
    List<UserCustomerOrderInfoListDO> listUserPCShopOrderInfoByParams(@Param("params") Map<String, Object> map);

    /**
     * 查询分销订单
     * 
     * @param map
     */
    List<OrderInfoDO> listUserMBDistributorOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 查询分销订单总数
     *
     * @param map
     */
    Integer countUserMBDistributorOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 批量修改
     * 
     * @param orderInfoDOList
     */
    void batchUpdate(@Param("orderInfoList") List<OrderInfoDO> orderInfoDOList);

    OrderInfoDO getByOrderNo(@Param("orderNo") String orderNo);

    List<OrderExcelCO> listCOByCondition(@Param("sql") String sql, @Param("activityFlag") Boolean activityFlag,
        @Param("distributorId") Integer distributorId, @Param("orderSourceList") List<String> orderSourceList,
        @Param("orderStatusList") List<Short> orderStatusList, @Param("startTime") Date startTime,
        @Param("endTime") Date endTime, @Param("itemCode") String itemCode,
        @Param("orderTypeIdList") List<Integer> orderTypeIdList, @Param("payStatusList") List<Short> payStatusList,
        @Param("deliverStatusList") List<Short> deliverStatusList);

    /**
     *
     * 款到发货+直发订单+时间限制+发货国家限制
     *
     * @param map
     * @return
     */
    List<OrderInfoDO> listCancelOrderByParams(@Param("params") Map<String, Object> map);

    /**
     * 获取未发货的工厂订单
     * @param factoryCode
     * @return
     */
    List<OrderFactoryNoDO> listOrderFactoryNoByFactoryCodeAndNoLogistics(@Param("factoryCode") String factoryCode,@Param("startTime") Date startTime);

}
