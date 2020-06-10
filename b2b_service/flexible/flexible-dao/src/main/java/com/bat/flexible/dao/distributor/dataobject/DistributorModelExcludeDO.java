package com.bat.flexible.dao.distributor.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class DistributorModelExcludeDO implements Serializable {
    private static final long serialVersionUID = 4313012896955831603L;
    private Integer id;

    private Integer modelId;

    private Integer distributorId;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

   
}