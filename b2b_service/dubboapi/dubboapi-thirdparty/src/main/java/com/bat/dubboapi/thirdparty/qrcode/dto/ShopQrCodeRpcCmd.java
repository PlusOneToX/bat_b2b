package com.bat.dubboapi.thirdparty.qrcode.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/20 8:50
 */
public class ShopQrCodeRpcCmd implements Serializable {
    private String url;
    private String shopCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
