package com.bat.financial.api.platformaccount.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:46
 */
@Data
@ApiModel(value = "AccountKuaiQianUpdateCmd", description = "平台收款账户(快钱)更新   ")
public class AccountKuaiQianUpdateCmd {

    @NotNull(message = "P_ACCOUNT_KUAIQIAN_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_ACCOUNT_KUAIQIAN_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "销售组织id", required = true, example = "1")
    private Integer organizationId;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @ApiModelProperty(value = "erp账户编码", required = false, example = "00001")
    private String erpAccountNo;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_BANK_NO_NULL")
    @ApiModelProperty(value = "erp银行账号", required = true, example = "10863000000410707")
    private String bankNo;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_MER_CHAN_ACCT_ID_NULL")
    @ApiModelProperty(value = "人民币网关账号", required = true, example = "10863000000410707")
    private String merchanAcctId;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_SIGN_FILE_URL_NULL")
    @ApiModelProperty(value = "签名文件url", required = true, example = "10863000000410707")
    private String signFileUrl;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_SIGN_PWD_NULL")
    @ApiModelProperty(value = "签名密码", required = true, example = "10863000000410707")
    private String signPwd;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_SIGN_PRIVATE_KEY_NULL")
    @ApiModelProperty(value = "签名私钥", required = true, example = "10863000000410707")
    private String signPrivateKey;

    @NotBlank(message = "P_ACCOUNT_KUAIQIAN_CHECK_SIGN_FILE_URL_NULL")
    @ApiModelProperty(value = "检查签名文件url", required = true, example = "10863000000410707")
    private String checkSignFileUrl;

    @NotNull(message = "P_ACCOUNT_KUAIQIAN_BACK_TYPE_NULL")
    @ApiModelProperty(value = "取消订单是否原路返回 1、是 2、否", required = true, example = "1")
    private Short backType;

    @NotNull(message = "P_ACCOUNT_KUAIQIAN_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用,0停用", required = true, example = "1")
    private Short openFlag;

}
