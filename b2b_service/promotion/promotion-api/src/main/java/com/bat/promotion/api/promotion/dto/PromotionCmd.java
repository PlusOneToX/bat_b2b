package com.bat.promotion.api.promotion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(value = "PromotionCmd", description = "促销活动信息")
public class PromotionCmd {
    @ApiModelProperty(value = "促销活动id(修改情况必填)", required = false, example = "123456")
    private Integer id;
    @NotBlank(message = "P_PROMOTION_NAME_NULL")
    @ApiModelProperty(value = "促销活动名称", required = true, example = "促销活动名称")
    private String name;
    @ApiModelProperty(value = "促销活动英文名称", required = false, example = "促销活动英文名称")
    private String nameEn;
    @ApiModelProperty(value = "促销活动描述", required = false, example = "促销活动描述")
    private String promoDesc;
    @NotNull(message = "P_PROMOTION_START_TIME_NULL")
    @ApiModelProperty(value = "开始时间", required = true, example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @NotNull(message = "P_PROMOTION_END_TIME_NULL")
    @ApiModelProperty(value = "结束时间", required = true, example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @NotNull(message = "P_PROMOTION_ADVANCE_FLAG_NULL")
    @ApiModelProperty(value = "是否提前展示：1 准时2 提前", required = true, example = "1")
    private Short advanceFlag;
    @ApiModelProperty(value = "申请状态：0草稿(当是草稿时，必填) 1申请中 2申请通过 3申请失败", required = false, example = "1")
    private Short applyStatus;
    @NotNull(message = "P_PROMOTION_TYPE_NULL")
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动", required = true, example = "1")
    private Short promoType;
    @NotNull(message = "P_PROMOTION_ON_WAY_FLAG_NULL")
    @ApiModelProperty(value = "在途商品是否参与活动 1.参与，0.不参与", required = true, example = "1")
    private Short onWayFlag;
    @NotNull(message = "P_PROMOTION_DISTRIBUTOR_SCOPE_NULL")
    @ApiModelProperty(value = "分销商可视范围：1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "1")
    private Short distributorScope;
    @NotNull(message = "P_PROMOTION_DISTRIBUTOR_SCOPE_NO_NULL")
    @ApiModelProperty(value = "分销商不可视范围：0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "0")
    private Short distributorScopeNo;

    @ApiModelProperty(value = "可视，分销商价格等级id列表(分销商可视范围为2时必填)", required = false)
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "可视，销售部门id列表(分销商可视范围为4时必填)", required = false)
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "可视，业务员id列表(分销商可视范围为5时必填)", required = false)
    private List<Integer> adminIds;
    @Valid
    @ApiModelProperty(value = "可视，分销商id列表(分销商可视范围为3时必填)", required = false)
    private List<PromotionDistributorScopeCmd> distributors;
    @ApiModelProperty(value = "不可视，分销商等级id列表(分销商不可视范围为2时必填)", required = false)
    private List<Integer> scalePriceNoIds;
    @ApiModelProperty(value = "不可视，销售部门id列表(分销商不可视范围为4时必填)", required = false)
    private List<Integer> departmentNoIds;
    @ApiModelProperty(value = "不可视，业务员id列表(分销商不可视范围为5时必填)", required = false)
    private List<Integer> adminNoIds;
    @ApiModelProperty(value = "不可视，分销商id列表(分销商不可视范围为3时必填)", required = false)
    private List<Integer> distributorNoIds;

    @Valid
    @NotNull(message = "P_PROMOTION_RULE_NULL")
    @ApiModelProperty(value = "活动规则列表", required = true)
    private List<PromotionRuleCmd> rules;
}
