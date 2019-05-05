package com.bat.dubboapi.order.order.api;

import java.util.Date;
import java.util.List;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDiyRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;

public interface OrderGoodsDiyServiceRpc {

    /**
     * 根据订单id查询定制订单详情
     * 
     * @param orderId
     * @return
     */
    Response<List<OrderGoodsDiyRpcDTO>> listByOrderId(Integer orderId);

    /**
     * 查询一小时前至一天前区间的订单
     * 
     * @return
     */
    Response<List<OrderGoodsDiyRpcDTO>> findLatelyNullLabel();

    /**
     * 手动生成标签
     * 
     * @param orderGoodsDiyId
     * @return
     */
    Response createLable(Integer orderGoodsDiyId);

    /**
     * 根据工厂编码查询未同步工厂订单id列表
     * 
     * @param manufactor
     * @return
     */
    Response<OrderIdListQry> listUnSyncFactoryByManufactor(String manufactor);

    /**
     * <h2>根据工厂编码查询需要同步的订单id</h2>
     */
    Response<OrderIdListQry> needToSyncOrders(String manufactor);


    /**
     * 根据时间查询未同步工厂订单id列表
     *
     * @param startTime
     * @return
     */
    Response<OrderIdListQry> listUnSyncFactoryByStartTime(Date startTime);

}