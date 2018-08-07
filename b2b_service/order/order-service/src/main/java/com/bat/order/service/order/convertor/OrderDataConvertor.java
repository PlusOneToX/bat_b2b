package com.bat.order.service.order.convertor;

import java.util.Date;
import java.util.List;

import com.bat.order.service.common.enumtype.OrderStatus;
import org.springframework.util.CollectionUtils;

import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;

public class OrderDataConvertor {

    public static void toOrderDataCancel(OrderCustomerDataDO customerDataDO,
        List<OrderDistributorDataDO> distributorDataDOS, Date time, String remark) {
        if (customerDataDO != null) {
            customerDataDO.setOrderStatus(OrderStatus.CANCELLED.getValue());
            // customerDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
            customerDataDO.setUpdateTime(time);
            customerDataDO.setCancelRemark(remark);
        }
        if (!CollectionUtils.isEmpty(distributorDataDOS)) {
            distributorDataDOS.forEach(distributorDataDO -> {
                distributorDataDO.setOrderStatus(OrderStatus.CANCELLED.getValue());
                // distributorDataDO.setPayStatus(PayStatus.APPLY_REFUND.getValue());
                distributorDataDO.setUpdateTime(time);
                distributorDataDO.setCancelRemark(remark);
            });
        }
    }
}
