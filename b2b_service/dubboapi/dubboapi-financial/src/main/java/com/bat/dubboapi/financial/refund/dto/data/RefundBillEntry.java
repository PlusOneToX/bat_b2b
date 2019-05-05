package com.bat.dubboapi.financial.refund.dto.data;

import java.io.Serializable;

public class RefundBillEntry implements Serializable {
    private String FSETTLETYPEID = ""; // 结算方式,固定
    private String FPURPOSEID; // 原收款用途，收款用途，订单收款:SFKYT01_SYS，充值收款:SFKYT02_SYS
    private Double FREFUNDAMOUNTFOR; // 应退金额，小数
    private Double FREALREFUNDAMOUNTFOR_D; // 实退金额，小数
    private String FNOTE = ""; // 备注，必填
    private String FORDERBILLNO = ""; // 销售订单的编号
    private String FACCOUNTID = ""; // 收款的银行账号，用于测试的账号：10863000000410707
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

    public Double getFREFUNDAMOUNTFOR() {
        return FREFUNDAMOUNTFOR;
    }

    public void setFREFUNDAMOUNTFOR(Double FREFUNDAMOUNTFOR) {
        this.FREFUNDAMOUNTFOR = FREFUNDAMOUNTFOR;
    }

    public Double getFREALREFUNDAMOUNTFOR_D() {
        return FREALREFUNDAMOUNTFOR_D;
    }

    public void setFREALREFUNDAMOUNTFOR_D(Double FREALREFUNDAMOUNTFOR_D) {
        this.FREALREFUNDAMOUNTFOR_D = FREALREFUNDAMOUNTFOR_D;
    }

    public String getFNOTE() {
        return FNOTE;
    }

    public void setFNOTE(String FNOTE) {
        this.FNOTE = FNOTE;
    }

    public String getFORDERBILLNO() {
        return FORDERBILLNO;
    }

    public void setFORDERBILLNO(String FORDERBILLNO) {
        this.FORDERBILLNO = FORDERBILLNO;
    }

    public String getFACCOUNTID() {
        return FACCOUNTID;
    }

    public void setFACCOUNTID(String FACCOUNTID) {
        this.FACCOUNTID = FACCOUNTID;
    }

    public Double getFHANDLINGCHARGEFOR() {
        return FHANDLINGCHARGEFOR;
    }

    public void setFHANDLINGCHARGEFOR(Double fHANDLINGCHARGEFOR) {
        FHANDLINGCHARGEFOR = fHANDLINGCHARGEFOR;
    }

    @Override
    public String toString() {
        return "RefundBillEntry{" +
                "FSETTLETYPEID='" + FSETTLETYPEID + '\'' +
                ", FPURPOSEID='" + FPURPOSEID + '\'' +
                ", FREFUNDAMOUNTFOR=" + FREFUNDAMOUNTFOR +
                ", FREALREFUNDAMOUNTFOR_D=" + FREALREFUNDAMOUNTFOR_D +
                ", FNOTE='" + FNOTE + '\'' +
                ", FORDERBILLNO='" + FORDERBILLNO + '\'' +
                ", FACCOUNTID='" + FACCOUNTID + '\'' +
                ", FHANDLINGCHARGEFOR=" + FHANDLINGCHARGEFOR +
                '}';
    }
}
