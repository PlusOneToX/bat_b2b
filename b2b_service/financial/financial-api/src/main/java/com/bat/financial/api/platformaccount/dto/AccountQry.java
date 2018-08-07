package com.bat.financial.api.platformaccount.dto;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:09
 */
@Data
@ApiModel(value = "PlatformAccountQry", description = "平台账户查询")
public class AccountQry extends BaseSearchQry {

    public AccountQry() {
        super.attributeMapper.put((short)1, "setAccountName");
        super.attributeMapper.put((short)2, "setAppId");
        super.attributeMapper.put((short)3, "setAccountNo");
        super.attributeMapper.put((short)4, "setMerchanAcctId");
        super.attributeMapper.put((short)5, "setBankName");
        super.attributeMapper.put((short)6, "setCardNo");
    }

    @ApiModelProperty(value = "1.组织id", example = "1")
    private Integer organizationId;

    @ApiModelProperty(value = "应用类型 1、微信公众号 2、微信小程序", example = "1")
    private Short appType;

    @ApiModelProperty(value = "1.收款账户名称", example = "")
    private String accountName;

    @ApiModelProperty(value = "2.微信应用appid", example = "wx63c410d5de384240")
    private String appId;

    @ApiModelProperty(value = "3.微信商户号", example = "1341583701")
    private String accountNo;

    @ApiModelProperty(value = "4.人民币网关账号", example = "10863000000410707")
    private String merchanAcctId;

    @ApiModelProperty(value = "5.开户行名称", example = "wx63c410d5de384240")
    private String bankName;

    @ApiModelProperty(value = "6.银行卡号", example = "wx63c410d5de384240")
    private String cardNo;

}
