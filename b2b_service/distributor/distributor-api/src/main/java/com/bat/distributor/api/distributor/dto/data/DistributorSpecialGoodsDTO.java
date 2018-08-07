package com.bat.distributor.api.distributor.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商特价商品信息")
public class DistributorSpecialGoodsDTO {

    private Integer id;
    @ApiModelProperty(value = "商品id", example = "1234")
    private Integer goodsId;
    @ApiModelProperty(value = "货品id", example = "1234")
    private Integer goodsItemId;
    @ApiModelProperty(value = "货品名称", example = "1234")
    private String itemName;
    @ApiModelProperty(value = "货品编码", example = "1234")
    private String itemCode;
    @ApiModelProperty(value = "商品名称", example = "1234")
    private String goodsName;
    @ApiModelProperty(value = "商品编码", example = "1234")
    private String goodsNo;
    @ApiModelProperty(value = "商品特价", example = "12.987")
    private BigDecimal distributorPrice;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
