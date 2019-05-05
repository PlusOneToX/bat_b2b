package com.bat.promotion.api.groupseckill.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "拼团秒杀活动商品信息")
public class GroupSeckillGoodsCmd {
    @ApiModelProperty(value = "拼团秒杀活动商品关系id", required = false, example = "123456")
    private Integer id;
    @ApiModelProperty(value = "拼团秒杀活动id", required = false, example = "123456")
    private Integer groupSeckillId;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品id", required = true, example = "123445")
    private Integer goodsId;
    @NotBlank(message = "P_PROMOTION_GROUP_SECKILL_GOODS_NO_NULL")
    @ApiModelProperty(value = "商品编码", required = true, example = "商品名称")
    private String goodsNo;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "货品id", required = true, example = "123445")
    private Integer itemId;
    @NotBlank(message = "P_PROMOTION_GROUP_SECKILL_GOODS_ITEM_CODE_NULL")
    @ApiModelProperty(value = "货品编码(规则对象为货品时有值)", required = true, example = "货品编码")
    private String itemCode;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序号", required = true, example = "0")
    private Integer sort;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_EXITS_FLAG_NULL")
    @ApiModelProperty(value = "优先使用现有库存 1、是 0、否", required = true, example = "1")
    private Short existFlag;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_MTO_FLAG_NULL")
    @ApiModelProperty(value = "是否支持预售 1、支持 0、不支持", required = true, example = "1")
    private Short mtoFlag;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_MAX_NUM_NULL")
    @ApiModelProperty(value = "最大拼团或秒杀数量(填0不限制)", required = true, example = "10")
    private Integer maxNum;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_GOODS_MIN_NUM_NULL")
    @ApiModelProperty(value = "最小起拼或秒杀数量(填0不限制)", required = true, example = "10")
    private Integer minNum;
    @NotNull(message = "P_PROMOTION_GROUP_SECKILL_PRICE_NULL")
    @ApiModelProperty(value = "拼团秒杀价", required = true, example = "10.00")
    private BigDecimal groupSeckillPrice;
    @ApiModelProperty(value = "虚拟拼团秒杀数", required = false, example = "100")
    private Integer virtualSum;
}
