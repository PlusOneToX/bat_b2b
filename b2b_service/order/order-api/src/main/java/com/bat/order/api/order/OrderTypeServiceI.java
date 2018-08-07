package com.bat.order.api.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.dto.common.OrderTypeCmd;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

import java.util.List;

public interface OrderTypeServiceI {
    OrderTypeDO getById(Integer orderTypeId);

    /**
     * 获取订单类型列表
     * 
     * @param qry
     * @return
     */
    PageInfo<OrderTypeDTO> listOrderType(OrderTypeListQry qry);

    /**
     * 创建订单类型
     * 
     * @param qry
     */
    void createOrderType(OrderTypeCmd qry);

    /**
     * 更新订单类型
     * 
     * @param qry
     */
    void updateOrderType(OrderTypeCmd qry);

    /**
     * 删除订单类型
     * 
     * @param id
     */
    void deleteOrderType(Integer id);

    /**
     * 获取单个订单类型
     * 
     * @param id
     * @return
     */
    OrderTypeDTO getOrderType(Integer id);

    /**
     * 条件查询订单类型列表
     * @param openFlag
     * @return
     */
    List<OrderTypeDO> listByCondition(Short openFlag);
}
