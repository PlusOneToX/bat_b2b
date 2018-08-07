package com.bat.financial.api.pay.dto.wechat;

import lombok.Data;

@Data
public class WxPaySceneInfo {

    /**
     * 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     */
    private String payer_client_ip;

    /**
     *商户端设备号（门店号或收银设备ID）
     */
    private String device_id;
}
