package com.bat.thirdparty.erp.api.request;

import com.bat.thirdparty.erp.api.request.dto.ErpVoucherDetailsEntry;

import java.util.List;

/**
 * Created by apple on 2019/7/15.
 */
public class ErpVoucherDetailsRequest {

    private List<ErpVoucherDetailsEntry> voucherDetails;

    public List<ErpVoucherDetailsEntry> getVoucherDetails() {
        return voucherDetails;
    }

    public void setVoucherDetails(List<ErpVoucherDetailsEntry> voucherDetails) {
        this.voucherDetails = voucherDetails;
    }
}
