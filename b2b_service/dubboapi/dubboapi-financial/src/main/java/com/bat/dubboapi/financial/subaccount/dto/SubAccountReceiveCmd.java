package com.bat.dubboapi.financial.subaccount.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SubAccountReceiveCmd implements Serializable {
    private static final long serialVersionUID = -2908959323224174478L;

    /**
     * 商户号
     */
    private String merchantNumber;

    /**
     * 业务员openId
     */
    private String openId;

    /**
     * 分账金额、保留两位小数
     */
    private BigDecimal amount;

    /**
     * 分账等级id
     */
    private Integer levelId;


    private String levelName;


    private Integer salemanId;

    private String salemanName;

    private BigDecimal ratio;

}
