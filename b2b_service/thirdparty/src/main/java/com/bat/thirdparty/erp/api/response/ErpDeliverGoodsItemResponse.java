package com.bat.thirdparty.erp.api.response;

import com.bat.dubboapi.order.common.ResponseBaseBean;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverGoodsItemEntry;

import java.util.List;

/**
 * Created by apple on 2019/7/11.
 */
public class ErpDeliverGoodsItemResponse extends ResponseBaseBean {

    private List<ErpDeliverGoodsItemEntry> deliverDetails;

    public List<ErpDeliverGoodsItemEntry> getDeliverDetails() {
        return deliverDetails;
    }

    public void setDeliverDetails(List<ErpDeliverGoodsItemEntry> deliverDetails) {
        this.deliverDetails = deliverDetails;
    }
}
