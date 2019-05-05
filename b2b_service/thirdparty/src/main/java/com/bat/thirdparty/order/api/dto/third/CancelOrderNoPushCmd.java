package com.bat.thirdparty.order.api.dto.third;

import lombok.Data;

import java.io.Serializable;

@Data
public class CancelOrderNoPushCmd implements Serializable {

    //第三方单号
    private String otherOrderNo;

    /**
     * bat单号
     */
    private String orderNo;

    /**
     * 1表示取消;null或者其他值则不表示取消
     */
    private Short cancelFlag;
}
