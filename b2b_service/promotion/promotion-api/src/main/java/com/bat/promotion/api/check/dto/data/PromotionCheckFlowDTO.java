package com.bat.promotion.api.check.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批流信息")
public class PromotionCheckFlowDTO {

    @ApiModelProperty(value = "审批流id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "审批人id", example = "123456")
    private Integer userId;
    @ApiModelProperty(value = "审批人名称", example = "小铭")
    private String userName;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "审批时间", example = "2019-12-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    @ApiModelProperty(value = "审批备注", example = "2019-12-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private String remark;
    @ApiModelProperty(value = "审批顺序", example = "1")
    private Integer checkSort;
}
