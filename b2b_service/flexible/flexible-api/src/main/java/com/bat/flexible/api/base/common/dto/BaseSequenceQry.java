package com.bat.flexible.api.base.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseSequenceQry {

    /**
     * 开始序号
     */
    private BigDecimal sequenceStart=BigDecimal.ONE;


    /**
     * 递增序号
     */
    private BigDecimal sequenceAdd=BigDecimal.ONE;

    /**
     * 原来序号
     */
    private BigDecimal parentSequence;
}
