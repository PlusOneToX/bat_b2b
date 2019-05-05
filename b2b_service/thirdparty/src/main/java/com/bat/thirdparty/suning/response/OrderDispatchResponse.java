package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class OrderDispatchResponse {
    /**
     * API接收成功/失败
     */
    private Boolean success;

    /**
     * 商户返回报文内容
     */
    private MsgData data;

    @Data
    public static class MsgData {
        /**
         * 服务订单号
         */
        private String orderId;
        /**
         * 处理状态
         */
        private String returnCode;
        /**
         * 报错信息
         */
        private String message;
        /**
         * 作业人员一代码
         */
        private String zyry1Bp;
    }
}
