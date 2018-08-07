package com.bat.goods.api.goods.dto.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "货品(SKU)信息封装")
public class GoodsItemDTO {

    @ApiModelProperty(value = "货品ID", required = true, example = "1345")
    private Integer id;
    @ApiModelProperty(value = "货品(SKU)名称", required = true, example = "bat")
    private String itemName;
    @ApiModelProperty(value = "货品英文名称", required = false, example = "bat")
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
    @ApiModelProperty(value = "规格名称", example = "1223343")
    private String specsName;
    @ApiModelProperty(value = "颜色名称", example = "1223343")
    private String colorName;
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
    @ApiModelProperty(value = "是否支持预售：0-否 1-是", required = false, example = "0")
    private Short advanceSaleFlag;
    @ApiModelProperty(value = "商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止", required = false, example = "1")
    private Short lifeCycle;
    @ApiModelProperty(value = "ERP促销状态 清仓 5caebbb86c7863 ; BCD  5e9fec57c26d75", required = false,
        example = "5caebbb86c7863")
    private String promotionStatus;
    @ApiModelProperty(value = "直发客户是否支持在途：0-否 1-是", required = false, example = "0")
    private Short onwaySaleFlag;
    @ApiModelProperty(value = "货品上架状态，1未上架，2审批中，3已上架", required = false, example = "3")
    private Short saleStatus;
    @ApiModelProperty(value = "上架时间", required = false, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleTime;
    @ApiModelProperty(value = "创建时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间", required = true, example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("货品等级价格")
    private List<GoodsItemScalePriceDTO> scalePrices;
    @ApiModelProperty("货品装箱信息")
    private List<GoodsItemBoxDTO> boxs;

}
