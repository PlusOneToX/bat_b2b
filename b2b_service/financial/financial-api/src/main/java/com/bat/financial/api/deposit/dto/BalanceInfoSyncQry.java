package com.bat.financial.api.deposit.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/28 11:11
 */
@Data
@ApiModel(value = "BalanceInfoSyncQry", description = "账户余额同步")
public class BalanceInfoSyncQry {
    /**
     * 分销商内码
     */
    private List<String> FCUSTID;
}
