package com.bat.dubboapi.financial.voucher.dto.data;

import com.bat.dubboapi.financial.common.BaseResponse;

import java.io.Serializable;

public class CreateReceiveBillEntryResp extends BaseResponse implements Serializable {

    private String Data; // erp的收款单据编号

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
