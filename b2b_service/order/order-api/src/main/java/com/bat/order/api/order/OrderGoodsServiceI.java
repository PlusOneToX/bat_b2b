package com.bat.order.api.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.dto.common.OrderGoodsVmiDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderGoodsVmiListQry;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDetailDTO;

import java.util.List;

public interface OrderGoodsServiceI {

    void createList(List<OrderGoodsDO> orderGoodsDOList, Integer orderId);

    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    List<OrderGoodsDO> listByOrderId(Integer orderId);

    void update(OrderGoodsDO orderGoodsDO);

    /**
     *
     * @param orderId 订单id
     * @param itemId 货品id
     * @param serialNumber 行序号
     * @return
     */
    OrderGoodsDO findByOrderIdAndItemIdAndSerialNumber(Integer orderId, Integer itemId, Integer serialNumber);

    PageInfo<OrderGoodsVmiDTO> listVmiByParam(AdminOrderGoodsVmiListQry qry);

    /**
     * 批量修改
     * @param orderGoodsDOList
     */
    void batchUpdate(List<OrderGoodsDO> orderGoodsDOList);

    List<OrderGoodsDetailDTO> listOrderGoodsDetailByOrderId(Integer orderId);

    /**
     * 重置发货数量
     */
    void resetDeliverCount();

    /**
     * 根据订单id列表查询订单明细
     * @param orderIdList
     * @return
     */
    List<OrderGoodsDO> listByOrderIdList(List<Integer> orderIdList);
}
