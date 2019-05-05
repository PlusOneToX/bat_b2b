package com.bat.promotion.api.rebatevoucher.dto.data;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "返利代金券使用信息")
public class RebateVoucherUsageRecordDTO {

    private Integer id;
    @ApiModelProperty(value = "返利代金券id", example = "1")
    private Integer rebateVoucherId;
    @ApiModelProperty(value = "返利代金券编号", example = "1")
    private String rebateVoucherNo;
    @ApiModelProperty(value = "使用金额 负数为使用 正数为退还", example = "1")
    private BigDecimal useAmount;
    @ApiModelProperty(value = "余额", example = "1")
    private BigDecimal balance;
    @ApiModelProperty(value = "订单id", example = "1")
    private Integer orderId;
    @ApiModelProperty(value = "订单编号", example = "1")
    private String orderNo;
    @ApiModelProperty(value = "使用时间", example = "1")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;
}