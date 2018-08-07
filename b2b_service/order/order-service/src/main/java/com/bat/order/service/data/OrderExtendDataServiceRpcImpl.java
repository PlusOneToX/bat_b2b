package com.bat.order.service.data;

import java.util.List;

import com.bat.order.service.common.error.OrderCommonErrorCode;
import com.bat.order.service.data.executor.OrderExtendDataCmdExe;
import com.bat.order.service.data.executor.OrderExtendDataQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderExtendDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.data.co.OrderExtendDataCO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;

@DubboService
public class OrderExtendDataServiceRpcImpl implements OrderExtendDataServiceRpc {

    @Autowired
    private OrderExtendDataQryExe orderExtendDataQryExe;

    @Autowired
    private OrderExtendDataCmdExe orderExtendDataCmdExe;

    @Override
    public Response<List<OrderExtendDataSimpleRpcDTO>> listExtendDataSimpleByOrderIdList(List<Integer> orderIdList) {
        if (orderIdList == null || orderIdList.size() == 0) {
            return Response.buildSuccess();
        }
        List<OrderExtendDataCO> orderExtendDataCOList =
            orderExtendDataQryExe.listExtendDataSimpleByOrderIdList(orderIdList);
        List<OrderExtendDataSimpleRpcDTO> list =
            BeanUtils.copyList(orderExtendDataCOList, OrderExtendDataSimpleRpcDTO.class);
        return Response.of(list);
    }

    @Override
    public Response<OrderExtendDataSimpleRpcDTO> getExtendDataSimpleByOrderId(Integer orderId) {
        OrderExtendDataCO orderExtendDataCO = orderExtendDataQryExe.getExtendDataSimpleByOrderId(orderId);
        OrderExtendDataSimpleRpcDTO orderSimpleRpcDTO =
            BeanUtils.copy(orderExtendDataCO, OrderExtendDataSimpleRpcDTO.class);
        return Response.of(orderSimpleRpcDTO);
    }

    @Override
    public Response<OrderExtendDataSimpleRpcDTO> getByOrderFactoryNo(String maikeOrderNo) {
        OrderExtendDataDO orderExtendDataDO = orderExtendDataQryExe.getByOrderFactoryNo(maikeOrderNo);
        return Response.of(BeanUtils.copy(orderExtendDataDO, OrderExtendDataSimpleRpcDTO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response setOtherFactoryNo(Integer orderId, String otherFactoryNo, String manufactors) {
        OrderExtendDataDO orderExtendDataDO = orderExtendDataQryExe.getByOrderId(orderId);
        if (StringUtils.isNotBlank(orderExtendDataDO.getOrderFactoryNo())) {
            return Response.buildFailure(OrderCommonErrorCode.COMMON_OPERATE_REPEAT,
                MessageUtils.get(OrderCommonErrorCode.COMMON_OPERATE_REPEAT));
        }
        orderExtendDataDO.setOrderFactoryNo(otherFactoryNo);
        orderExtendDataDO.setFactoryCode(manufactors);
        orderExtendDataCmdExe.update(orderExtendDataDO);
        return Response.buildSuccess();
    }

}
