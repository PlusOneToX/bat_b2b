package com.bat.thirdparty.factory.haixing.bak;

import lombok.Data;
/**
 * 订单同步到海星（脚本类-放同一个包方便管理）
 */
@Data
public class HaiXingRequest {

    private Integer orderId;

    private String sku;

    private  String pb;
}
