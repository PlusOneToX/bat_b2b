package com.bat.thirdparty.erp.api.request;

import java.util.ArrayList;
import java.util.List;

public class SysPushtSTKInStockRequest extends BaseTokenRequest {
    @Override
    public String getPath() {
        return "Push_t_STK_InStock";
    }

    private String FSOURCEBILLNO;  //K3销售订单单号
    private String FDATE; //YYYY-MM-DD
    private String FBILLNO; //当前单据编码， 不传会自动赋值，建议传一个当前操作ID
    private List<STKINSTOCKENTRY> STK_INSTOCKENTR = new ArrayList<>();
    private String FSupplierId;// 供应商Id
    private String FStockId;// 仓库Id

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

    public List<STKINSTOCKENTRY> getSTK_INSTOCKENTR() {
        return STK_INSTOCKENTR;
    }

    public void setSTK_INSTOCKENTR(List<STKINSTOCKENTRY> STK_INSTOCKENTR) {
        this.STK_INSTOCKENTR = STK_INSTOCKENTR;
    }

    public String getFSupplierId() {
        return FSupplierId;
    }

    public void setFSupplierId(String FSupplierId) {
        this.FSupplierId = FSupplierId;
    }

    public String getFStockId() {
        return FStockId;
    }

    public void setFStockId(String FStockId) {
        this.FStockId = FStockId;
    }

    @Override
    public String toString() {
        return "CreateSaleOrderRequest{" +
                "FSOURCEBILLNO=" + FSOURCEBILLNO +
                ", FDATE='" + FDATE + '\'' +
                ", FBILLNO='" + FBILLNO + '\'' +
                ", STK_INSTOCKENTR=" + STK_INSTOCKENTR +
                '}';
    }

    public static class STKINSTOCKENTRY {
        private String FMATERIALID;
        private Long FREALQTY;
        private String FNOTE = "定制销售订单采购入库";

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

        public String getFNOTE() {
            return FNOTE;
        }

        public void setFNOTE(String FNOTE) {
            this.FNOTE = FNOTE;
        }

        @Override
        public String toString() {
            return "STKINSTOCKENTRY{" +
                    "FMATERIALID=" + FMATERIALID +
                    ", FREALQTY='" + FREALQTY +
                    '}';
        }
    }
}
