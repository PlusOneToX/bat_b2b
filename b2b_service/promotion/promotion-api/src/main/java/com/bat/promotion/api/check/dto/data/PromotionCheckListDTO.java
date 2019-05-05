package com.bat.promotion.api.check.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批列表信息")
public class PromotionCheckListDTO {

    @ApiModelProperty(value = "审批单号", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "分销商编号Id", example = "123456")
    private Integer promotionId;
    @ApiModelProperty(value = "活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券", example = "1")
    private Short promotionType;
    @ApiModelProperty(value = "拼团秒杀：1拼团 2秒杀(活动类型为2时有值)", example = "1")
    private Short groupSeckillType;
    @ApiModelProperty(value = "活动名称", example = "RO12345")
    private String name;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "审批类型: 1 新增 2 修改", example = "1")
    private Short checkType;
    @ApiModelProperty(value = "发起人id", example = "123456")
    private Integer userId;
    @ApiModelProperty(value = "发起人", example = "小铭")
    private String userName;
    @ApiModelProperty(value = "创建时间或发起时间", example = "2019-12-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
