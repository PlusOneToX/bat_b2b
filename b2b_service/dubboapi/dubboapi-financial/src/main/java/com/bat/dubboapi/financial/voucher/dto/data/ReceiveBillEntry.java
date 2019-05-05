package com.bat.dubboapi.financial.voucher.dto.data;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/17 21:25
 */
public class ReceiveBillEntry implements Serializable {
    private String FSETTLETYPEID = ""; // 结算方式,固定
    private String FPURPOSEID = "SFKYT02_SYS"; // 收款用途，默认预收款
    private Byte FRECEIVEITEMTYPE = 1; // 1.销售订单收款 2.分销商充值
    private Double FRECTOTALAMOUNTFOR; // 应收金额,小数
    private String FACCOUNTID = ""; // 收款的银行账号，用于测试的账号：10863000000410707
    private String FRECEIVEITEM = "";// 销售订单号
    private Double FHANDLINGCHARGEFOR = 0.00D;

    public String getFSETTLETYPEID() {
        return FSETTLETYPEID;
    }

    public void setFSETTLETYPEID(String FSETTLETYPEID) {
        this.FSETTLETYPEID = FSETTLETYPEID;
    }

    public String getFPURPOSEID() {
        return FPURPOSEID;
    }

    public void setFPURPOSEID(String FPURPOSEID) {
        this.FPURPOSEID = FPURPOSEID;
    }

    public Byte getFRECEIVEITEMTYPE() {
        return FRECEIVEITEMTYPE;
    }

    public void setFRECEIVEITEMTYPE(Byte FRECEIVEITEMTYPE) {
        this.FRECEIVEITEMTYPE = FRECEIVEITEMTYPE;
    }

    public Double getFRECTOTALAMOUNTFOR() {
        return FRECTOTALAMOUNTFOR;
    }

    public void setFRECTOTALAMOUNTFOR(Double FRECTOTALAMOUNTFOR) {
        this.FRECTOTALAMOUNTFOR = FRECTOTALAMOUNTFOR;
    }

    public String getFACCOUNTID() {
        return FACCOUNTID;
    }

    public void setFACCOUNTID(String FACCOUNTID) {
        this.FACCOUNTID = FACCOUNTID;
    }

    public String getFRECEIVEITEM() {
        return FRECEIVEITEM;
    }

    public void setFRECEIVEITEM(String fRECEIVEITEM) {
        FRECEIVEITEM = fRECEIVEITEM;
    }

    public Double getFHANDLINGCHARGEFOR() {
        return FHANDLINGCHARGEFOR;
    }

    public void setFHANDLINGCHARGEFOR(Double fHANDLINGCHARGEFOR) {
        FHANDLINGCHARGEFOR = fHANDLINGCHARGEFOR;
    }

    @Override
    public String toString() {
        return "ReceiveBillEntry{" + "FSETTLETYPEID='" + FSETTLETYPEID + '\'' + ", FPURPOSEID='" + FPURPOSEID + '\''
            + ", FRECEIVEITEMTYPE=" + FRECEIVEITEMTYPE + ", FRECTOTALAMOUNTFOR=" + FRECTOTALAMOUNTFOR + ", FACCOUNTID='"
            + FACCOUNTID + '\'' + ", FRECEIVEITEM='" + FRECEIVEITEM + '\'' + ", FHANDLINGCHARGEFOR="
            + FHANDLINGCHARGEFOR + '}';
    }
}
