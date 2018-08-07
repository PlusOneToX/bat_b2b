package com.bat.financial.dao.platformaccount.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class AccountKuaiQianDO {
    private Integer id;

    private Integer organizationId;

    private String accountName;

    private String bankNo;

    private String merchanAcctId;

    private String signFileUrl;

    private String signPwd;

    private String signPrivateKey;

    private Short backType;

    private Short openFlag;

    private String erpAccountNo;

    private Date createTime;

    private Date updateTime;

    private String checkSignFileUrl;

}