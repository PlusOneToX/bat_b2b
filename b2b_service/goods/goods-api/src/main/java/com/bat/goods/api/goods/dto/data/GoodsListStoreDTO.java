package com.bat.goods.api.goods.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商店配置商品信息")
public class GoodsListStoreDTO {

    @ApiModelProperty(value = "商品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "eng")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", example = "G175F8DCBF82")
    private String goodsNo;
    @ApiModelProperty(value = "上架状态，1未上架，2审批中，3已上架", example = "3")
    private String saleStatus;
    @ApiModelProperty(value = "冻结状态，1未冻结，2冻结", example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "排序序号", example = "1")
    private Integer sort;

}
