package com.bat.dubboapi.thirdparty.xxljob.api;

import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.dubboapi.thirdparty.common.Response;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/2 10:51
 */
public interface XxlJobServiceRpc {
    /**
     * 创建定时任务
     *
     * @param cmds
     * @return
     */
    Response xxlJobAdd(List<XxlJobRpcCmd> cmds);

    /**
     * 保存同步工厂定时任务
     */
    Response saveSyncFactoryXxlJob(List<String> timeList);
}
