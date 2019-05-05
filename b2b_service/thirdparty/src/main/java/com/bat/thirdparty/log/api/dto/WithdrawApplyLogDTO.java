package com.bat.thirdparty.log.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class WithdrawApplyLogDTO {

    @ApiModelProperty(value = "主键")
    private String _id;

    @ApiModelProperty(value = "提现申请id")
    private Integer withdrawApplyId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "操作来源")
    private String operateSource;

    @ApiModelProperty(value = "操作人id")
    private Integer operateId;

    @ApiModelProperty(value = "操作人名称")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "操作类型")
    private String operateType;

    @ApiModelProperty(value = "操作说明")
    private String operateDes;

    @ApiModelProperty(value = "操作数据")
    private String operateData;
}
