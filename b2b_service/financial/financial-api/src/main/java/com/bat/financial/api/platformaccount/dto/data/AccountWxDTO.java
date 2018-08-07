package com.bat.financial.api.platformaccount.dto.data;

import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:35
 */
@Data
public class AccountWxDTO {
    private Integer id;

    private Integer organizationId;

    private String organizationName;

    private String accountName;

    private String erpAccountNo;

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

    private String version;

    private Date createTime;

    private Date updateTime;

}
