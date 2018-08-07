package com.bat.order.service.order;

import java.util.List;

import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.order.convertor.OrderInfoConvertor;
import com.bat.order.service.order.executor.OrderErpCmdExe;
import com.bat.order.service.order.executor.OrderInfoQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderInfoServiceRpc;
import com.bat.dubboapi.order.order.dto.erp.ErpOrderDetailEntryId;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@DubboService
public class OrderInfoServiceRpcImpl implements OrderInfoServiceRpc {

    @Autowired
    private OrderInfoQryExe orderInfoQryExe;

    @Autowired
    private OrderErpCmdExe orderErpCmdExe;

    @Override
    public Response<OrderInfoRpcQryDTO> getById(Integer orderId) {
        OrderInfoDO orderInfoDO = orderInfoQryExe.getById(orderId);
        OrderInfoRpcQryDTO orderInfoRpcQryDTO = OrderInfoConvertor.toOrderInfoRpcQryDTO(orderInfoDO);
        return Response.of(orderInfoRpcQryDTO);
    }

    /**
     * 同步销售单到ERP、处理B2B订单信息
     * 
     * @param entryIds
     * @param orderErpNo
     * @param orderId
     *            B2B订单号
     * @param erpOrderType
     * @param syncVoucherFlag
     *            是否需要发消息同步收款单到ERP
     * @return
     */
    @Override
    public Response<Boolean> setOrderErpMsg(List<ErpOrderDetailEntryId> entryIds, String orderErpNo, Integer orderId,
        String erpOrderType, Boolean syncVoucherFlag) {
        Boolean flag = orderErpCmdExe.setOrderErpMsg(entryIds, orderErpNo, orderId, erpOrderType, syncVoucherFlag);
        return Response.of(flag);
    }

    @Override
    public Response<OrderInfoRpcQryDTO> getByOrderNo(String orderNo) {
        OrderInfoDO orderInfoDO = orderInfoQryExe.getByOrderNo(orderNo);
        if (orderInfoDO == null) {
            return Response.buildFailure(OrderInfoErrorCode.ORDER_NO_ERROR,
                orderNo + MessageUtils.get(OrderInfoErrorCode.ORDER_NO_ERROR));
        }
        OrderInfoRpcQryDTO orderInfoRpcQryDTO = OrderInfoConvertor.toOrderInfoRpcQryDTO(orderInfoDO);
        return Response.of(orderInfoRpcQryDTO);
    }
}
