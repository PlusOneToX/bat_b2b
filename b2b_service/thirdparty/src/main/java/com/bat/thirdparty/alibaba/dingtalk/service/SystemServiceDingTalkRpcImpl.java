package com.bat.thirdparty.alibaba.dingtalk.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.alibaba.dingtalk.service.executor.ErrorCode;
import com.bat.thirdparty.alibaba.dingtalk.service.executor.SystemDingTalkQryExc;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.ThirdPartySystemDingTalkServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.dingtalk.api.dto.UserDetailListResp;
import com.bat.thirdparty.common.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/26 15:58
 */
@DubboService
@Slf4j
public class SystemServiceDingTalkRpcImpl implements ThirdPartySystemDingTalkServiceRpc {

    @Resource
    private SystemDingTalkQryExc systemQryExc;

    private static final String DING_TALK_DEPARTMENT_KEY = "dingTalkDepartment";

    @Override
    public Response<List<UserDetailListResp.ResultDTO.ListDTO>> listDepartment() {
        List<UserDetailListResp.ResultDTO.ListDTO> listDTOS = null;
        try {
            long start = System.currentTimeMillis();
            listDTOS = systemQryExc.listDepartment(DING_TALK_DEPARTMENT_KEY);
            long end = System.currentTimeMillis();
            log.info("cost time:{}", end - start);
            return Response.of(listDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_DING_TALK_QUERY_ERROR, MessageUtils.get(ErrorCode.B_DING_TALK_QUERY_ERROR));
        }
    }

}
