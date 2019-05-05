package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class OrderSignInResponse {
    /**
     * API接收成功/失败
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
         * 返回结果 Y-成功；N-失败
         */
        private String returnCode;
    }
}
