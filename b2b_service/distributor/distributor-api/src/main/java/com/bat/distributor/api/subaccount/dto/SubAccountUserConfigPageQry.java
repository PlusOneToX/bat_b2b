package com.bat.distributor.api.subaccount.dto;

import com.bat.distributor.api.base.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SubAccountUserConfigPageQry extends BasePage {

    @ApiModelProperty(value = "搜索类型、1、分销商用户名 2、配置名称 3、店铺名称")
    private Short searchType;

    @ApiModelProperty(value = "搜索关键词")
    private String content;
}
