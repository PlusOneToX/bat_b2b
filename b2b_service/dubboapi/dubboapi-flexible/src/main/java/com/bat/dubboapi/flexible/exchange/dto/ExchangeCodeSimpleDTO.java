package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExchangeCodeSimpleDTO implements Serializable {

    private static final long serialVersionUID = -6206797357962454219L;
    private Integer exchangeId;

    private String secretCode;

    /**
     * 归属B2B订单分销商id
     */
    private Integer b2bDistributorId;

    /**
     * 分销商名称
     */
    private String distributorName;

    /**
     * 分销商公司名称
     */
    private String distributorCompanyName;

    /**
     * 分销商范围 1、全部可用 2、指定分销商
     */
    private Short distributorScope;

    /**
     * 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
     */
    private Short mailSetting;

    /**
     * 邮费
     */
    private BigDecimal mailFee;

    /**
     * 业务员
     */
    private Integer salesId;
    private String salesName;
}
