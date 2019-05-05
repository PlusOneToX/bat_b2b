package com.bat.thirdparty.erp.api.request;

import lombok.Data;

@Data
public class SyncGeneralOutboundCmd extends BaseTokenRequest {
    @Override
    public String getPath() {
        return "PUSHOUTSTOCK";
    }

    private String FSOURCEBILLNO;  //K3销售订单单号
    private String FDATE; //YYYY-MM-DD


    //是否直发（针对标品）1、是 0、否
    private String F_SOZF;


}
