package com.bat.thirdparty.erp.api.request;

public abstract class BaseTokenRequest extends BaseRequest {

    private String ACCESSTOKEN;

    public String getACCESSTOKEN() {
        return ACCESSTOKEN;
    }

    public void setACCESSTOKEN(String ACCESSTOKEN) {
        this.ACCESSTOKEN = ACCESSTOKEN;
    }
}
