package com.bat.financial.api.pay.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 14:33
 */
@NoArgsConstructor
@Data
@ToString
public class TradeErrorRespDTO {

    /**
     * code : PARAM_ERROR message : 无效的openid
     */

    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
}
