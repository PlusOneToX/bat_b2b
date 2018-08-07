package com.bat.order.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.order.service.deliver.executor.OrderDeliverBillDetailQryExe;
import com.bat.order.service.order.executor.OrderGoodsCmdExe;
import com.bat.order.service.order.executor.OrderGoodsQryExe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.dto.common.OrderGoodsVmiDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderGoodsVmiListQry;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDetailDTO;

@Service
public class OrderGoodsServiceImpl implements OrderGoodsServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsServiceImpl.class);

    @Autowired
    private OrderGoodsQryExe orderGoodsQryExe;

    @Autowired
    private OrderGoodsCmdExe orderGoodsCmdExe;

    @Autowired
    private OrderDeliverBillDetailQryExe orderDeliverBillDetailQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createList(List<OrderGoodsDO> orderGoodsDOList, Integer orderId) {
        orderGoodsDOList.stream().forEach(orderGoodsDO -> {
            orderGoodsDO.setOrderId(orderId);
            orderGoodsDO.setCreateTime(new Date());
            orderGoodsDO.setUpdateTime(new Date());
            orderGoodsCmdExe.create(orderGoodsDO);
        });
    }

    @Override
    public List<OrderGoodsDO> listByOrderId(Integer orderId) {
        return orderGoodsQryExe.listByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrderGoodsDO orderGoodsDO) {
        orderGoodsDO.setUpdateTime(new Date());
        orderGoodsCmdExe.update(orderGoodsDO);
    }

    @Override
    public OrderGoodsDO findByOrderIdAndItemIdAndSerialNumber(Integer orderId, Integer itemId, Integer serialNumber) {
        return orderGoodsQryExe.findByOrderIdAndItemIdAndSerialNumber(orderId, itemId, serialNumber);
    }

    @Override
    public PageInfo<OrderGoodsVmiDTO> listVmiByParam(AdminOrderGoodsVmiListQry qry) {
        return orderGoodsQryExe.listVmiByParam(qry);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdate(List<OrderGoodsDO> orderGoodsDOList) {
        orderGoodsCmdExe.batchUpdate(orderGoodsDOList);
    }

    @Override
    public List<OrderGoodsDetailDTO> listOrderGoodsDetailByOrderId(Integer orderId) {
        return orderGoodsQryExe.listOrderGoodsDetailByOrderId(orderId);
    }

    /**
     * 上线重置发货数量
     */
    @Override
    public void resetDeliverCount() {
        List<OrderDeliverBillDetailDO> billDetailDOList = orderDeliverBillDetailQryExe.listMorethenCreateTime();
        LOGGER.info("查询回来不匹配的发货明细列表{}", JSON.toJSONString(billDetailDOList));
        if (billDetailDOList == null || billDetailDOList.size() == 0) {
            LOGGER.info("找不到不匹配的列表");
            return;
        }
        List<OrderGoodsDO> orderGoodsDOList = new ArrayList<>();
        billDetailDOList.stream().forEach(orderDeliverBillDetailDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsQryExe.getById(orderDeliverBillDetailDO.getOrderGoodsId());
            if (orderGoodsDO.getDeliverCount() < orderDeliverBillDetailDO.getCount()) {
                LOGGER.info(orderGoodsDO.getId() + "商品明细数量不匹配,{}", JSON.toJSONString(orderGoodsDO));
                orderGoodsDO.setDeliverCount(orderDeliverBillDetailDO.getCount());
                // 未发数量
                orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount() - orderGoodsDO.getDeliverCount());
                orderGoodsDOList.add(orderGoodsDO);
            }
        });
        // 批量修改
        if (orderGoodsDOList.size() == 0) {
            LOGGER.info("找不到不匹配的列表、不需要修改");
        }
        LOGGER.info("批量修改orderGoodsList{}", JSON.toJSONString(orderGoodsDOList));
        orderGoodsCmdExe.batchUpdate(orderGoodsDOList);
    }

    @Override
    public List<OrderGoodsDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderGoodsQryExe.listByOrderIdList(orderIdList);
    }
}
