package com.bat.promotion.dao.promotion.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PromotionStatusDO {
    private Integer id;
    private Short promoStatus;
    private Date updateTime;
}