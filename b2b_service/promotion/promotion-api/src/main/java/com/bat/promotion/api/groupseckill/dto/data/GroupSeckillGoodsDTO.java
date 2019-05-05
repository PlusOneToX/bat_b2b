package com.bat.promotion.api.groupseckill.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "拼团秒杀活动商品信息")
public class GroupSeckillGoodsDTO {
    @ApiModelProperty(value = "拼团秒杀活动商品关系id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "拼团秒杀活动id", example = "123456")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "商品id", example = "123445")
    private Integer goodsId;
    @ApiModelProperty(value = "商品编码", example = "商品名称")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "货品id", example = "123445")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码", example = "货品编码")
    private String itemCode;
    @ApiModelProperty(value = "ERP物料60码", example = "121323")
    private String barCode;
    @ApiModelProperty(value = "货品名称", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "规格", example = "货品规格")
    private String specsName;
    @ApiModelProperty(value = "颜色", example = "货品颜色")
    private String colorName;
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "优先使用现有库存 1、是 0、否", example = "1")
    private Short existFlag;
    @ApiModelProperty(value = "是否支持预售 1、支持 0、不支持", example = "1")
    private Short mtoFlag;
    @ApiModelProperty(value = "最大拼团或秒杀数量、默认不限制", example = "10")
    private Integer maxNum;
    @ApiModelProperty(value = "最小起拼或秒杀数量、默认不限制", example = "10")
    private Integer minNum;
    @ApiModelProperty(value = "拼团秒杀价", example = "10.00")
    private BigDecimal groupSeckillPrice;
    @ApiModelProperty(value = "虚拟拼团秒杀数", example = "100")
    private Integer virtualSum;
    @ApiModelProperty(value = "已拼或已秒实际数量", example = "100")
    private Integer realSum;
}
