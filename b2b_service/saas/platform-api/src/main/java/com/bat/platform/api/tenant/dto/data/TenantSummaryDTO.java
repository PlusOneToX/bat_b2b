package com.bat.platform.api.tenant.dto.data;

import io.swagger.annotations.ApiModelProperty;

public class TenantSummaryDTO {

    @ApiModelProperty(value = "基础信息配置是否配置")
    private boolean commonFlag;

    @ApiModelProperty(value = "数据库配置是否配置")
    private boolean dbFlag;

    @ApiModelProperty(value = "定制信息配置是否配置")
    private boolean diyFlag;

    @ApiModelProperty(value = "ERP信息配置是否配置")
    private boolean erpFlag;

    @ApiModelProperty(value = "文件存储配置信息是否配置")
    private boolean ossFlag;

    @ApiModelProperty(value = "短信配置信息是否配置")
    private boolean smsFlag;

    public boolean isCommonFlag() {
        return commonFlag;
    }

    public void setCommonFlag(boolean commonFlag) {
        this.commonFlag = commonFlag;
    }

    public boolean isDbFlag() {
        return dbFlag;
    }

    public void setDbFlag(boolean dbFlag) {
        this.dbFlag = dbFlag;
    }

    public boolean isDiyFlag() {
        return diyFlag;
    }

    public void setDiyFlag(boolean diyFlag) {
        this.diyFlag = diyFlag;
    }

    public boolean isErpFlag() {
        return erpFlag;
    }

    public void setErpFlag(boolean erpFlag) {
        this.erpFlag = erpFlag;
    }

    public boolean isOssFlag() {
        return ossFlag;
    }

    public void setOssFlag(boolean ossFlag) {
        this.ossFlag = ossFlag;
    }

    public boolean isSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(boolean smsFlag) {
        this.smsFlag = smsFlag;
    }
}
