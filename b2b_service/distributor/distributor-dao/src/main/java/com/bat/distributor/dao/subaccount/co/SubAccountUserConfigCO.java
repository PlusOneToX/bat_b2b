package com.bat.distributor.dao.subaccount.co;

import lombok.Data;

import java.util.List;

@Data
public class SubAccountUserConfigCO {

    private Integer id;

    private String distributorName;

    private String name;

    private Short amountType;

    private List<SubAccountLevelRatioCO> levelRatioList;

}
