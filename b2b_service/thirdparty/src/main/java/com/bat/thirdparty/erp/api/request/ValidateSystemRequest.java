package com.bat.thirdparty.erp.api.request;

public class ValidateSystemRequest extends BaseRequest {
    private String USERNAME;
    private String PASSWORD;

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    @Override
    public String getPath() {
        return "ValidateSystem";
    }
}
