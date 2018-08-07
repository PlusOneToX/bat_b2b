package com.bat.goods.api.goods.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsItemCmd", description = "货品信息")
public class GoodsItemCmd {

    private Integer id;
    @NotBlank(message = "P_GOODS_SKU_NAME_NULL")
    @ApiModelProperty(value = "货品(SKU)名称", required = true, example = "bat")
    private String itemName;
    @ApiModelProperty(value = "英文名称", required = false, example = "bat")
    private String itemNameEn;
    @ApiModelProperty(value = "ERP物料编码", required = false, example = "810607000249")
    private String itemCode;
    @ApiModelProperty(value = "ERP物料id", required = false, example = "121323")
    private Integer itemErpId;
    @ApiModelProperty(value = "条形码", required = false, example = "6941402742584")
    private String barCode;
    @ApiModelProperty(value = "规格id", required = false, example = "1223343")
    private Integer specsId;
    @ApiModelProperty(value = "颜色id", required = false, example = "1223343")
    private Integer colorId;
    @ApiModelProperty(value = "默认销售价", required = false, example = "12.43")
    private BigDecimal salePrice;
    @ApiModelProperty(value = "成本价", required = false, example = "10.23")
    private BigDecimal costPrice;
    @ApiModelProperty(value = "重量g", required = false, example = "10.23")
    private BigDecimal weight;
    @ApiModelProperty(value = "长度", required = false, example = "10.23")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", required = false, example = "10.23")
    private BigDecimal width;
    @ApiModelProperty(value = "高", required = false, example = "10.23")
    private BigDecimal height;
    @ApiModelProperty(value = "单位", required = false, example = "mm")
    private String unit;
    @ApiModelProperty(value = "货品(SKU)图片", required = false, example = "http://bat")
    private String itemImg;
    @ApiModelProperty(value = "预售最少购买数量", required = false, example = "32435")
    private String moq;
    @NotNull(message = "P_GOODS_ADVANCE_SALE_FLAG_NULL")
    @ApiModelProperty(value = "是否支持预售：0-否 1-是", required = true, example = "0")
    private Short advanceSaleFlag;
    @ApiModelProperty(value = "商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止", required = false, example = "1")
    private Short lifeCycle;
    @ApiModelProperty(value = "ERP促销状态 清仓 5caebbb86c7863 ; BCD  5e9fec57c26d75", required = false, example = "1")
    private String promotionStatus;
    @ApiModelProperty(value = "直发客户是否支持在途：0-否 1-是", required = false, example = "0")
    private Short onwaySaleFlag;
    @NotNull(message = "P_GOODS_SALE_STATUS")
    @ApiModelProperty(value = "货品上架状态，1未上架，2审批中，3已上架", required = true, example = "3")
    private Short saleStatus;

    @Valid
    @NotNull(message = "P_GOODS_SCALE_PRICES")
    @ApiModelProperty(value = "货品(SKU)等级价格", required = true)
    private List<GoodsItemScalePriceCmd> scalePrices;
    @Valid
    @ApiModelProperty("货品(SKU)装箱信息")
    private List<GoodsItemBoxCmd> boxs;
    @Valid
    @ApiModelProperty("货品(SKU)扩展信息")
    private GoodsItemDataCmd data;

    @NotNull(message = "P_GOODS_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型,1.新增 2.修改 3.删除", required = true, example = "1")
    private Short operationType;

}
