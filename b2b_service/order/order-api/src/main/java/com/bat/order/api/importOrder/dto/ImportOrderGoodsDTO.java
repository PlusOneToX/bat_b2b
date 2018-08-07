package com.bat.order.api.importOrder.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@Data
public class ImportOrderGoodsDTO {

    private Integer id;

    private Integer importOrderId;

    @ApiModelProperty("货品id")
    private Integer itemId;

    @ApiModelProperty("货品编号")
    private String itemCode;

    @ApiModelProperty("货品名称")
    private String itemName;

    @ApiModelProperty("商品id")
    private Integer goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品编码")
    private String goodsNo;

    @ApiModelProperty("分销商价格")
    private BigDecimal distributorPrice;

    @ApiModelProperty("价格折扣")
    private BigDecimal promotionAmount;

    @ApiModelProperty("实际价格")
    private BigDecimal actualPrice;

    @ApiModelProperty("货品数量")
    private Integer itemCount;

    @ApiModelProperty("实际下单数量")
    private Integer actualOrderCount;

    @ApiModelProperty("缺货数量")
    private Integer itemLoseCount;

    /**
     * 小计
     */
    private BigDecimal amountTotal;

    @ApiModelProperty("货品在库总数")
    private Integer inWarehouseCount;

    @ApiModelProperty("货品在途总数")
    private Integer onWayCount;

    /**
     * 商品状态
     */
    private Short saleStatus;

    /**
     * 货品最大数量
     */
    private Integer maxCount;

    @ApiModelProperty("商品类型")
    private Short goodsType;

    private String moq;

    private String barCode;

    /**
     * 1-支持预售 0-不支持
     */
    private Short advanceSaleFlag;

    private Integer brandId;

    @ApiModelProperty("规格值id")
    private Integer specificationValueId;

    @ApiModelProperty("颜色值id")
    private Integer colorValueId;

    @ApiModelProperty("货品类型 1-主货品 2-赠品")
    private Integer itemType;

    @ApiModelProperty("商品等级折扣Id")
    private Integer gradeDiscountId;

    @ApiModelProperty("促销活动规则（条件）ID")
    private Integer ruleId;

    @ApiModelProperty("订单促销活动规则ID（暂时没有使用）")
    private Integer orderRuleId;
}
