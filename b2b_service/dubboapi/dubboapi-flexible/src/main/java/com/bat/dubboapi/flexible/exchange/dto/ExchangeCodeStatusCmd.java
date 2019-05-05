package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ExchangeCodeStatusCmd implements Serializable {

    private Integer id;

    private String reason;

    private Integer distributorId;

    private String distributorName;


}
