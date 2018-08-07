package com.bat.financial.dao.platformaccount.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class AccountAlipayDO {
    private Integer id;

    private Integer organizationId;

    private String accountName;

    private String appId;

    private String bankNo;

    private Short backType;

    private Short openFlag;

    private String erpAccountNo;

    private Date createTime;

    private Date updateTime;

    private String appPublicSecret;

    private String appPrivateSecret;
}