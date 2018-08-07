package com.bat.financial.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/6 19:47
 */
@NoArgsConstructor
@Data
public class VoucherReq {
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("login_id")
    private String loginId;
    @JsonProperty("taobao_nick")
    private String taobaoNick;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("out_biz_no")
    private String outBizNo;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("memo")
    private String memo;
    @JsonProperty("extend_info")
    private String extendInfo;
}
