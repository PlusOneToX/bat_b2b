package com.bat.thirdparty.factory.maike.service.dto;

import com.bat.thirdparty.factory.maike.request.devlivery.ApiOrderDeliveryCallbackModelRequest;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/11 12:07
 */
@Data
public class MaikeDeliverOrderResp {
    private String signature;
    private String timestamp;
    private ApiOrderDeliveryCallbackModelRequest request;
}
