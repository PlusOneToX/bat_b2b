package com.bat.distributor.dao.nextscaleprice.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class NextScalePriceDO {
    private Integer id;

    private Integer distributorId;

    private String name;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;

    private Short specialFlag;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;

}