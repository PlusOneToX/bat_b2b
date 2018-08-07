package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorCheckListDO {
    private Integer id;
    private Integer distributorId;
    private String erpNo;
    private Integer treeNode;
    private String name;
    private String companyName;
    private Short checkStatus;
    private Short checkType;
    private Integer userId;
    private String userName;
    private Date createTime;
}