package com.bat.distributor.service.common;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.DistributorBusinessUpdateCmd;
import com.bat.distributor.api.distributor.dto.DistributorUpdateCmd;
import com.bat.distributor.dao.distributor.dataobject.DistributorDO;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import com.bat.distributor.service.subaccount.validator.SubAccountValidator;
import org.springframework.stereotype.Component;

import static com.bat.distributor.service.common.Constant.APPLY_STATUS_1;
import static com.bat.distributor.service.common.Constant.ERP_FLAG_1;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/16 13:44
 */

@Component
public class CommonValidator {

    public static void checkDistributorValidator(DistributorDO beforeDistributorDO, DistributorUpdateCmd cmd) {
        if (beforeDistributorDO.getTreeNode() == 1 || cmd.getExtendData().getErpFlag().equals(ERP_FLAG_1)) {
            if (cmd.getAddress() == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_ADDRESS_NULL);
            }
            if (cmd.getFinancial() == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_FINANCIAL_NULL);
            }
            if (cmd.getExtendData().getErpBalanceFlag() == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_ERP_BALANCE_FLAG);
            }
        }
        if (beforeDistributorDO.getTreeNode() == 1) {
            DistributorBusinessUpdateCmd business = cmd.getBusiness();
            if (business.getScalePriceId() == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_ONE_DEFAULT_SCALE_PRICE_NULL);
            }
        }
        // 多级分销商需审核通过后才能编辑
        if (beforeDistributorDO.getTreeNode() > 1 && beforeDistributorDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_APPLY_STATUS_1_ERROR);
        }
        //判断分账
        SubAccountValidator.validateAdminConfigParam(cmd.getExtendData(),cmd.getSubAccountAdminConfigCmd());
    }

}
