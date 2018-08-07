package com.bat.order.api.order.dto.customer;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "兑换订单商品明细信息")
public class OrderGoodsExchangeCmd extends OrderGoodsCustomerCmd {

    @NotNull(message = "P_GOODS_CODE_NULL")
    @ApiModelProperty(value = "兑换码列表(兑换卡暗码或者是第三方兑换码)", required = true)
    private List<String> codes;
    @ApiModelProperty(value = "快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）", required = false, example = "1")
    private Short mailSetting;

}