package com.bat.order.service.order;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.bat.order.service.deliver.executor.OrderDeliveryQryExe;
import com.bat.order.service.order.executor.OrderCmdExe;
import com.bat.order.service.order.executor.distributor.UserDistributorOrderCmdExe;
import com.bat.order.service.order.executor.distributor.UserDistributorOrderQryExe;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.basic.BaseIds;
import com.bat.order.api.cost.dto.data.OrderDistributorCostDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliverBillDetailDTO;
import com.bat.order.api.order.UserDistributorOrderServiceI;
import com.bat.order.api.order.dto.common.OrderCancelCmd;
import com.bat.order.api.order.dto.common.OrderOneMoreDTO;
import com.bat.order.api.order.dto.distributor.OrderCheckCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderDeliverDetailDTO;
import com.bat.order.api.order.dto.orderquery.common.OrderDistributorDTO;
import com.bat.order.api.order.dto.orderquery.user.*;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/17 11:32
 */
@Service
public class UserDistributorOrderServiceImpl implements UserDistributorOrderServiceI {

    @Resource
    private UserDistributorOrderCmdExe cmdExe;

    @Resource
    private UserDistributorOrderQryExe qryExe;

    @Resource
    private OrderDeliveryQryExe orderDeliveryQryExe;

    @Resource
    private OrderCmdExe orderCmdExe;

    @Override
    public UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO getOrderInfoById(Integer id) {
        return qryExe.getOrderInfoById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseIds createOrder(OrderInfoCmd cmd, String userId, String userName, String contactId, String contactName,
        String platform, String language) {
        List<OrderInfoDO> orders =
            cmdExe.createOrder(cmd, userId, userName, contactId, contactName, platform, language);
        BaseIds ids = new BaseIds();
        ids.setIds(orders.stream().map(OrderInfoDO::getId).collect(toList()));
        return ids;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOrder(OrderCheckCmd cmd, String userId) {
        cmdExe.checkOrder(cmd, userId);
    }

    @Override
    public List<OrderOneMoreDTO> oneMoreOrder(BaseId qry, String userId) {
        return qryExe.oneMoreOrder(qry, userId);
    }

    @Override
    public UserPCDistributorOrderResultDTO getOrderInfoByIds(List<Integer> ids) {
        List<UserPCDistributorOrderResultDTO.UserDistributorOrderItemResultDTO> collect =
            ids.stream().map(this::getOrderInfoById).collect(toList());
        UserPCDistributorOrderResultDTO dto = new UserPCDistributorOrderResultDTO();
        dto.setItem(collect);
        AtomicReference<BigDecimal> decimal = new AtomicReference<>(new BigDecimal(0));
        collect.forEach(orderItemResultDTO -> {
            OrderDistributorDTO orderDistributor = orderItemResultDTO.getOrderDistributor();
            OrderDistributorCostDTO orderDistributorCostDTO = orderDistributor.getOrderDistributorCostDTO();
            if (orderDistributorCostDTO != null) {
                decimal.set(decimal.get().add(orderDistributorCostDTO.getPayAmount()));
            }
        });
        dto.setAmount(decimal.get());
        OrderDeliveryDO byOrderId = orderDeliveryQryExe.getByOrderId(collect.get(0).getOrderInfo().getId());
        dto.setUserName(byOrderId.getUserName());
        return dto;
    }

    @Override
    public PageInfo<UserPCDistributorOrderInfoListDTO>
        listPCOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry) {
        return qryExe.listOrderInfoByDistributorId(qry);
    }

    @Override
    public PageInfo<UserPCDistributorOrderInfoListDTO>
        listPCShopOrderInfoByDistributorId(UserPCDistributorOrderInfoListQry qry) {
        return qryExe.listShopOrderInfoByDistributorId(qry);
    }

    @Override
    public List<UserPCDistributorOrderInfoExportDTO>
        pcShopOrderInfoByDistributorIdExport(UserPCDistributorOrderInfoListQry qry) {
        return qryExe.shopOrderInfoByDistributorIdExportData(qry);
    }

    @Override
    public PageInfo<UserMBDistributorOrderInfoListDTO>
        listMBOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry) {
        return qryExe.listMBOrderInfoByDistributorId(qry);
    }

    @Override
    public PageInfo<UserMBDistributorOrderInfoListDTO>
        listMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoListQry qry) {
        return qryExe.listMBNextDistributorOrderInfoByDistributorId(qry);
    }

    @Override
    public Integer countMBNextDistributorOrderInfoByDistributorId(UserMBDistributorOrderInfoCountQry qry) {
        return qryExe.countMBNextDistributorOrderInfoByDistributorId(qry);
    }

    @Override
    public List<OrderDeliverBillDetailDTO>
        getPCOrderDeliverBillDetailById(UserPCDistributorOrderDeliverBillDetailQry qry) {
        return qryExe.getPCOrderDeliverBillDetailById(qry);
    }

    @Override
    public UserPCDistributorOrderDetailDTO getPCOrderDetailInfoById(UserPCDistributorOrderDetailQry qry) {
        return qryExe.getOrderDetailInfoById(qry);
    }

    @Override
    public List<OrderDeliverDetailDTO> getMBOrderDeliverBillDetailByOrderId(UserPCDistributorOrderDetailQry qry) {
        return qryExe.getMBOrderDeliverBillDetailByOrderId(qry);
    }

    @Override
    public void orderCancel(OrderCancelCmd cmd, String userId, String userName) {
        orderCmdExe.cancelOrderByOrderId(cmd.getId(), cmd.getRemark(), userId, userName);
    }

    @Override
    public List<UserMBOrderInfoCountDTO> listMBOrderInfoCountByDistributorId(UserMBOrderInfoCountQry qry) {
        return qryExe.listMBOrderInfoCountByDistributorId(qry);
    }

    @Override
    public Integer getLoseTime() {
        return qryExe.getLoseTime();
    }

}
