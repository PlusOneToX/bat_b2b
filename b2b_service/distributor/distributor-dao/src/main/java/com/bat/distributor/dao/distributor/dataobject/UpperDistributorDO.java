package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpperDistributorDO {

    private Integer upperDistributorId;

    private String upperDistributorName;

    private String openId;

    private Integer distributorId;

    private String distributorName;

    private Date checkTime;

    private Date applyTime;

}