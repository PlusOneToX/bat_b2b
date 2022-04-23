package com.platform.modules.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserIntegralOrderVo {
    private String nikeName;
    private BigDecimal integral;
    private BigDecimal award;
    private String remark;
    private String userId;
    private Integer bonusPoolId;
    private Integer mallBonusPoolDetailId;

}
