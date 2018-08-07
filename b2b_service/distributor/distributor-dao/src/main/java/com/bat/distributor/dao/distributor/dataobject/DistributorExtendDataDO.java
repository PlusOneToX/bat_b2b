package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class DistributorExtendDataDO {
    private Integer id;

    private Integer distributorId;

    private String certNo;

    private String comment;

    private String language;

    private String currencyType;

    private Short distributionFlag;

    private Short distributionMode;

    private Short distributionPayWay;

    private Short distributionAutoFlag;

    private Short customerFlag;

    private Short customerMode;

    private Short distributionPromotionFlag;

    private String distributionQrUrl;

    private Short erpFlag;

    private Short erpBalanceFlag;

    private Integer erpId;

    private String erpNo;

    private Date createTime;

    private Date updateTime;

    private Short subAccountFlag;

    private String openId;
}