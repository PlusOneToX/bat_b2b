package com.bat.thirdparty.erp.api.request;

public class CloseSaleOrderRequest extends BaseTokenRequest {

    @Override
    public String getPath() {
        return "CloseSalesOrder";
    }

    private String FBILLNO;

    private String Note;

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
