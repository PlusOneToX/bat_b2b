package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExchangeCodeUseCmd  implements Serializable {
    private static final long serialVersionUID = 5585975811227503076L;

    //订单id
    private Integer orderId;

    /**
     * C端用户id
     */
    private Integer userId;

    /**
     * C端用户名称
     */
    private String userName;

    /**
     * 核销订单商品明细ID
     */
    private Integer orderGoodId;

    /**
     * 暗码列表
     */
    private List<String> secretCodeList;
}
