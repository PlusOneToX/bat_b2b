package com.bat.thirdparty.order.dao.dataobject.log;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class OrderBusinessLogDO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "日志类型 1、推送定制信息给第三方 2、接收第三方订单（基于ID）3、接收第三方订单（基于编码） 4、推送销售单给ERP 5、B2B推单给工厂 6、工厂发货 7、推送物流信息给第三方 8、第三方取消订单 9、工厂取消订单 10、推送核销信息给第三方 11.同步erp上的物流单号")
    private Short logType;

    @ApiModelProperty(value = "请求朝向 1、B2B->第三方 2、第三方->B2B 3、B2B->ERP 4、ERP->B2B 5、工厂-->B2B 6、B2B->>工厂")
    private Short towardType;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "外部单号")
    private String otherOrderNo;

    @ApiModelProperty(value = "业务数据")
    private String businessData;

    @ApiModelProperty(value = "请求头")
    private String headerParamJson;

    @ApiModelProperty(value = "请求参数")
    private String requestParamJson;

    @ApiModelProperty(value = "状态 1、成功 0、失败")
    private Short status;

    @ApiModelProperty(value = "调用时间")
    private Date createTime;

    @ApiModelProperty(value = "响应")
    private String responseMsg;

    @ApiModelProperty(value = "B2B单号")
    private String orderNo;

    @ApiModelProperty(value = "错误提示")
    private String errorMsg;

    private Date updateTime;


    private Short deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getLogType() {
        return logType;
    }

    public void setLogType(Short logType) {
        this.logType = logType;
    }

    public Short getTowardType() {
        return towardType;
    }

    public void setTowardType(Short towardType) {
        this.towardType = towardType;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getOtherOrderNo() {
        return otherOrderNo;
    }

    public void setOtherOrderNo(String otherOrderNo) {
        this.otherOrderNo = otherOrderNo == null ? null : otherOrderNo.trim();
    }

    public String getBusinessData() {
        return businessData;
    }

    public void setBusinessData(String businessData) {
        this.businessData = businessData == null ? null : businessData.trim();
    }

    public String getHeaderParamJson() {
        return headerParamJson;
    }

    public void setHeaderParamJson(String headerParamJson) {
        this.headerParamJson = headerParamJson == null ? null : headerParamJson.trim();
    }

    public String getRequestParamJson() {
        return requestParamJson;
    }

    public void setRequestParamJson(String requestParamJson) {
        this.requestParamJson = requestParamJson == null ? null : requestParamJson.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg == null ? null : responseMsg.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}