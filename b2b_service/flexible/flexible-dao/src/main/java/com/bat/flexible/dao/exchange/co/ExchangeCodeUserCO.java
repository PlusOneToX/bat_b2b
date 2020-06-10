package com.bat.flexible.dao.exchange.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExchangeCodeUserCO implements Serializable {

    private static final long serialVersionUID = 6595709401068801546L;

    private Integer id;


    private Integer exchangeId;


    private String plainCode;


    private String secretCode;


    private Date createTime;


    private Short status;


    private String codeName;


    private Long startTime;


    private Long endTime;


    private String headImg;

    /**
     * 可以选择此兑换卡 0否 1是
     */
    private Short canSelect;

    /**
     * 已经选择此兑换卡 0否 1是
     */

    private Short HasSelect;

    @ApiModelProperty(value = "快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）")
    private Short mailSetting;

    @ApiModelProperty(value = "运费")
    private BigDecimal mailFee;


}
