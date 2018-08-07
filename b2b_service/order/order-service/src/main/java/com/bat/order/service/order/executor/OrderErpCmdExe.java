package com.bat.order.service.order.executor;

import java.util.List;

import com.bat.order.service.message.MessageSendService;
import com.bat.order.service.order.convertor.OrderGoodsConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.order.dto.erp.ErpOrderDetailEntryId;
import com.bat.order.api.data.OrderExtendDataServiceI;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;

@Component
public class OrderErpCmdExe {

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderExtendDataServiceI orderExtendDataServiceI;

    @Autowired
    private MessageSendService messageSendService;

    /**
     * 销售单同步到ERP、处理B2B订单信息
     * 
     * @param entryIds
     * @param orderErpNo
     * @param orderId
     * @param erpOderType
     *            ERP实际的订单类型
     */
    public Boolean setOrderErpMsg(List<ErpOrderDetailEntryId> entryIds, String orderErpNo, Integer orderId,
        String erpOderType, Boolean syncVoucherFlag) {
        OrderExtendDataDO orderExtendDataDO = orderExtendDataServiceI.getByOrderId(orderId);
        orderExtendDataDO.setOrderErpNo(orderErpNo);
        orderExtendDataDO.setOrderErpType(erpOderType);
        orderExtendDataServiceI.update(orderExtendDataDO);
        List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(orderId);
        // 设置B2B的行序号为ERP的内码明细Id、返回是否已同步ERP标记
        Boolean existFlag = OrderGoodsConvertor.updateSerialNumberToErpEntryId(orderGoodsDOList, entryIds);
        orderGoodsServiceI.batchUpdate(orderGoodsDOList);
        if (!syncVoucherFlag) {
            return !existFlag;
        }
        if (existFlag) {
            // 已同步过收款单（根据行序号是否已设置来判断、如果是之前就设置过该订单的行序号、即已同步过收款单）
            return !existFlag;
        }
        // 同步收款单到ERP
        messageSendService.orderVoucherErpNew(orderId);
        // 第一次同步
        return true;
    }
}
