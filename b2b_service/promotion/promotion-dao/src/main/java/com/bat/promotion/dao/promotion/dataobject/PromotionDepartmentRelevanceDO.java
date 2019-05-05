package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionDepartmentRelevanceDO {
    private Integer id;

    private Integer promotionId;

    private Integer departmentId;
}