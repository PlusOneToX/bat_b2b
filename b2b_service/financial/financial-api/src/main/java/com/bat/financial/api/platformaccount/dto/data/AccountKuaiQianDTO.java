package com.bat.financial.api.platformaccount.dto.data;

import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 19:15
 */
@Data
public class AccountKuaiQianDTO {
    private Integer id;

    private Integer organizationId;

    private Integer organizationName;

    private String accountName;

    private String erpAccountNo;

    private String bankNo;

    private String merchanAcctId;

    private String signFileUrl;

    private String signPwd;

    private String signPrivateKey;

    private String checkSignFileUrl;

    private Short backType;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}
