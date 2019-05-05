package com.bat.thirdparty.erp.api.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysPUSHOUTSTOCKRequest extends BaseTokenRequest {
    @Override
    public String getPath() {
        return "PUSHOUTSTOCK";
    }

    private String FSOURCEBILLNO;  //K3销售订单单号
    private String FDATE; //YYYY-MM-DD
    private String FBILLNO; //当前单据编码， 不传会自动赋值，建议传一个当前操作ID
    private List<OUTSTOCKNENTRY> OUTSTOCKNENTRY = new ArrayList<>();

    public String FWLGS;//物流公司
    public String FWLDH;//物流单号
    public String FFHRQ;//发货日期

    //是否直发（针对标品）1、是 0、否
    private String F_SOZF;

    public String getFSOURCEBILLNO() {
        return FSOURCEBILLNO;
    }

    public void setFSOURCEBILLNO(String FSOURCEBILLNO) {
        this.FSOURCEBILLNO = FSOURCEBILLNO;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFBILLNO() {
        return FBILLNO;
    }

    public void setFBILLNO(String FBILLNO) {
        this.FBILLNO = FBILLNO;
    }

    public List<SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY> getOUTSTOCKNENTRY() {
        return OUTSTOCKNENTRY;
    }

    public void setOUTSTOCKNENTRY(List<SysPUSHOUTSTOCKRequest.OUTSTOCKNENTRY> OUTSTOCKNENTRY) {
        this.OUTSTOCKNENTRY = OUTSTOCKNENTRY;
    }

    public String getFWLGS() {
        return FWLGS;
    }

    public void setFWLGS(String FWLGS) {
        this.FWLGS = FWLGS;
    }

    public String getFWLDH() {
        return FWLDH;
    }

    public void setFWLDH(String FWLDH) {
        this.FWLDH = FWLDH;
    }

    public String getFFHRQ() {
        return FFHRQ;
    }

    public void setFFHRQ(String FFHRQ) {
        this.FFHRQ = FFHRQ;
    }



    public static class OUTSTOCKNENTRY {
        private String FMATERIALID;
        private Long FREALQTY;

        public String getFMATERIALID() {
            return FMATERIALID;
        }

        public void setFMATERIALID(String FMATERIALID) {
            this.FMATERIALID = FMATERIALID;
        }

        public Long getFREALQTY() {
            return FREALQTY;
        }

        public void setFREALQTY(Long FREALQTY) {
            this.FREALQTY = FREALQTY;
        }

        @Override
        public String toString() {
            return "OUTSTOCKNENTRY{" +
                    "FMATERIALID=" + FMATERIALID +
                    ", FREALQTY='" + FREALQTY +
                    '}';
        }
    }
}
