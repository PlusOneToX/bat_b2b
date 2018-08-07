package com.bat.order.api.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderPromotionDTO;
import com.bat.order.api.order.dto.distributor.OrderPromotionQry;
import com.bat.order.api.order.dto.diy.OrderDiyCmd;
import com.bat.order.api.order.dto.export.OrderInfoExportQry;
import com.bat.order.api.order.dto.orderquery.admin.*;
import com.bat.order.api.order.dto.orderquery.admin.*;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public interface OrderInfoServiceI {
    Response createDiyOrder(OrderDiyCmd orderDiyCmd);

    /**
     * 根据订单id查询订单信息
     * 
     * @param orderId
     * @return
     */
    OrderInfoDO getById(Integer orderId);

    /**
     * 修改订单
     * 
     * @param orderInfoDO
     */
    void update(OrderInfoDO orderInfoDO);

    /**
     * 分销订单列表
     * 
     * @param qry
     * @return
     */
    PageInfo<AdminOrderInfoListDTO> listDistributorOrderInfo(AdminDistributorOrderInfoListQry qry);

    /**
     * 柔性定制订单列表
     *
     * @param qry
     * @return
     */
    PageInfo<AdminOrderInfoListDTO> listCustomerDiyOrderInfo(AdminCustomerOrderInfoListQry qry);

    /**
     * 异常订单
     *
     * @param qry
     * @return
     */
    PageInfo<AdminOrderInfoListDTO> listExceptionOrderInfo(AdminExceptionOrderInfoListQry qry);

    /**
     * 分销订单详情
     *
     * @param qry
     * @return
     */
    AdminDistributorOrderInfoDetailDTO getDistributorOrderDetail(AdminOrderDetailQry qry);

    /**
     * 柔性订单详情
     *
     * @param qry
     * @return
     */
    AdminCustomerOrderInfoDetailDTO getCustomerOrderDetail(AdminOrderDetailQry qry);

    /**
     * 根据订单id列表查询订单列表
     * 
     * @param orderIds
     * @return
     */
    List<OrderInfoDO> listByIds(List<Integer> orderIds);

    /**
     * 根据订单id取消订单
     *
     */
    void orderCancel(OrderCancelCmd cmd, String userId, String userName);

    /**
     * 批量修改
     * 
     * @param orders
     */
    void batchUpdate(List<OrderInfoDO> orders);

    /**
     * 根据订单活动条件ids获取活动信息
     * 
     * @param qry
     * @return
     */
    OrderPromotionDTO orderPromotion(OrderPromotionQry qry);

    List<Integer> listUnSyncFactory(String manufactors);

    /**
     * 根据订单id 获取订单编号
     * 
     * @param id
     * @return
     */
    String getOrderIdByOrderNo(Integer id);

    /**
     * 处理ERP的行序号
     * @param inputStream
     * @return
     */
    Response setOrderGoodsSerialNumber(InputStream inputStream);

    /**
     * 订单导出
     * @param orderInfoExportQry
     * @return
     */
    HSSFWorkbook createExcelByCondition(OrderInfoExportQry orderInfoExportQry, String currentLanguage);

    void test();

    /**
     * 判断分销层订单状态
     * @param qry
     */
    AdminJudgeOrderDTO judgeDistributorOrderStatus(AdminJudgeOrderQry qry);
}
