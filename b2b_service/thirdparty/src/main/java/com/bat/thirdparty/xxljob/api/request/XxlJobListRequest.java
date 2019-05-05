package com.bat.thirdparty.xxljob.api.request;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/2 11:54
 */
@Data
public class XxlJobListRequest {
    /**
     * 执行器id
     */
    private Integer jobGroup;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * jobHandler
     */
    private String executorHandler;
    /**
     * 状态
     */
    private Integer triggerStatus = -1;
}
