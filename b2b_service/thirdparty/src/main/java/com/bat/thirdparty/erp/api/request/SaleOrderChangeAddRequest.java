package com.bat.thirdparty.erp.api.request;


import com.bat.thirdparty.erp.api.request.dto.SaleOrderChangeAddEntry;

/**
 * Created by apple on 2019/11/12.
 */
public class SaleOrderChangeAddRequest {


    private SaleOrderChangeAddEntry Model;

    private String IsAutoSubmitAndAudit = "true";

    public SaleOrderChangeAddEntry getModel() {
        return Model;
    }

    public void setModel(SaleOrderChangeAddEntry model) {
        Model = model;
    }

    public String getIsAutoSubmitAndAudit() {
        return IsAutoSubmitAndAudit;
    }

    public void setIsAutoSubmitAndAudit(String isAutoSubmitAndAudit) {
        IsAutoSubmitAndAudit = isAutoSubmitAndAudit;
    }
}
