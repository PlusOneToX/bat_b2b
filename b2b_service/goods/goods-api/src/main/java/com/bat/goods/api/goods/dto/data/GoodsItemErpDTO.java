package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品erp信息")
public class GoodsItemErpDTO {
    @ApiModelProperty(value = "货品条形码", example = "609876567434")
    private String barCode;
    @ApiModelProperty(value = "货品重量（g）", example = "3.23")
    private BigDecimal weight;
    @ApiModelProperty(value = "货品名称", example = "bat")
    private String itemName;
    @ApiModelProperty(value = "货品名称（英文）", example = "bat")
    private String itemNameEn;
    @ApiModelProperty(value = "货品编码", example = "80562453255")
    private String itemCode;
    @ApiModelProperty(value = "货品erp内码", example = "122432")
    private Integer itemErpId;
    @ApiModelProperty(value = "被分配的商品名称", example = "bat")
    private String goodsName;
    @ApiModelProperty(value = "被分配的商品id", example = "122432")
    private Integer goodsId;
    @ApiModelProperty(value = "货品品类erp编码", example = "354512")
    private String categoryErpNo;
    @ApiModelProperty(value = "长", example = "3.235")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", example = "3.235")
    private BigDecimal width;
    @ApiModelProperty(value = "高", example = "3.235")
    private BigDecimal height;
    @ApiModelProperty(value = "尺寸单位", example = "mm")
    private String unit;
    @ApiModelProperty(value = "预售最少购买数量", example = "34")
    private String moq;
    @ApiModelProperty(value = "默认销售价", example = "3.78")
    private BigDecimal salePrice = new BigDecimal(0);
    @ApiModelProperty(value = "成本价", example = "3.78")
    private BigDecimal costPrice = new BigDecimal(0);
    @ApiModelProperty(value = "商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止", example = "1")
    private Short lifeCycle = 1;
    @ApiModelProperty(value = "ERP促销状态 清仓 5caebbb86c7863 ; BCD 5e9fec57c26d75", example = "5caebbb86c7863")
    private String promotionStatus;
    @ApiModelProperty(value = "货品等级价格列表（按顺序）")
    private List<GoodsItemErpPriceDTO> scalePrices;
    @ApiModelProperty(value = "货品装箱规格")
    private List<GoodsItemBoxErpDTO> boxInfos;
}
