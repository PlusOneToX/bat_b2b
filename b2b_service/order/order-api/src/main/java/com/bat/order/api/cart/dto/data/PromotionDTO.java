package com.bat.order.api.cart.dto.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "购物车商品促销活动信息")
public class PromotionDTO implements Serializable {
    @ApiModelProperty(value = "促销活动id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动名称", example = "促销活动名称")
    private String name;
    @ApiModelProperty(value = "促销活动英文名称", example = "促销活动英文名称")
    private String nameEn;
    @ApiModelProperty(value = "促销活动描述", example = "促销活动描述")
    private String promoDesc;
    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束", example = "1")
    private Short promoStatus;
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动", example = "1")
    private Short promoType;
    @ApiModelProperty(value = "在途商品是否参与活动 1.参与，0.不参与", example = "1")
    private Short onWayFlag;

    @ApiModelProperty(value = "促销活动规则列表")
    private List<PromotionRuleDTO> rules;
}
