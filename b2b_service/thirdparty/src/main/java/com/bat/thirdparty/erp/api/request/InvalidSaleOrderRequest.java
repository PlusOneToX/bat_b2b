package com.bat.thirdparty.erp.api.request;

/**
 * Created by apple on 2019/7/13.
 */
public class InvalidSaleOrderRequest extends BaseTokenRequest {
    @Override
    public String getPath() {
        return "CancelSalesOrder";
    }

    private String FBILLNO;

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }
}
