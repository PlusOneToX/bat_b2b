package com.bat.order.api.deliver;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillListDTO;
import com.bat.order.api.common.response.ResponseBaseBean;
import com.bat.order.api.deliver.dto.OrderGoodsDeliverBean;
import com.bat.order.api.deliver.dto.data.AdminOrderDeliverBillDetailDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDeliverBillListQry;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

public interface OrderDeliverBillServiceI {
    /**
     * 根据订单id查询订单配送流水列表
     * 
     * @param orderId
     * @return
     */
    List<OrderDeliverBillDO> listByOrderId(Integer orderId);

    /**
     * 创建发货流水
     * 
     * @param deliverOrderDetailList
     * @param deliverStatus
     * @param logisticsStatus
     * @param logisticsName
     * @param expressNo
     * @param orderInfoDO
     * @param distributionId
     * @param orderDeliveryType
     * @param orderDeliveryDO
     * @param erpId
     * @param deliveryErpNo
     * @param deliveryTime
     * @param diyFlag
     * @param push
     *            是否需要推送到第三方 1、是 0、否
     * @param distributionCode
     *            快递编码
     * @return
     */
    ResponseBaseBean createBill(List<OrderGoodsDeliverBean> deliverOrderDetailList, Short deliverStatus,
        Short logisticsStatus, String logisticsName, String expressNo, OrderInfoDO orderInfoDO, Integer distributionId,
        Short orderDeliveryType, OrderDeliveryDO orderDeliveryDO, String erpId, String deliveryErpNo, Date deliveryTime,
        Boolean diyFlag, Short push, String distributionCode);

    void update(OrderDeliverBillDO orderDeliverBillDO);

    /**
     * 发货单列表
     *
     * @param qry
     * @return
     */
    PageInfo<AdminOrderDeliverBillListDTO> listOrderDeliverBillByParam(AdminOrderDeliverBillListQry qry);

    /**
     * 发货单明细
     * 
     * @param id
     * @return
     */
    AdminOrderDeliverBillDetailDTO getDetail(Integer id);

    /**
     * 物流查询
     * 
     * @param orderId
     * @return
     */
    OrderDeliverDetailDTO logisticsView(Integer orderId);

    /**
     * 根据ERP出库单号查询
     * 
     * @param deliverOrderNo
     * @return
     */
    List<OrderDeliverBillDO> listByDeliverErpNo(String deliverOrderNo);

    /**
     * 根据id列表批量删除
     * 
     * @param idList
     */
    void batchDelete(List<Integer> idList);

    /**
     * 根据id查询对象
     * 
     * @param id
     * @return
     */
    OrderDeliverBillDO getById(Integer id);

    /**
     * 批量修改
     * 
     * @param orderDeliverBillDOList
     */
    void batchUpdate(List<OrderDeliverBillDO> orderDeliverBillDOList);

    /**
     * 获取为同步erp订单发货单id列表
     * 
     * @return
     */
    List<Integer> getNotErpOrderDeliverBillIds(Date startTime);
}
