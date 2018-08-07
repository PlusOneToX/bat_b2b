package com.bat.financial.api.platformaccount.dto.data;

import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:35
 */
@Data
public class AccountAlipayDTO {
    private Integer id;

    private Integer organizationId;

    private String organizationName;

    private String accountName;

    private String erpAccountNo;

    private String appId;

    private String bankNo;

    private Short backType;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

    private String appPublicSecret;

    private String appPrivateSecret;
}
