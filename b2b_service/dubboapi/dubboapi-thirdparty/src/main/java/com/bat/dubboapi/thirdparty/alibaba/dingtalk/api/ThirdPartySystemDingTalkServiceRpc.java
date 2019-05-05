package com.bat.dubboapi.thirdparty.alibaba.dingtalk.api;

import java.util.List;

import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.UserDetailListResp;
import com.bat.dubboapi.thirdparty.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 15:57
 */
public interface ThirdPartySystemDingTalkServiceRpc {

    Response<List<UserDetailListResp.ResultDTO.ListDTO>> listDepartment();
}
