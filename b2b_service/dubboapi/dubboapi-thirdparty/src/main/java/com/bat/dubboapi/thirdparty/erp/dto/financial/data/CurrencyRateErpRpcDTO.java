package com.bat.dubboapi.thirdparty.erp.dto.financial.data;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/18 18:33
 */
@Data
public class CurrencyRateErpRpcDTO implements Serializable {

    /**
     * rateId
     */
    private Integer rateId;

    /**
     * 直接汇率
     */
    private Double exchangeRate;

    /**
     * 间接汇率
     */
    private Double reverseExRate;

    /**
     * 原币
     */
    private String cyForid;

    /**
     * 目标币
     */
    private String cyToid;

    /**
     * 生效时间
     */
    private Date begDate;

    /**
     * 失效时间
     */
    private Date endDate;

    /**
     * 审核状态
     */
    private String documentStatus;

}
