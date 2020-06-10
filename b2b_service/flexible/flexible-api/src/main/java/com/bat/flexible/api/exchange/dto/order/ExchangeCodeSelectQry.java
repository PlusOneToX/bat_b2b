package com.bat.flexible.api.exchange.dto.order;

import lombok.Data;

@Data
public class ExchangeCodeSelectQry {

    private Integer materialId;

    private Integer modelId;

    private Integer exchangeCodeId;

    private String secretCode;


}
