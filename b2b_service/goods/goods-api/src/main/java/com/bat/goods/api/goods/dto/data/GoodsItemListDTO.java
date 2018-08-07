package com.bat.goods.api.goods.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "货品(SKU)(简易)信息")
public class GoodsItemListDTO {

    private Integer id;
    @ApiModelProperty(value = "商品ID", example = "45133")
    private Integer goodsId;
    @ApiModelProperty(value = "商品(SPU)名称", example = "bat")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "bat")
    private String goodsNameEn;
    @ApiModelProperty(value = "商品编码", example = "G245015232")
    private String goodsNo;
    @ApiModelProperty(value = "货品(SKU)名称", example = "bat")
    private String itemName;
    @ApiModelProperty(value = "英文名称", example = "bat")
    private String itemNameEn;
    @ApiModelProperty(value = "ERP物料编码", example = "810607000249")
    private String itemCode;
    @ApiModelProperty(value = "ERP物料60码", example = "121323")
    private String barCode;
    @ApiModelProperty(value = "规格id", example = "1223343")
    private Integer specsId;
    @ApiModelProperty(value = "颜色id", example = "1223343")
    private Integer colorId;
    @ApiModelProperty(value = "规格名称", example = "1223343")
    private String specsName;
    @ApiModelProperty(value = "颜色名称", example = "1223343")
    private String colorName;
    @ApiModelProperty(value = "默认销售价", example = "12.43")
    private BigDecimal salePrice;
    @ApiModelProperty(value = "成本价", example = "10.23")
    private BigDecimal costPrice;
    @ApiModelProperty(value = "货品上架状态，1未上架，2审批中，3已上架", example = "3")
    private Short saleStatus;

    @ApiModelProperty(value = "材质名称")
    private String materialName;
}
