package com.bat.distributor.dao.subaccount.co;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubAccountLevelRatioCO {


    private Integer levelId;


    private BigDecimal ratio;

    private String levelName;



}
