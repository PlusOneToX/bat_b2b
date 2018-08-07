package com.bat.system.api.logistics.dto.data;

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/13 12:05
 */
@Data
@ApiModel(value = "LogisticsQryDTO")
public class LogisticsQryDTO implements Cloneable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "物流名称")
    private String name;
    @ApiModelProperty(value = "物流描述")
    private String description;
    @ApiModelProperty(value = "费用")
    private BigDecimal cost;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogisticsQryDTO dto = (LogisticsQryDTO)o;
        return Objects.equals(id, dto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @SneakyThrows
    @Override
    public LogisticsQryDTO clone() {
        return (LogisticsQryDTO)super.clone();
    }
}
