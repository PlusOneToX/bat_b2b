package com.bat.goods.api.user.dto.data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "前台货品(SKU)信息封装")
public class UserGoodsItemDTO {

    @ApiModelProperty(value = "货品ID", example = "1345")
    private Integer id;
    @ApiModelProperty(value = "货品(SKU)名称", example = "bat")
    private String itemName;
    @ApiModelProperty(value = "货品英文名称", example = "bat")
    private String itemNameEn;
    @ApiModelProperty(value = "ERP物料编码", example = "810607000249")
    private String itemCode;
    @ApiModelProperty(value = "条形码", example = "6941402742584")
    private String barCode;
    @ApiModelProperty(value = "规格名称", example = "1223343")
    private String specsName;
    @ApiModelProperty(value = "颜色名称", example = "1223343")
    private String colorName;
    @ApiModelProperty(value = "规格英文名称", example = "1223343")
    private String specsNameEn;
    @ApiModelProperty(value = "颜色英文名称", example = "1223343")
    private String colorNameEn;
    @ApiModelProperty(value = "重量g", example = "10.23")
    private BigDecimal weight;
    @ApiModelProperty(value = "长度", example = "10.23")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", example = "10.23")
    private BigDecimal width;
    @ApiModelProperty(value = "高", example = "10.23")
    private BigDecimal height;
    @ApiModelProperty(value = "单位", example = "mm")
    private String unit;
    @ApiModelProperty(value = "货品(SKU)图片", example = "http://bat")
    private String itemImg;
    @ApiModelProperty(value = "预售最少购买数量", example = "32435")
    private String moq;
    @ApiModelProperty(value = "是否支持预售：0-否 1-是", example = "0")
    private Short advanceSaleFlag;
    @ApiModelProperty(value = "直发客户是否支持在途：0-否 1-是", example = "0")
    private Short onwaySaleFlag;
    @ApiModelProperty("货品装箱信息")
    private List<UserGoodsItemBoxDTO> boxs;

}
