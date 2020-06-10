package com.bat.flexible.api.exchange.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ExchangeFactoryRequest implements Serializable {
    private static final long serialVersionUID = 3357557426508115500L;

    //兑换卡活动id
    @NotNull(message = "兑换id不能为空")
    private Integer exchangeId;

    //数量
    @NotNull(message = "生成数量不能为空")
    @Min(value = 10L,message = "生成数量必须要大于等于10")
    private Integer count;


}
