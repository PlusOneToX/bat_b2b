package com.bat.thirdparty.order.api.dto.common;

public class UserInfoQry {
    private String userName;
    private String phoneNumber;
    private String userNo;
    /**
     * 海外订单可能存在没有 手机号 有邮箱的情况
     */
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfoQry(String userName, String phoneNumber, String userNo, String email) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userNo = userNo;
        this.email = email;
    }
}
