package com.bat.goods.api.goods.dto;

import com.bat.goods.api.base.PageQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DiyGoodsPageQry extends PageQry {

    @ApiModelProperty(value = "商品类型 1-普通 2-定制")
    private Short goodsType=1;

    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制")
    private Short diyType;

    @ApiModelProperty(value = "搜索关键词、商品名称、货品名称、80码")
    private String content;
}
