package com.bat.order.api.order;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderOneMoreDTO;
import com.bat.order.api.order.dto.distributor.OrderCheckCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.api.order.dto.orderquery.user.*;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.order.dto.orderquery.user.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 11:30
 */
public interface UserDistributorOrderServiceI {
    /**
     * 根据订单id获取订单信息
     *
     * @param id
     * @return
     */
    UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO getOrderInfoById(Integer id);

    /**
     * 下单接口
     * 
     * @param cmd
     * @param userId
     * @return
     */
    BaseIds createOrder(OrderInfoCmd cmd, String userId, String userName, String contactId, String contactName,
                        String platform, String language);

    /**
     * 分销订单审核接口(支持批量审核)
     * 
     * @param cmd
     * @param userId
     */
    void checkOrder(OrderCheckCmd cmd, String userId);

    /**
     * 分销商获取再来一单数据
     * 
     * @param qry
     * @param userId
     * @return
     */
    List<OrderOneMoreDTO> oneMoreOrder(BaseId qry, String userId);

    /**
     * pc 前台下单结果
     * 
     * @param ids
     * @return
     */
    UserPCDistributorOrderResultDTO getOrderInfoByIds(List<Integer> ids);

    /**
     * pc 前台获取订单列表
     * 
     * @param qry
     * @return
     */
    PageInfo<UserPCDistributorOrderInfoListDTO> listPCOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry);

    /**
     * pc 前台获取分销订单详情
     * 
     * @param qry
     * @return
     */
    UserPCDistributorOrderDetailDTO getPCOrderDetailInfoById(UserPCDistributorOrderDetailQry qry);

    /**
     * pc 前台获取店铺订单列表
     * 
     * @param qry
     * @return
     */
    PageInfo<UserPCDistributorOrderInfoListDTO>
        listPCShopOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry);

    /**
     * pc 前台获取店铺订单导出
     *
     * @param qry
     * @return
     */
    List<UserPCDistributorOrderInfoExportDTO> pcShopOrderInfoByDistributorIdExport(UserPCDistributorOrderInfoListQry qry);

    /**
     * mb 获取当前分销商订单
     *
     * @param qry
     * @return
     */
    PageInfo<UserMBDistributorOrderInfoListDTO> listMBOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry);

    /**
     * mb 获取下级分销商订单
     * 
     * @param qry
     * @return
     */
    PageInfo<UserMBDistributorOrderInfoListDTO>
        listMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry);

    /**
     * mb 获取下级分销商订单总数
     *
     * @param qry
     * @return
     */
    Integer countMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoCountQry qry);

    /**
     * PC端获取订单发货单详情
     * 
     * @param qry
     * @return
     */
    List<OrderDeliverBillDetailDTO> getPCOrderDeliverBillDetailById(UserPCDistributorOrderDeliverBillDetailQry qry);

    /**
     * 移动端根据订单号获取获取订单发货单详情
     * 
     * @param qry
     * @return
     */
    List<OrderDeliverDetailDTO> getMBOrderDeliverBillDetailByOrderId(UserPCDistributorOrderDetailQry qry);

    /**
     * 根据订单id取消订单
     *
     */
    void orderCancel(OrderCancelCmd cmd, String userId, String userName);

    /**
     * 移动端 订单数量
     * 
     * @param qry
     * @return
     */
    List<UserMBOrderInfoCountDTO> listMBOrderInfoCountByDistributorId(UserMBOrderInfoCountQry qry);

    /**
     * 分销端获取订单失效时间（单位分钟）
     * 
     * @return
     */
    Integer getLoseTime();
}
