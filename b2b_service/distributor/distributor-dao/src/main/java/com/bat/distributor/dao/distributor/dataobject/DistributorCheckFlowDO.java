package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorCheckFlowDO {
    private Integer id;

    private Integer distributorCheckId;

    private Integer userId;

    private String userName;

    private Short checkStatus;

    private Date checkTime;

    private String remark;

    private Integer checkSort;

    private Date createTime;

    private Date updateTime;
}