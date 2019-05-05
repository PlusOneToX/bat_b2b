package com.bat.thirdparty.order.api.dto.moleji;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 摩乐吉订单
 */
public class MolejiOrderCreateCmd {

    /**
     * 时间戳
     */
    @NotNull(message = "时间戳不能为空")
    private Long timestamp;

    /**
     * 签名
     */
    @NotBlank(message = "签名不能为空")
    private String signature;

    /**
     * 订单编号
     */
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    /**
     * 买家姓名
     */
    @NotBlank(message = "买家姓名不能为空")
    private String userName;

    /**
     * 卖家手机号
     */
    @NotBlank(message = "买家手机号不能为空")
    private String mobile;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空")
    private String province;

    /**
     * 市
     */
    @NotBlank(message = "市不能为空")
    private String city;

    /**
     * 区
      */
    @NotBlank(message = "区不能为空")
    private String area;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 地址编码
     */
    private String nationalCode;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 壳列表
     */
    @JsonProperty("case")
    @NotEmpty(message = "壳信息不能为空")
    private JSONArray caseArray;

    /**
     * 膜列表
     */
    private JSONArray protector;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public JSONArray getCaseArray() {
        return caseArray;
    }

    public void setCaseArray(JSONArray caseArray) {
        this.caseArray = caseArray;
    }

    public JSONArray getProtector() {
        return protector;
    }

    public void setProtector(JSONArray protector) {
        this.protector = protector;
    }
}
