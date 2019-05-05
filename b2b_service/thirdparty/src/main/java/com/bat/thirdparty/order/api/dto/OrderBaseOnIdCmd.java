package com.bat.thirdparty.order.api.dto;

import com.bat.thirdparty.order.api.dto.common.OrderCommonCmd;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于接收推送订单信息(详情基于id)
 */
@Data
public class OrderBaseOnIdCmd extends OrderCommonCmd implements Serializable {


    private static final long serialVersionUID = -3729888721485266762L;

    @ApiModelProperty(value = "下单明细、id为准")
    private List<OrderDetailBaseOnIdQry> orderDetails = new ArrayList<>();





}
