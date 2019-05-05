package com.bat.dubboapi.financial.refund.dto.data;

import java.io.Serializable;

/**
 * Created by apple on 2019/7/15.
 */
public class ErpRefundBillDetailsDTO  implements Serializable {

    private String refundId;
    private String refundBillNo;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundBillNo() {
        return refundBillNo;
    }

    public void setRefundBillNo(String refundBillNo) {
        this.refundBillNo = refundBillNo;
    }

    @Override
    public String toString() {
        return "ErpRefundBillDetailsDTO{" +
                "refundId='" + refundId + '\'' +
                ", refundBillNo='" + refundBillNo + '\'' +
                '}';
    }
}
