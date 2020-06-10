package com.bat.flexible.api.exchange.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExchangeCodeStatusRequest {

    @NotNull(message = "id不能位空")
    private Integer id;

    @NotBlank(message = "作废原因不能为空")
    private String reason;


}
