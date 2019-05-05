package com.bat.dubboapi.financial.basesetting.dto.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/17 22:12
 */
@Data
public class AccountBalanceChangeReq implements Serializable {
    private Long erpDistributorId;//分销商内码
    private Short changeType;//变更类型 1.增加，2.减少
    private Double amount;//变动金额
    private Long businessNo;
    private String billNo;//erp单据编号
}

