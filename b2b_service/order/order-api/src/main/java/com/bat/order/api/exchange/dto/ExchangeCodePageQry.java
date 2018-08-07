package com.bat.order.api.exchange.dto;

import com.bat.order.api.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ExchangeCodePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "下单开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "下单结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "订单状态 1.待确认2.已确认 3.已拒绝 4.已取消 5.已完成 ")
    private Short orderStatus;

    @ApiModelProperty(value = "搜索 分销商/订单号/收货人")
    private String content;
}
