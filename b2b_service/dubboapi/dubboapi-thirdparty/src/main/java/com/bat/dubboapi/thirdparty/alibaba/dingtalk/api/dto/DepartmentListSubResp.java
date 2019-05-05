package com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 17:16
 */
@NoArgsConstructor
@Data
public class DepartmentListSubResp implements Serializable {

    /**
     * errcode : 0 errmsg : ok result :
     * [{"auto_add_user":true,"create_dept_group":true,"dept_id":"37xxxx95","name":"市场部","parent_id":1},{"auto_add_user":true,"create_dept_group":true,"dept_id":"399xxxx96","name":"财务部","parent_id":1}]
     * request_id : 5um7ykyaalsj
     */

    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("errmsg")
    private String errmsg;
    @JsonProperty("result")
    private List<ResultDTO> result;
    @JsonProperty("request_id")
    private String requestId;

    @NoArgsConstructor
    @Data
    public static class ResultDTO implements Serializable {
        /**
         * auto_add_user : true create_dept_group : true dept_id : 37xxxx95 name : 市场部 parent_id : 1
         */

        @JsonProperty("auto_add_user")
        private Boolean autoAddUser;
        @JsonProperty("create_dept_group")
        private Boolean createDeptGroup;
        @JsonProperty("dept_id")
        private String deptId;
        @JsonProperty("name")
        private String name;
        @JsonProperty("parent_id")
        private Integer parentId;
    }
}
