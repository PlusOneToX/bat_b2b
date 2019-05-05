package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class OrderModifyResponse extends ActiveBaseResponse {

    /**
     * 服务订单号
     */
    private String serviceNum;

}
