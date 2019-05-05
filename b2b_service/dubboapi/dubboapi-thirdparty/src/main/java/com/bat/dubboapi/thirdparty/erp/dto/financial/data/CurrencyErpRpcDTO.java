package com.bat.dubboapi.thirdparty.erp.dto.financial.data;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/17 14:47
 */
@Data
public class CurrencyErpRpcDTO implements Serializable {

    /**
     * 币种名称 人民币
     */
    private String name;
    /**
     * 币种编码 PRE001
     */
    private String currencyCode;
    /**
     * 货币精度 2
     */
    private Short moneyAccuracy;
    /**
     * erp编码 CNY
     */
    private String erpNo;
    /**
     * 状态, 1启用,0停用
     */
    private Short openFlag;

}
