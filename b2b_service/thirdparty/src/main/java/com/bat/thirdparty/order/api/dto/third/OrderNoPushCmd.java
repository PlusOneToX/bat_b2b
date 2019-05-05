package com.bat.thirdparty.order.api.dto.third;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderNoPushCmd implements Serializable {
    //订单信息
    /**
     * 原来的系统的单号
     */
    private String jiujiNo;
    /**
     * 原来系统的bat单号
     */
    private String luokeNo;

    //第三方单号
    private String otherOrderNo;

    /**
     * bat单号
     */
    private String orderNo;

}
