package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorFinancialDO {
    private Integer id;

    private Integer distributorId;

    private Integer tradeId;

    private Short coinType;

    private String bankAccountName;

    private String bankDepositName;

    private String bankAccount;

    private Short taxType;

    private String invoiceTitleName;

    private String taxpayerNumber;

    private String registeredAddress;

    private String registeredPhone;

    private String registeredBankDepositName;

    private String registeredBankAccount;

    private Date createTime;

    private Date updateTime;
}