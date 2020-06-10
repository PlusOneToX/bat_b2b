package com.bat.flexible.dao.exchange.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ExchangeSpecialDistributorCO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "专题id")
    private Integer exchangeSpecialId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "页面访问次数")
    private Integer pageVisits;

    @ApiModelProperty(value = "一次转发次数")
    private Integer oneForwardTimes;

    @ApiModelProperty(value = "二次转发次数")
    private Integer twoForwardTimes;

    @ApiModelProperty(value = "福利领取次数")
    private Integer receiveTimes;

    @ApiModelProperty(value = "链接")
    private String link;

    @ApiModelProperty(value = "状态 0禁用 1启用")
    private Short status;

    @ApiModelProperty(value = "小程序二维码链接")
    private String qrCodeUrl;

    @ApiModelProperty(value = "短链接")
    private String shortLink;

}
