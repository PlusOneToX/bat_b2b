package com.bat.dubboapi.thirdparty.erp.dto.goods;

import java.io.Serializable;

/**
 * erp接口请求参数
 */
public class ErpPriceFieldListRpcQry implements Serializable {

    private Integer id;
    private String erpField;

    public String getErpField() {
        return erpField;
    }

    public void setErpField(String erpField) {
        this.erpField = erpField;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
