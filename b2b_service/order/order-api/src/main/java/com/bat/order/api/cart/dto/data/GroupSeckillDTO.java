package com.bat.order.api.cart.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "购物车商品拼团秒杀信息")
public class GroupSeckillDTO {
    @ApiModelProperty(value = "拼团秒杀规则id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "拼团秒杀活动id", example = "123456")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "拼团秒杀活动名称", example = "拼团秒杀活动名称")
    private String name;
    @ApiModelProperty(value = "拼团秒杀活动描述", example = "促销活动描述")
    private Short groupSeckillDesc;
    @ApiModelProperty(value = "拼团秒杀：1拼团 2秒杀", example = "1")
    private Short groupSeckillType;
    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束", example = "1")
    private Short groupSeckillStatus;
    @ApiModelProperty(value = "货品id", example = "123445")
    private Integer itemId;
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
