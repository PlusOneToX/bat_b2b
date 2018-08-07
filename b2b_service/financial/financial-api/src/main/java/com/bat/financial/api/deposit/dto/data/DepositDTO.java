package com.bat.financial.api.deposit.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 20:06
 */
@Data
public class DepositDTO {
    private Integer id;
    /**
     * 显示预存款 0为否 1为是
     */
    @ApiModelProperty(value = "显示预存款 0为否 1为是")
    private Byte isShowPrestore;

    /**
     * 开启线上充值 0为否 1为是
     */
    @ApiModelProperty(value = "开启线上充值 0为否 1为是")
    private Byte isOpenOnlineTopup;

    /**
     * 充值最小额度
     */
    @ApiModelProperty(value = "充值最小额度")
    private Float onlineMinAmount;

    /**
     * 开启线下充值 0为否 1为是
     */
    @ApiModelProperty(value = "开启线下充值 0为否 1为是")
    private Byte isOpenOfflineTopup;

    /**
     * 开启提现 0为否 1为是
     */
    @ApiModelProperty(value = "开启提现 0为否 1为是")
    private Byte isOpenWithdrawal;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
}
