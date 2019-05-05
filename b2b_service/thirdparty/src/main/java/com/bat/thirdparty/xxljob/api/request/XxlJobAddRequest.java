package com.bat.thirdparty.xxljob.api.request;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/2 11:54
 */
@Data
public class XxlJobAddRequest {
    /**
     * 执行器id
     */
    private Integer jobGroup;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 负责人
     */
    private String author;
    /**
     * 警告通知邮件地址
     */
    private String alarmEmail;
    /**
     * 调度类型 CRON FIX_RATE
     */
    private String scheduleType;
    /**
     * 调度类型时间配置cron
     */
    private String scheduleConf;
    /**
     * 调度类型时间配置cron
     */
    private String cronGen_display;
    /**
     * 调度类型时间配置cron
     */
    private String schedule_conf_CRON;
    /**
     * 运行模式
     */
    private String glueType;
    /**
     * JobHandler,任务执行器
     */
    private String executorHandler;
    /**
     * 任务参数
     */
    private String executorParam;
    /**
     * 路由策略
     */
    private String executorRouteStrategy;
    /**
     * 子任务id
     */
    private Integer childJobId;
    /**
     * 调度过期策略
     */
    private String misfireStrategy;
    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;
    /**
     * 任务超时时间
     */
    private Integer executorTimeout;
    /**
     * 失败重试次数
     */
    private Integer executorFailRetryCount;
}
