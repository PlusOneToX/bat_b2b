package com.bat.distributor.api.subaccount.dto;

import com.bat.distributor.api.base.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubAccountSalemanPageQry extends BasePage {

    @ApiModelProperty(value = "搜索类型 1、分销商用户名 2、业务员姓名 3、业务员手机号")
    private Short searchType;

    @ApiModelProperty(value = "搜索关键词")
    private String content;

    @ApiModelProperty(value = "分账等级Id")
    private Integer levelId;

    private Integer distributorId;
}
