package com.bat.financial.api.basesetting.dto.data;

import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:15
 */
@Data
public class CurrencyDTO {
    private Integer id;

    private String name;

    private String currencyCode;

    private Short moneyAccuracy;

    private String erpNo;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}
