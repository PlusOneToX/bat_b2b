package com.bat.thirdparty.alibaba.dingtalk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 19:24
 */
@NoArgsConstructor
@Data
public class UserDetailListResp {

    /**
     * errcode : 0 result :
     * {"next_cursor":"10","has_more":"true","list":[{"dept_order":1,"leader":"true","extension":"{\"爱好\":\"旅游\",\"年龄\":\"24\"}","unionid":"z21HjQlxxxx","boss":"true","exclusive_account":"false","mobile":"18513027676","active":"true","admin":"true","telephone":"010-86bat2019-2345","remark":"备注备注","avatar":"xxx","hide_mobile":"false","title":"技术总监","hired_date":"1597573616828","userid":"zhangsan","work_place":"未来park","org_email":"test@xxx.com","name":"张三","dept_id_list":[2,3,4],"job_number":"4","state_code":"86","email":"test@xxx.com"}]}
     * errmsg : ok request_id : slv9xeyix4mf
     */

    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("result")
    private ResultDTO result;
    @JsonProperty("errmsg")
    private String errmsg;
    @JsonProperty("request_id")
    private String requestId;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        /**
         * next_cursor : 10 has_more : true list :
         * [{"dept_order":1,"leader":"true","extension":"{\"爱好\":\"旅游\",\"年龄\":\"24\"}","unionid":"z21HjQlxxxx","boss":"true","exclusive_account":"false","mobile":"18513027676","active":"true","admin":"true","telephone":"010-86bat2019-2345","remark":"备注备注","avatar":"xxx","hide_mobile":"false","title":"技术总监","hired_date":"1597573616828","userid":"zhangsan","work_place":"未来park","org_email":"test@xxx.com","name":"张三","dept_id_list":[2,3,4],"job_number":"4","state_code":"86","email":"test@xxx.com"}]
         */

        @JsonProperty("next_cursor")
        private String nextCursor;
        @JsonProperty("has_more")
        private String hasMore;
        @JsonProperty("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            /**
             * dept_order : 1 leader : true extension : {"爱好":"旅游","年龄":"24"} unionid : z21HjQlxxxx boss : true
             * exclusive_account : false mobile : 18513027676 active : true admin : true telephone : 010-86bat2019-2345
             * remark : 备注备注 avatar : xxx hide_mobile : false title : 技术总监 hired_date : 1597573616828 userid : zhangsan
             * work_place : 未来park org_email : test@xxx.com name : 张三 dept_id_list : [2,3,4] job_number : 4 state_code :
             * 86 email : test@xxx.com
             */

            @JsonProperty("dept_order")
            private String deptOrder;
            @JsonProperty("leader")
            private String leader;
            @JsonProperty("extension")
            private String extension;
            @JsonProperty("unionid")
            private String unionid;
            @JsonProperty("boss")
            private String boss;
            @JsonProperty("exclusive_account")
            private String exclusiveAccount;
            @JsonProperty("mobile")
            private String mobile;
            @JsonProperty("active")
            private String active;
            @JsonProperty("admin")
            private String admin;
            @JsonProperty("telephone")
            private String telephone;
            @JsonProperty("remark")
            private String remark;
            @JsonProperty("avatar")
            private String avatar;
            @JsonProperty("hide_mobile")
            private String hideMobile;
            @JsonProperty("title")
            private String title;
            @JsonProperty("hired_date")
            private String hiredDate;
            @JsonProperty("userid")
            private String userid;
            @JsonProperty("work_place")
            private String workPlace;
            @JsonProperty("org_email")
            private String orgEmail;
            @JsonProperty("name")
            private String name;
            @JsonProperty("dept_id_list")
            private List<String> deptIdList;
            @JsonProperty("job_number")
            private String jobNumber;
            @JsonProperty("state_code")
            private String stateCode;
            @JsonProperty("email")
            private String email;
        }
    }
}
