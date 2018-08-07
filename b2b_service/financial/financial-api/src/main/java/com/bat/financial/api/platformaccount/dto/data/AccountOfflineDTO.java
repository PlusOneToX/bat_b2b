package com.bat.financial.api.platformaccount.dto.data;

import java.util.Date;

import lombok.Data;

@Data
public class AccountOfflineDTO {
    private Integer id;

    private Integer organizationId;

    private Integer organizationName;

    private String currencyCode;

    private String accountName;

    private String erpAccountNo;

    private Integer bankNo;

    private String cardNo;

    private String bankName;

    private String bankAddr;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}