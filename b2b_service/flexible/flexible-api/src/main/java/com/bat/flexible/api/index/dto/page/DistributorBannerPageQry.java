package com.bat.flexible.api.index.dto.page;


import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributorBannerPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "运营位置类型 1、首页主banner")
    private Short showLocation;

    @ApiModelProperty(value = "状态 0、未开始、1、展示中 2、已结束")
    private Short status;

    @ApiModelProperty(value = "搜索关键词")
    private String content;


}
