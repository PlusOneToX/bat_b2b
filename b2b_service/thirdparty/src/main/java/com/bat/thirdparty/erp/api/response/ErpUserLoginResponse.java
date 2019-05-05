package com.bat.thirdparty.erp.api.response;


import com.bat.dubboapi.thirdparty.common.Response;

/**
 * Created by apple on 2019/7/10.
 */
public class ErpUserLoginResponse extends Response {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
