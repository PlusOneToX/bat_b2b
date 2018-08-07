package com.bat.financial.deposit;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.deposit.constant.ErpBalanceFlag;
import com.bat.financial.deposit.executor.DepositBalanceCmdExc;
import com.bat.financial.deposit.executor.DepositBalanceQryExc;
import com.bat.financial.deposit.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorInfoRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.AccountBalanceChangeReq;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.deposit.api.FinancialDepositServiceRpc;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyFinancialServiceErpRpc;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.dto.BalanceInfoSyncQry;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:57
 */
@DubboService
@Slf4j
public class DepositBalanceServiceRpcImpl implements FinancialDepositServiceRpc {

    @Resource
    private DepositBalanceCmdExc depositCmdExc;

    @Resource
    private DepositBalanceQryExc depositQryExc;

    @Resource
    private DepositServiceImpl depositService;

    @Resource
    private DepositBalanceService depositBalanceService;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartyFinancialServiceErpRpc thirdPartyFinancialServiceErpRpc;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @Override
    public Response accountBalanceChange(AccountBalanceChangeReq req) {
        try {
            DistributorInfoRpcDTO dto =
                distributorServiceRpc.getDistributorInfoByErpId(Integer.valueOf(req.getErpDistributorId() + ""));
            if (dto != null) {
                if (dto.getErpBalanceFlag().equals(ErpBalanceFlag.NO)) {
                    return Response.buildFailure(ErrorCode.B_BALANCE_NO_SYNC,
                        MessageUtils.get(ErrorCode.B_BALANCE_NO_SYNC));
                }
                Integer distributorId = dto.getId();
                DepositDistributorSubsidiaryBookDTO bookDTO = new DepositDistributorSubsidiaryBookDTO();
                bookDTO.setDistributorId(distributorId);
                bookDTO.setDistributorName(dto.getName());
                bookDTO.setBusinessType(DepositBusinessType.ERP_INCREMENT);
                bookDTO.setBusinessId(req.getBusinessNo() + "");
                bookDTO.setChangeType(req.getChangeType());
                bookDTO.setAmount(BigDecimal.valueOf(req.getAmount()));
                bookDTO.setRemark(req.getBillNo());
                depositService.accountBalanceChange(bookDTO);
                return Response.buildSuccess();
            }
            return Response.buildFailure(ErrorCode.B_ERP_DISTRIBUTOR_ID_B2B_NOT_EXISTS,
                MessageUtils.get(ErrorCode.B_ERP_DISTRIBUTOR_ID_B2B_NOT_EXISTS));
        } catch (FinancialException e) {
            return Response.buildFailure(e.getCode(), e.getMsg());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }

    @Override
    public Response syncDepositBalanceInfo() {
        BalanceInfoSyncQry qry = new BalanceInfoSyncQry();
        try {
            depositBalanceService.syncDepositBalanceInfo(qry);
            return Response.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_DEPOSIT_BALANCE_SYNC_ERROR, null);
        }
    }

}