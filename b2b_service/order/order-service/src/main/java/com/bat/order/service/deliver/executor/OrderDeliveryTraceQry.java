package com.bat.order.service.deliver.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.deliver.convertor.OrderDeliveryTraceConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.deliver.KdniaoTrackQueryAPI;
import com.bat.order.api.deliver.dto.data.KdnRespDTO;
import com.bat.order.api.deliver.dto.data.OrderDeliveryTraceDTO;
import com.bat.order.dao.deliver.OrderDeliverBillDOMapper;
import com.bat.order.dao.deliver.OrderDeliveryDOMapper;
import com.bat.order.dao.deliver.OrderDeliveryTraceDOMapper;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryTraceDO;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import com.bat.order.service.common.error.OrderDeliverBillErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/22 19:56
 */
@Component
@Slf4j
public class OrderDeliveryTraceQry {
    @Resource
    private OrderDeliveryTraceDOMapper orderDeliveryTraceDOMapper;

    @Resource
    private OrderDeliverBillDOMapper orderDeliverBillDOMapper;

    @Autowired
    private KdniaoTrackQueryAPI kdniaoTrackQueryAPI;

    @Resource
    private OrderDeliveryDOMapper orderDeliveryDOMapper;

    @Transactional(rollbackFor = Exception.class)
    public List<OrderDeliveryTraceDTO> getByOrderDeliverBillId(Integer orderDeliverBillId) {
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillDOMapper.selectByPrimaryKey(orderDeliverBillId);
        if (orderDeliverBillDO == null) {
            throw OrderException.buildException(OrderDeliverBillErrorCode.B_ORDER_DELIVER_BILL_NULL);
        }
        // 已签收的件 尝试读取数据库信息
        if (Short.valueOf(orderDeliverBillDO.getLogisticsStatus())
            .equals(OrderDeliverConstant.ORDER_DELIVER_LOGISTICS_STATUS_SIGN)) {
            List<OrderDeliveryTraceDTO> orderDeliveryTraceDTOS = OrderDeliveryTraceConvertor
                .toOrderDeliveryTraceDTOList(orderDeliveryTraceDOMapper.listByOrderDeliverBillId(orderDeliverBillId),
                    orderDeliverBillDO.getLogisticsStatus());
            if (!CollectionUtils.isEmpty(orderDeliveryTraceDTOS)) {
                return orderDeliveryTraceDTOS;
            }
        } else {
            // 待签收 与 问题件 删除后重新查询 并添加
            orderDeliveryTraceDOMapper.deleteByOrderDeliverBillId(orderDeliverBillId);
        }
        if (StringUtils.isNoneBlank(orderDeliverBillDO.getDistributionCode(), orderDeliverBillDO.getLogisticsNo())) {
            KdnRespDTO orderTracesByJson;
            if ("SF".equals(orderDeliverBillDO.getDistributionCode())) {
                OrderDeliveryDO byOrderId = orderDeliveryDOMapper.getByOrderId(orderDeliverBillDO.getOrderId());
                if (byOrderId == null) {
                    log.error("订单：{}，物流发货单对应的 收货人信息缺失", orderDeliverBillDO.getOrderId());
                    return new ArrayList<>();
                } else {
                    String mobile = byOrderId.getMobile();
                    orderTracesByJson =
                        kdniaoTrackQueryAPI.getOrderTracesByJson(orderDeliverBillDO.getDistributionCode(),
                            orderDeliverBillDO.getLogisticsNo(), mobile.substring(mobile.length() - 4));
                }
            } else {
                orderTracesByJson = kdniaoTrackQueryAPI.getOrderTracesByJson(orderDeliverBillDO.getDistributionCode(),
                    orderDeliverBillDO.getLogisticsNo());
            }
            if (orderTracesByJson.getSuccess()) {
                orderDeliverBillDO.setLogisticsStatus(orderTracesByJson.getState());
                orderDeliverBillDO.setUpdateTime(new Date());
                orderDeliverBillDOMapper.updateByPrimaryKey(orderDeliverBillDO);
                List<KdnRespDTO.TracesDTO> traces = orderTracesByJson.getTraces();
                if (!CollectionUtils.isEmpty(traces)) {
                    List<OrderDeliveryTraceDO> collect = traces.stream().map(dto -> {
                        OrderDeliveryTraceDO traceDO = new OrderDeliveryTraceDO();
                        traceDO.setOrderDeliverBillId(orderDeliverBillId);
                        traceDO.setAcceptStation(dto.getAcceptStation());
                        traceDO.setAcceptTime(dto.getAcceptTime());
                        return traceDO;
                    }).collect(Collectors.toList());
                    orderDeliveryTraceDOMapper.insertBatch(collect);
                    return OrderDeliveryTraceConvertor.toOrderDeliveryTraceDTOList(collect,
                        orderDeliverBillDO.getLogisticsStatus());
                }
                return new ArrayList<>();
            } else {
                log.error(orderTracesByJson.getReason());
                return new ArrayList<>();
                // throw OrderException.buildException(OrderDeliverBillErrorCode.B_ORDER_KDN_ERROR,
                // orderTracesByJson.getReason());
            }
        } else {
            return new ArrayList<>();
        }
    }
}
