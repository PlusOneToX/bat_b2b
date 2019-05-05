package com.bat.thirdparty.log.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommonLogDTO {

    @ApiModelProperty(value = "主键")
    private String _id;
    /**
     * 业务模块
     */
    @ApiModelProperty(value = "业务模块")
    private String businessModule;
    /**
     * 业务功能
     */
    @ApiModelProperty(value = "业务功能")
    private String businessFunction;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Integer businessId;
    /**
     * 操作来源
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
