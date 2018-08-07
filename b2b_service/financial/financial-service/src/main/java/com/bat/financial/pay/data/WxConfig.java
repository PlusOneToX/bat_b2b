package com.bat.financial.pay.data;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/1 12:00
 */
@Data
public class WxConfig {
    private String appId;
    private String appKey;
    private String accountNo;
    private String apiclientKey;
    private String serialNumber;
    private Short backType;

    private String subMchid;
}
