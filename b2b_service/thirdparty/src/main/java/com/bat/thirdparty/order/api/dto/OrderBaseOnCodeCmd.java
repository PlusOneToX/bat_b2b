package com.bat.thirdparty.order.api.dto;

import com.bat.thirdparty.order.api.dto.common.OrderCommonCmd;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderBaseOnCodeCmd extends OrderCommonCmd implements Serializable {
    private static final long serialVersionUID = 998112386649250830L;

    /**
     * 基于编码的详情
     */
    @ApiModelProperty(value = "基于编码的详情")
    private List<OrderDetailBaseOnCodeQry> orderDetails=new ArrayList<>();
}
