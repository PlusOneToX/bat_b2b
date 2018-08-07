package com.bat.order.service.stock.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.common.error.OrderGoodsErrorCode;
import com.bat.order.service.message.MessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberIdList;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.data.OrderExtendDataServiceI;
import com.bat.order.api.stock.dto.OrderGoodsStockQry;
import com.bat.order.api.stock.dto.OrderGoodsStockQryDTO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.stock.OrderGoodsStockDOMapper;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDTO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDTO2;

@Component
public class OrderGoodsStockQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsStockQryExe.class);

    @Resource
    private OrderGoodsStockDOMapper orderGoodsStockDOMapper;

    @Autowired
    private OrderExtendDataServiceI orderExtendDataServiceI;

    @Resource
    private MessageSendService sendService;

    public List<OrderGoodsStockSimpleDTO>
        listByOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberIdList> lineNumberIdLists) {
        List<OrderGoodsStockSimpleDTO> list = new ArrayList<>();
        LOGGER.info("根据ERP单号和订单内码列表查询，参数{}", JSON.toJSONString(lineNumberIdLists));
        for (int x = 0; x < lineNumberIdLists.size(); x++) {
            OrderErpNoLineNumberIdList orderErpNoLineNumber = lineNumberIdLists.get(x);
            OrderExtendDataDO orderExtendDataDO =
                orderExtendDataServiceI.getByOrderErpNo(orderErpNoLineNumber.getOrderErpNo());
            if (orderExtendDataDO == null) {
                // 非B2B订单、忽略
                continue;
            }
            LOGGER.info("根据B2B订单id{}和订单内码列表查询，参数{}", orderExtendDataDO.getOrderId(),
                JSON.toJSONString(orderErpNoLineNumber));
            List<OrderGoodsStockDTO> resultList = orderGoodsStockDOMapper.listDTOByOrderIdAndSerialNumberList(
                orderExtendDataDO.getOrderId(), orderErpNoLineNumber.getLineNumberIdList());
            List<OrderGoodsStockSimpleDTO> dtoList = BeanUtils.copyList(resultList, OrderGoodsStockSimpleDTO.class);

            Set<Integer> orderGoodsIdList = null;
            if (dtoList != null && dtoList.size() > 0) {
                orderGoodsIdList =
                    dtoList.stream().map(OrderGoodsStockSimpleDTO::getOrderGoodsId).collect(Collectors.toSet());
            }
            // 设置ERP单号
            if (orderGoodsIdList == null
                || orderGoodsIdList.size() - orderErpNoLineNumber.getLineNumberIdList().size() != 0) {
                LOGGER.info("erp订单的明细内码与B2B的orderGoods行序号对不上{}", JSON.toJSON(orderErpNoLineNumber));
                throw OrderException.buildException(OrderGoodsErrorCode.O_ORDER_GOODS_SERIAL_NUMBER_DISAGREE_ERP_CODE,
                    MessageUtils.get(OrderGoodsErrorCode.O_ORDER_GOODS_SERIAL_NUMBER_DISAGREE_ERP_CODE));
            }
            // 重新设置一下ERP单号
            dtoList.forEach(orderGoodsStockSimpleDTO -> {
                orderGoodsStockSimpleDTO.setOrderErpNo(orderErpNoLineNumber.getOrderErpNo());
            });
            list.addAll(dtoList);
        }
        return list;
    }

    public List<OrderGoodsStockQryDTO> listDTOByCondition(OrderGoodsStockQry orderGoodsStockQry) {
        List<OrderGoodsStockDTO2> list = orderGoodsStockDOMapper.listDTOByCondition(orderGoodsStockQry.getWarehouseId(),
            orderGoodsStockQry.getLockType(), orderGoodsStockQry.getItemId());
        return BeanUtils.copyList(list, OrderGoodsStockQryDTO.class);
    }

    public List<OrderGoodsStockDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderGoodsStockDOMapper.listByOrderIds(orderIdList);
    }

    public List<OrderGoodsStockDO> listByOrderId(Integer orderId) {
        return orderGoodsStockDOMapper.listByOrderId(orderId);
    }

    public List<OrderGoodsStockDO> sumByWarehouseIdAndLockTypeGroupByItemId(Short lockType, Integer warehousesId,
        Integer itemId) {
        return orderGoodsStockDOMapper.sumByWarehouseIdAndLockTypeGroupByItemId(lockType, warehousesId, itemId);
    }

    public List<OrderGoodsStockDO> sumByWarehouseIdAndByItemIds(List<Integer> itemIds) {
        return orderGoodsStockDOMapper.sumByItemIds(itemIds);
    }
}
