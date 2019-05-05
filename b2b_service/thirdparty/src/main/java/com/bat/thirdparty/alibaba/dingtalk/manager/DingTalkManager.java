package com.bat.thirdparty.alibaba.dingtalk.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.Cached;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.DepartmentListSubResp;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.UserDetailListResp;
import com.bat.thirdparty.alibaba.dingtalk.config.DingTalkConfig;
import com.bat.thirdparty.alibaba.dingtalk.dto.AccessTokenResp;
import com.bat.thirdparty.alibaba.dingtalk.dto.UserListIdResp;

import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 16:03
 */
@Component
public class DingTalkManager {

    @Autowired
    DingTalkConfig dingTalkConfig;

    @SneakyThrows
    @Cached(name = "thirdparty:dingtalk:DingTalkManager.getToken", key = "dingTalkToken", expire = 3600)
    AccessTokenResp getToken() {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(dingTalkConfig.getAppkey());
        request.setAppsecret(dingTalkConfig.getAppsecret());
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        AccessTokenResp dingTalkAccessToken = JSON.parseObject(response.getBody(), AccessTokenResp.class);
        return dingTalkAccessToken;
    }

    @SneakyThrows
    public DepartmentListSubResp listDepartment(Long deptId) {
        AccessTokenResp token = getToken();
        String access_token = token.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = client.execute(req, access_token);
        return JSON.parseObject(rsp.getBody(), DepartmentListSubResp.class);
    }

    @SneakyThrows
    public UserListIdResp listUser(Long deptId) {
        AccessTokenResp token = getToken();
        String access_token = token.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
        OapiUserListidRequest req = new OapiUserListidRequest();
        req.setDeptId(deptId);
        OapiUserListidResponse rsp = client.execute(req, access_token);
        return JSON.parseObject(rsp.getBody(), UserListIdResp.class);
    }

    @SneakyThrows
    public static void main(String[] args) {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey("dingesufxl21lrzf7kmp");
        request.setAppsecret("R-eMErMQyYyqZblQTtKd5kKEgcv0FzpWzje_bm-Jue3RoqmK6qj6uY2SLxdrP1lb");
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        AccessTokenResp dingTalkAccessToken = JSON.parseObject(response.getBody(), AccessTokenResp.class);
        String access_token = dingTalkAccessToken.getAccessToken();
        // DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
        // OapiUserListidRequest req = new OapiUserListidRequest();
        // req.setDeptId(1L);
        // OapiUserListidResponse rsp = client1.execute(req, access_token);
        // UserListIdResp userListIdResp = JSON.parseObject(rsp.getBody(), UserListIdResp.class);
        // System.out.println(userListIdResp);

        DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
        req.setDeptId(10L);
        req.setCursor(0L);
        req.setSize(10L);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiUserListsimpleResponse rsp = client1.execute(req, access_token);
        System.out.println(rsp.getBody());
    }

    @SneakyThrows
    public List<UserDetailListResp.ResultDTO.ListDTO> listUserDetail(Long deptId, Long cursor, Long size) {
        List<UserDetailListResp.ResultDTO.ListDTO> listDTOS = new ArrayList<>(32);
        UserDetailListResp userDetailListResp = recursionListUserDetail(deptId, cursor, size);
        if (userDetailListResp.getErrcode() == 0) {
            listDTOS.addAll(userDetailListResp.getResult().getList());
            if ("true".equals(userDetailListResp.getResult().getHasMore())) {
                recursionListUserDetail(deptId, cursor + size, size);
            }
            return listDTOS;
        }
        return listDTOS;
    }

    /**
     * 递归 分页
     * 
     * @param deptId
     * @param cursor
     * @param size
     * @return
     */
    @SneakyThrows
    private UserDetailListResp recursionListUserDetail(Long deptId, Long cursor, Long size) {
        AccessTokenResp token = getToken();
        String access_token = token.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/list");
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(deptId);
        req.setCursor(cursor);
        req.setSize(size);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiV2UserListResponse rsp = client.execute(req, access_token);
        return JSON.parseObject(rsp.getBody(), UserDetailListResp.class);
    }
}
