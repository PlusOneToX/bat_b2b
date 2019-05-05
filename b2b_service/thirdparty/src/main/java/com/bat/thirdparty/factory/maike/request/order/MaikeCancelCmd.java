package com.bat.thirdparty.factory.maike.request.order;


import com.bat.thirdparty.factory.maike.common.MaikeBaseParamCmd;

/**
 * 主动取消麦客订单
 */
public class MaikeCancelCmd extends MaikeBaseParamCmd {

    private String order_number;

    private String remark;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}