package com.bat.dubboapi.flexible.exchange.dto;



import java.io.Serializable;
import java.util.List;

public class ExchangePlainCodeChangeErp implements Serializable {

    private static final long serialVersionUID = 8861422006447746277L;
    /**
     * 明码
     */
    private List<String> MCodes;

    /**
     * 使用时间
     */
    private String F_UseDate;

    /**
     * 兑换人
     */
    private String F_UseName;

    /**
     * 兑换订单号
     */
    private String F_OrderNo;

    /**
     *
     */
    private String F_UseCustId;

    /**
     * 兑换物料
     */
    private String F_UseMateID;

    public List<String> getMCodes() {
        return MCodes;
    }

    public void setMCodes(List<String> MCodes) {
        this.MCodes = MCodes;
    }

    public String getF_UseDate() {
        return F_UseDate;
    }

    public void setF_UseDate(String f_UseDate) {
        F_UseDate = f_UseDate;
    }

    public String getF_UseName() {
        return F_UseName;
    }

    public void setF_UseName(String f_UseName) {
        F_UseName = f_UseName;
    }

    public String getF_OrderNo() {
        return F_OrderNo;
    }

    public void setF_OrderNo(String f_OrderNo) {
        F_OrderNo = f_OrderNo;
    }

    public String getF_UseCustId() {
        return F_UseCustId;
    }

    public void setF_UseCustId(String f_UseCustId) {
        F_UseCustId = f_UseCustId;
    }

    public String getF_UseMateID() {
        return F_UseMateID;
    }

    public void setF_UseMateID(String f_UseMateID) {
        F_UseMateID = f_UseMateID;
    }


    public String getPath() {
        return "RockCardChange";
    }
}
