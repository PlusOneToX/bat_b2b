package com.bat.order.api.deliver.dto;

import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import lombok.Data;

import java.util.List;

@Data
public class OrderDeliverBillResp {

    /**
     * 流水
     */
    private OrderDeliverBillDO orderDeliverBillDO;

    /**
     * 流水明细
     */
    private List<OrderDeliverBillDetailDO> deliverBillDetailDOList;


}
