package com.bat.thirdparty.erp.api.request;


import java.util.List;

public class BalanceInfoRequest {

    private List<String> FCUSTID;//分销商内码

    public List<String> getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(List<String> FCUSTID) {
        this.FCUSTID = FCUSTID;
    }


}
