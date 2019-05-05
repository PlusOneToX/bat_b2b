package com.bat.thirdparty.factory.haixing.service.dto;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/11 12:07
 */
@Data
public class HaixingDeliverOrderResp {
    /**
     * 	事件消息ID（唯一）
     */
    private String msgId;
    /**
     * 	时间戳（秒）
     */
    private String timestamp;
    /**
     * 事件编码（字符串）
     */
    private String eventCode;
    /**
     * 	事件数据（JSON字符串）
     */
    private String data;
    /**
     * 	签名（32位大写字符串）
     */
    private String sign;
}
