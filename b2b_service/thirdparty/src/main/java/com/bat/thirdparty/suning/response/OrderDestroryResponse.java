package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class OrderDestroryResponse {
    /**
     * API接收成功/失败 true成功；false失败
     */
    private Boolean success;

    /**
     * 报错信息
     */
    private String message;

    /**
     * 商户返回报文内容
     */
    private MsgData data;

    @Data
    public static class MsgData {
        /**
         * 返回结果 S-成功；F-失败；
         */
        private String returnCode;
    }
}
