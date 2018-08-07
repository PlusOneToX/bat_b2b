package com.bat.financial.distributoraccount.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorCreateCmd;
import com.bat.financial.api.distributoraccount.dto.AccountWxDistributorUpdateCmd;
import com.bat.financial.common.constant.account.DistributorAccountConstant;
import com.bat.financial.common.error.DistributorAccountErrorCode;
import com.bat.financial.common.error.FinancialCommonErrorCode;

@Component
public class DistributorAccountValidator {

    /**
     * 校验微信支付新增
     * 
     * @param cmd
     */
    public static void validatorWxPayAccountCreate(AccountWxDistributorCreateCmd cmd) {
        if (DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SELF.equals(cmd.getAccountType())) {
            return;
        }
        if (DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE_PROVIDER_SECOND
            .equals(cmd.getAccountType()) && cmd.getSubAccountRatio() == null) {
            throw FinancialException
                .buildException(DistributorAccountErrorCode.P_DISTRIBUTOR_ACCOUNT_SUB_ACCOUNT_RATIO_NULL);
        }
        if (cmd.getSubAccountRatio() != null && cmd.getSubAccountRatio().compareTo(BigDecimal.ZERO) < 0) {
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_NUMBER_ILLEGAL);
        }
        if (cmd.getSubAccountRatio() != null && cmd.getSubAccountRatio().compareTo(new BigDecimal("100")) > 0) {
            throw FinancialException
                .buildException(FinancialCommonErrorCode.COMMON_NUMBER_PERCENTAGE_GREATER_THEN_HUNDRED);
        }
        // 百分比转为小数
        if (cmd.getSubAccountRatio() != null) {
            cmd.setSubAccountRatio(cmd.getSubAccountRatio().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
        }
    }

    public static void validatorWxPayAccountUpdate(AccountWxDistributorUpdateCmd cmd) {
        if (DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SELF.equals(cmd.getAccountType())) {
            return;
        }
        if (DistributorAccountConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE_PROVIDER_SECOND
            .equals(cmd.getAccountType()) && cmd.getSubAccountRatio() == null) {
            throw FinancialException
                .buildException(DistributorAccountErrorCode.P_DISTRIBUTOR_ACCOUNT_SUB_ACCOUNT_RATIO_NULL);
        }
        if (cmd.getSubAccountRatio() != null && cmd.getSubAccountRatio().compareTo(BigDecimal.ZERO) == -1) {
            throw FinancialException.buildException(FinancialCommonErrorCode.COMMON_NUMBER_ILLEGAL);
        }
        if (cmd.getSubAccountRatio() != null && cmd.getSubAccountRatio().compareTo(new BigDecimal("100")) == 1) {
            throw FinancialException
                .buildException(FinancialCommonErrorCode.COMMON_NUMBER_PERCENTAGE_GREATER_THEN_HUNDRED);
        }
        // 百分比转为小数
        if (cmd.getSubAccountRatio() != null) {
            cmd.setSubAccountRatio(cmd.getSubAccountRatio().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
        }
    }
}
