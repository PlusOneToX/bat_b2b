package com.bat.distributor.dao.subaccount.co;

import lombok.Data;

import java.util.Date;

@Data
public class SubAccountSalemanPageCO {

    private String distributorName;

    private Integer id;

    private String salemanName;

    private Integer levelId;

    private Short type;

    private String mobile;

    private String levelName;

    private Integer parentId;

    private String parentSalemanName;

    private String merchantNumber;

    private String openId;

    private Short openFlag;

    private Date createTime;

}
