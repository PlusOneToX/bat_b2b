package com.bat.dubboapi.thirdparty.xxljob.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/2 10:55
 */
public class XxlJobRpcCmd implements Serializable {

    public static final String SCHEDULE_TYPE_CRON = "CRON";

    public static final String SCHEDULE_TYPE_FIX_RATE = "FIX_RATE";

    /**
     * 执行器
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
     * 警告报告邮件地址
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
     * JobHandler,任务执行器
     */
    private String executorHandler;
    /**
     * 任务参数
     */
    private String executorParam;
    /**
     * 子任务ID
     */
    private Integer childJobId;
    /**
     * 创建任务备注
     */
    private String glueRemark;

    /**
     * 是否覆盖 已经创建过的定时器 （自定义参数）
     */
    private Boolean overwrite = false;

    public Integer getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(Integer childJobId) {
        this.childJobId = childJobId;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleConf() {
        return scheduleConf;
    }

    public void setScheduleConf(String scheduleConf) {
        this.scheduleConf = scheduleConf;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public String getGlueRemark() {
        return glueRemark;
    }

    public void setGlueRemark(String glueRemark) {
        this.glueRemark = glueRemark;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }
}
