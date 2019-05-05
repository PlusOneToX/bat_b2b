package com.bat.promotion.dao.promotion.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class UserPromotionDO {
    private Integer id;

    private String name;

    private String nameEn;

    private String promoDesc;

    private Date startTime;

    private Date endTime;

    private Short advanceFlag;

    private Short applyStatus;

    private Short promoStatus;

    private Short promoSource;

    private Short promoType;

    private Short onWayFlag;

}