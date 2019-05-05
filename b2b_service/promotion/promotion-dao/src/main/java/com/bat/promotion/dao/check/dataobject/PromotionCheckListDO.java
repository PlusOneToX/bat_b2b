package com.bat.promotion.dao.check.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PromotionCheckListDO {
    private Integer id;
    private Integer promotionId;
    private Short promotionType;
    private Short groupSeckillType;
    private String name;
    private Short checkStatus;
    private Short checkType;
    private Integer userId;
    private String userName;
    private Date createTime;
}