package com.bat.thirdparty.alibaba.dingtalk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 17:22
 */
@NoArgsConstructor
@Data
public class UserListIdResp {

    /**
     * errcode : 0 errmsg : ok result : {"userid_list":["usxxx","manager4xxx","10203029011xxxx","usexxx"]} request_id :
     * 3naksldjh0dk
     */

    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("errmsg")
    private String errmsg;
    @JsonProperty("result")
    private ResultDTO result;
    @JsonProperty("request_id")
    private String requestId;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @JsonProperty("userid_list")
        private List<String> useridList;
    }
}
