package com.bat.distributor.api.user.dto.user.data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商支付方式")
public class UserPayMentModeDTO implements Serializable {

    @ApiModelProperty(value = "结算方式，1为立即支付，2为期间结算", example = "1")
    private Short payWay;
    @ApiModelProperty(value = "结算描述(结算方式为2时有值)", example = "1")
    private String payName;
    @ApiModelProperty(value = "结算英文描述(结算方式为2时有值)", example = "1")
    private String payNameEn;
    @ApiModelProperty(value = "结算时长(结算方式为2时有值)", example = "30")
    private Integer settlingTime;
    @ApiModelProperty(value = "是否支持线下转账： 1支持 0不支持", example = "0")
    private Short OfflineFlag;

}
