package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class UerNextListDO {
    private Integer id;
    private String name;
    private String companyName;
    private String userName;
    private String phone;
    private String scalePriceName;
    private Short applyStatus;
    private Date applyTime;

}
