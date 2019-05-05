package com.bat.thirdparty.xxljob.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.xxljob.service.executor.FlexibleJobExe;
import com.bat.thirdparty.xxljob.service.executor.PromotionExe;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/2 11:19
 */
@DubboService
public class XxlJobServiceRpcImpl implements XxlJobServiceRpc {
    @Resource
    private PromotionExe promotionExe;

    @Resource
    private FlexibleJobExe flexibleJobExe;

    @Override
    public Response xxlJobAdd(List<XxlJobRpcCmd> cmds) {
        promotionExe.xxlJobAdd(cmds);
        return Response.buildSuccess();
    }

    /**
     * 保存同步工厂定时任务
     * 
     * @param timeList
     * @return
     */
    @Override
    public Response saveSyncFactoryXxlJob(List<String> timeList) {
        flexibleJobExe.saveSyncFactoryXxlJob(timeList);
        return Response.buildSuccess();
    }
}
