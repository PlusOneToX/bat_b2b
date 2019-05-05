package com.bat.dubboapi.system.exportdownload.dto;

import java.io.Serializable;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/19 22:27
 */
public class ExcelExportInfoRpcDTO implements Serializable {
    /**
     * 平台来源 web admin,前台管理后台
     */
    private short platform;
    /**
     * 分销商id 业务员id
     */
    private Integer operatorId;
    /**
     * 业务名称 什么导出
     */
    private String businessName;

    private String downloadUrl;

    public short getPlatform() {
        return platform;
    }

    public void setPlatform(short platform) {
        this.platform = platform;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "ExcelExportInfoRpcDTO{" + "platform=" + platform + ", operatorId='" + operatorId + '\''
            + ", businessName='" + businessName + '\'' + ", downloadUrl='" + downloadUrl + '\'' + '}';
    }
}
