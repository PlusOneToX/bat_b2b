package com.bat.promotion.dao.check.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PromotionCheckDO {
    private Integer id;

    private Integer promotionId;

    private Short promotionType;

    private Integer userId;

    private String userName;

    private Integer checkUserId;

    private String checkUserName;

    private Short checkType;

    private Short checkStatus;

    private Date createTime;

    private Date updateTime;

    private String checkContent;
}