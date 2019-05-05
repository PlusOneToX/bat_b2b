package com.bat.dubboapi.system.logistics.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/6/1 16:37
 */
public class LogisticsCalculationRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal cost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
