package com.bat.promotion.dao.promotion.dataobject;

import lombok.Data;

@Data
public class PromotionDepartmentRelevanceNoDO {
    private Integer id;

    private Integer promotionId;

    private Integer departmentId;
}