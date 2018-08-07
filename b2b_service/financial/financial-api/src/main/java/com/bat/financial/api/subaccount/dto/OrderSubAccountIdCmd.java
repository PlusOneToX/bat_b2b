package com.bat.financial.api.subaccount.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderSubAccountIdCmd {

    /**
     * order_sub_account表主键
     */
    private Integer id;


    private Integer orderId;

    private String appId;

    private List<Integer> billIdList;
}
