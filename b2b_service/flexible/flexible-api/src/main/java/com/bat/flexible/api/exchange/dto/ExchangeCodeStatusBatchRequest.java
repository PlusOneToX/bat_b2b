package com.bat.flexible.api.exchange.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ExchangeCodeStatusBatchRequest {

    @NotEmpty(message = "id列表不能为空")
    private List<Integer> idList;

    @NotBlank(message = "作废原因不能为空")
    private String reason;


}
