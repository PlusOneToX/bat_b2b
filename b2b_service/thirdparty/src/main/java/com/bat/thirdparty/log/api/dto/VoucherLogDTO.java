package com.bat.thirdparty.log.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class VoucherLogDTO {

    @ApiModelProperty(value = "主键")
    private String _id;
    /**
     * 收款单id
     */
    @ApiModelProperty(value = "收款单id")
    private Integer voucherId;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    /**
     * 操作来源 "platform"
     */
    @ApiModelProperty(value = "操作来源")
    private String operateSource;
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Integer operateId;
    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private String operateType;
    /**
     * 操作说明
     */
    @ApiModelProperty(value = "操作说明")
    private String operateDes;
    /**
     * 操作数据
     */
    @ApiModelProperty(value = "操作数据")
    private String operateData;
}
