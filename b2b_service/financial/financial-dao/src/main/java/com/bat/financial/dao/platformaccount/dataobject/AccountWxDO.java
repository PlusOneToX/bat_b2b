package com.bat.financial.dao.platformaccount.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class AccountWxDO {

    public static final int BACK_TYPE_YES = 1;
    public static final int BACK_TYPE_NO = 0;

    private Integer id;

    private Integer organizationId;

    private String accountName;

    private String bankNo;

    private String appId;

    private String apiclientKey;

    private String serialNumber;

    private Date certificateInvalidTime;

    private String accountNo;

    private String appKey;

    private Short appType;

    private Short backType;

    private Short openFlag;

    private String erpAccountNo;

    private String version;

    private Date createTime;

    private Date updateTime;


}