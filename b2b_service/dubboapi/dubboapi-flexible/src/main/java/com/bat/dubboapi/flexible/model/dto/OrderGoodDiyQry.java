package com.bat.dubboapi.flexible.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/10/20 11:39
 */
@Data
public class OrderGoodDiyQry implements Serializable {
    /**
     * index
     */
    private Integer index;
    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 材质id
     */
    private Integer materialId;

    /**
     * 重量
     */
    private BigDecimal weight;
}
