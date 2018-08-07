package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DistributorCustomPriceDO implements Serializable {
    private static final long serialVersionUID = 2921875747593031791L;
    private Integer id;

    private Integer itemId;

    private Integer distributorId;


    private BigDecimal price;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Short delFlag;


}