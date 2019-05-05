package com.bat.promotion.dao.promotion.dataobject;

import java.io.Serializable;

import lombok.Data;

@Data
public class PromotionRuleGoodsDO implements Serializable {
    private Integer id;

    private Integer promotionId;

    private Integer promotionRuleId;

    private Integer goodsId;

    private Integer itemId;

    private String goodsNo;

    private String itemCode;
}