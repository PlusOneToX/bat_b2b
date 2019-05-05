package com.bat.dubboapi.flexible.shop.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class ShopDTORpcQry implements Serializable {


    private static final long serialVersionUID = 623323408658634933L;
    private Integer id;

    private Integer distributorId;

    private String distributorName;

    private String companyName;

    private String shopName;


    private String shopCode;

    private Integer wxPlatformId;


    private Short openFlag;

    private String remark;

    private String url;

    private String qrUrl;

    private String extendParam;


    private String thirdQrUrl;


    private String appId;


    private String appName;


    private Short source;

    private Short type;


    private String platform;


    private String appSecret;


    private Integer salemanId;


    private String salemanName;


    private Integer userConfigId;


    private String userConfigName;
}