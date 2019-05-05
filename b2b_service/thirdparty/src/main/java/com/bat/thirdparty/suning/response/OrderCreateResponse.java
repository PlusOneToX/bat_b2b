package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class OrderCreateResponse extends ActiveBaseResponse {

    /**
     * 服务订单号
     */
    private String orderId;

}
