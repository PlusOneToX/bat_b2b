package com.bat.thirdparty.erp.service.executor;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.order.order.dto.OrderCheckCmd;
import com.bat.thirdparty.erp.api.request.ErpOrderChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderCheckRequest;
import com.bat.thirdparty.erp.convertor.ErpOrderConvertor;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/11 12:01
 */
@Component
public class OrderExe {

    @Resource
    private OrderRpcExe orderRpcExe;
    @Resource
    private ErpOrderConvertor erpOrderConvertor;

    /**
     * ERP订单变更
     * 
     * @param request
     */
    public void orderChange(ErpOrderChangeRequest request) {
        ErpOrderChangeCmd changeCmd = erpOrderConvertor.toErpOrderChangeCmd(request);
        orderRpcExe.orderChangeByErp(changeCmd);
    }

    /**
     * ERP订单状态变更(包含1 未确认(反审核), 2 确认(审核通过), 5 取消(作废,关闭))
     * 
     * @param request
     */
    public void orderCheckByErp(ErpOrderCheckRequest request) {
        OrderCheckCmd checkCmd = erpOrderConvertor.toOrderCheckCmd(request);
        orderRpcExe.orderCheckByErp(checkCmd);
    }

}
