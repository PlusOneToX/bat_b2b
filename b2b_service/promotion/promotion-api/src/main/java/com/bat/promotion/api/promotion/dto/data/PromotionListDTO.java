package com.bat.promotion.api.promotion.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动列表信息")
public class PromotionListDTO {
    @ApiModelProperty(value = "促销活动id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动名称", example = "促销活动名称")
    private String name;
    @ApiModelProperty(value = "促销活动英文名称", example = "促销活动英文名称")
    private String nameEn;
    @ApiModelProperty(value = "促销活动描述", example = "促销活动描述")
    private String promoDesc;
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "申请状态：0草稿 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束", example = "1")
    private Short promoStatus;
    @ApiModelProperty(value = "活动来源，1 后台新增, 2 批量导入", example = "1")
    private Short promoSource;
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动", example = "1")
    private Short promoType;
    @ApiModelProperty(value = "在途商品是否参与活动 1.参与，0.不参与", example = "1")
    private Short onWayFlag;
    @ApiModelProperty(value = "分销商可视范围：1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "1")
    private Short distributorScope;
    @ApiModelProperty(value = "分销商不可视范围：0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "0")
    private Short distributorScopeNo;
}
