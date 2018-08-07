package com.bat.financial.pay.data.kuaiqian;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/25 11:41
 */
@NoArgsConstructor
@Data
public class KuaiQianQueryTradeReqDTO {

    /**
     * inputCharset : 1 version : v2.0 signType : 2 merchantAcctId : 1001162931901 queryType : 1 queryMode : 1 startTime
     * : 20120601000000 endTime : 20120606000000 signMsg :
     */

    @JsonProperty("inputCharset")
    private String inputCharset;
    @JsonProperty("version")
    private String version;
    @JsonProperty("signType")
    private String signType;
    @JsonProperty("merchantAcctId")
    private String merchantAcctId;
    @JsonProperty("queryType")
    private String queryType;
    @JsonProperty("queryMode")
    private String queryMode;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("endTime")
    private String endTime;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("requestPage")
    private String requestPage;
    @JsonProperty("signMsg")
    private String signMsg;
}
