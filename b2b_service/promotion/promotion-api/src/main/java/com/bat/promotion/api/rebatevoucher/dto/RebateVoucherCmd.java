package com.bat.promotion.api.rebatevoucher.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 16:15
 */
@Data
@ApiModel(value = "RebateVoucherCmd", description = "返利代金券信息")
public class RebateVoucherCmd {

    @ApiModelProperty(value = "返利代金券id(修改情况必填)", required = false, example = "1")
    private Integer id;

    // @NotNull(message = "P_PROMOTION_REBATE_VOUCHER_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", required = true, example = "2491")
    private Integer distributorId;

    // @NotBlank(message = "P_PROMOTION_REBATE_VOUCHER_DISTRIBUTOR_NAME_NULL")
    @ApiModelProperty(value = "分销商名称", required = true, example = "test01")
    private String distributorName;

    // @NotBlank(message = "P_PROMOTION_REBATE_VOUCHER_NAME_NULL")
    @ApiModelProperty(value = "代金券券名", required = true, example = "九月返利")
    private String name;

    @ApiModelProperty(value = "代金券编号(更新必传)", required = false, example = "九月返利")
    private String voucherNo;

    // @NotNull(message = "P_PROMOTION_REBATE_VOUCHER_FACE_VALUE_NULL")
    @ApiModelProperty(value = "面值", required = true, example = "200")
    private BigDecimal faceValue;

    // private BigDecimal balance;

    // private Short applyStatus;

    @ApiModelProperty(value = "冻结状态 10未冻结(可用) 11冻结", required = false, example = "10")
    private Short freezeStatus;

    // private Short voucherStatus;

    // @NotNull(message = "P_PROMOTION_REBATE_VOUCHER_START_TIME_NULL")
    @ApiModelProperty(value = "开始时间", required = true, example = "2019-12-31 00:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    // @NotNull(message = "P_PROMOTION_REBATE_VOUCHER_END_TIME_NULL")
    @ApiModelProperty(value = "结束时间", required = true, example = "2019-12-31 23:59:59")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    // private Integer sort;

    private String remark;

    // private Date createTime;

    // private Date updateTime;
}
