package com.bat.promotion.dao.check.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class PromotionCheckFlowDO {
    private Integer id;

    private Integer promotionCheckId;

    private Integer userId;

    private String userName;

    private Short checkStatus;

    private Date checkTime;

    private String remark;

    private Integer checkSort;

    private Date createTime;

    private Date updateTime;
}