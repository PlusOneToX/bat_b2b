package com.bat.promotion.api.check.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "活动审批详情")
public class PromotionCheckDTO {

    @ApiModelProperty(value = "审批单号", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券", example = "1")
    private Short promotionType;
    @ApiModelProperty(value = "审批状态 0, 审批中 1,审批通过，2审批未通过 ", example = "0")
    private Short checkStatus;
    @ApiModelProperty(value = "审批类型: 1 新增 2 修改", example = "1")
    private Short checkType;
    @ApiModelProperty(value = "当前审批人id", example = "123456")
    private Integer checkUserId;
    @ApiModelProperty(value = "当前审批人名称", example = "小铭")
    private String checkUserName;
    @ApiModelProperty(value = "发起人id", example = "123456")
    private Integer userId;
    @ApiModelProperty(value = "发起人", example = "小铭")
    private String userName;
    @ApiModelProperty(value = "创建时间或发起时间", example = "2019-12-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "审批流程")
    private List<PromotionCheckFlowDTO> checkFlows;

    @ApiModelProperty(value = "促销活动详情(活动类型为1，且审批类型为1时有值)")
    private PromotionDTO promotion;

    @ApiModelProperty(value = "拼团秒杀活动详情(活动类型为2，且审批类型为1时有值)")
    private GroupSeckillDTO groupSeckill;

    @ApiModelProperty(value = "优惠券活动详情(活动类型为3，且审批类型为1时有值)")
    private CouponDTO coupon;

    @ApiModelProperty(value = "返利代金券活动详情(多个)(活动类型为4，且审批类型为1时有值)")
    private List<RebateVoucherDTO> rebateVouchers;

    @ApiModelProperty(value = "活动修改列表(审批类型为2时有值)")
    private List<PromotionCheckContentDTO> checkContents;
}
