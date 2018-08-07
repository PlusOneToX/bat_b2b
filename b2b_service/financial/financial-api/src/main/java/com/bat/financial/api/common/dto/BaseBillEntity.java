package com.bat.financial.api.common.dto;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/28 9:44
 */
public class BaseBillEntity implements Serializable {
    private String FSETTLETYPEID = ""; // 结算方式,固定
    private String FACCOUNTID = ""; // 收款的银行账号，用于测试的账号：10863000000410707

    public String getFSETTLETYPEID() {
        return FSETTLETYPEID;
    }

    public void setFSETTLETYPEID(String FSETTLETYPEID) {
        this.FSETTLETYPEID = FSETTLETYPEID;
    }

    public String getFACCOUNTID() {
        return FACCOUNTID;
    }

    public void setFACCOUNTID(String FACCOUNTID) {
        this.FACCOUNTID = FACCOUNTID;
    }
}
