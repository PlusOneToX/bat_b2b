package com.bat.dubboapi.financial.pay.dto;

import lombok.Data;

import java.io.Serializable;
import java.security.cert.X509Certificate;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/1 12:00
 */
@Data
public class WxPayConfigQry implements Serializable {
    private static final long serialVersionUID = 4009616052529286181L;
    private String appId;
    private String appKey;
    private String accountNo;
    private String apiclientKey;
    private String serialNumber;
    private Short backType;

    private String subMchid;

    private X509Certificate x509Certificate;
}
