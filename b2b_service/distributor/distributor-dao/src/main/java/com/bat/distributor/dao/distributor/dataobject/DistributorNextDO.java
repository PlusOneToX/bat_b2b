package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorNextDO {

    private Integer id;
    private String name;
    private String companyName;
    private Short applyStatus;
    private Date applyTime;
    private Date checkTime;
    private Short freezeStatus;
    private Date freezeTime;
    private String userName;
    private String phone;
    private String email;
    private Integer scalePriceId;
    private String scalePriceName;

}
