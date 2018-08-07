package com.bat.financial.pay.data.wxv2;

public class WxPayBaseResponse {

    private String return_code;
    private String return_msg;

    public WxPayBaseResponse() {}

    public WxPayBaseResponse(String return_code) {
        this.return_code = return_code;
    }

    public WxPayBaseResponse(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public static WxPayBaseResponse toResponse(String code) {
        return new WxPayBaseResponse(code);
    }

    public static WxPayBaseResponse toResponse(String code, String returnMsg) {
        return new WxPayBaseResponse(code, returnMsg);
    }
}
