package com.bat.thirdparty.erp.api.response;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.thirdparty.erp.api.response.dto.ErpVoucherDetails;

import java.util.List;

/**
 * Created by apple on 2019/7/15.
 */
public class ErpVoucherDetailsResponse extends Response {

    private List<ErpVoucherDetails> voucherDetails;

    public List<ErpVoucherDetails> getVoucherDetails() {
        return voucherDetails;
    }

    public void setVoucherDetails(List<ErpVoucherDetails> voucherDetails) {
        this.voucherDetails = voucherDetails;
    }
}
