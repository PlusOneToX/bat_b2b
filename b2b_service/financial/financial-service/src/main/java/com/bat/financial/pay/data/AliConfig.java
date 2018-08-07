package com.bat.financial.pay.data;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/5 21:31
 */
@Data
public class AliConfig {

    private String appId;

    private String appPublicSecret;

    private String appPrivateSecret;

    private Short backType;
}
