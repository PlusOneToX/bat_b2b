package com.bat.thirdparty.alibaba.dingtalk.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 16:30
 */
@NoArgsConstructor
@Data
public class AccessTokenResp implements Serializable {

    /**
     * errcode : 0 access_token : 330a1c53e41b3e3d9d42896e9a2a163a errmsg : ok expires_in : 7200
     */

    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("errmsg")
    private String errmsg;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
