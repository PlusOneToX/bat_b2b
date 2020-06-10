package com.bat.flexible.api.exchange.dto;


import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeCodeOrderQry extends BasePageParamQry {
    
    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 兑换码状态
     */
    private Short status=2;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 4.已取消 5.已完成")
    private Short orderStatus;

    /**
     * 搜索关键词
     */
    private String search;






    
}