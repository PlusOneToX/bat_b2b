package com.bat.system.dao.storesetting.dataobject;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class MobileItemDO {
    private Integer id;

    private Integer mobileId;

    private String imageUrl;

    private BigDecimal widthPercentage;

    private Short jumpType;

    private String jumpParams;

    private Integer subSort;

    private Short styleType;
}