package com.bat.order.api.order.dto.common;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "再来一单商品信息")
public class OrderOneMoreDTO {

    @ApiModelProperty(value = "商品id", example = "1223324")
    private Integer goodsId;
    @ApiModelProperty(value = "商品编码", example = "G183285435")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "货品id", example = "123456")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码", example = "80462336544")
    private String itemCode;
    @ApiModelProperty(value = "货品条码", example = "80462336544")
    private String barCode;
    @ApiModelProperty(value = "货品名称", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "货品颜色值名称", example = "货品颜色值名称")
    private String colorName;
    @ApiModelProperty(value = "货品规格值名称", example = "货品规格值名称")
    private String specsName;
    @ApiModelProperty(value = "商品或货品图片", example = "80462336544")
    private String imageUrl;
    @ApiModelProperty(value = "重量g", example = "10.23")
    private BigDecimal weight;
    @ApiModelProperty(value = "长度", example = "10.23")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", example = "10.23")
    private BigDecimal width;
    @ApiModelProperty(value = "高", example = "10.23")
    private BigDecimal height;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "商品是否有效 1、有效 0、无效", example = "1")
    private Short openFlag;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "是否赠品 1 非赠品 2 赠品", example = "1")
    private Short itemType;
    @ApiModelProperty(value = "商品促销活动Id(活动规则id)", example = "11252")
    private Integer goodsPromotionId;
    @ApiModelProperty(value = "订单促销活动Id(活动规则id)", example = "11252")
    private Integer orderPromotionId;
    @ApiModelProperty(value = "拼团秒杀活动id", example = "11252")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "数量", example = "123")
    private Integer itemCount;
    @ApiModelProperty(value = "货品在库数量", example = "10")
    private Integer itemInCount;
    @ApiModelProperty(value = "货品在途数量", example = "10")
    private Integer itemOnWayCount;
    @ApiModelProperty(value = "建议零售价", example = "12.34")
    private BigDecimal retailPrice;
    @ApiModelProperty(value = "价格", example = "12.34")
    private BigDecimal salePrice;
    @ApiModelProperty(value = "是否支持预售：0-否 1-是", example = "0")
    private Short advanceSaleFlag;
    @ApiModelProperty(value = "直发客户是否支持在途：0-否 1-是", example = "0")
    private Short onwaySaleFlag;

    @ApiModelProperty(value = "定制信息(商品类型为2时有值)")
    private OrderGoodsDiyDTO diy;
    @ApiModelProperty(value = "商品促销活动列表(包含整单活动)")
    private List<PromotionDTO> promotions;
    @ApiModelProperty(value = "商品拼团秒杀活动列表")
    private List<GroupSeckillDTO> groupSeckills;
    @ApiModelProperty(value = "货品装箱列表")
    private List<GoodsItemBoxDTO> boxs;

    @ApiModelProperty(value = "用户商品是否可视(0,不可视,1,可视)")
    private Integer visible = 1;
}
