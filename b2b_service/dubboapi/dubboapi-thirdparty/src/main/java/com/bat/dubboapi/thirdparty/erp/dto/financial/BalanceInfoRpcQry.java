package com.bat.dubboapi.thirdparty.erp.dto.financial;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/28 11:24
 */
public class BalanceInfoRpcQry implements Serializable {
    private List<String> FCUSTID;

    public List<String> getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(List<String> FCUSTID) {
        this.FCUSTID = FCUSTID;
    }
}
