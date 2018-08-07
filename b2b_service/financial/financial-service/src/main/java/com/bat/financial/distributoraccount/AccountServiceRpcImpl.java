package com.bat.financial.distributoraccount;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.distributoraccount.executor.AccountWxDistributorQryExc;
import com.bat.financial.distributoraccount.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.financial.account.dto.AccountWxDistributorRpcQryDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialDistributorAccountServiceRpc;
import com.bat.dubboapi.financial.basesetting.dto.data.AccountWxDistributorRpcDTO;
import com.bat.dubboapi.financial.common.Response;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
import com.bat.financial.common.error.DistributorAccountErrorCode;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/25 16:49
 */
@DubboService
public class AccountServiceRpcImpl implements FinancialDistributorAccountServiceRpc {
    @Resource
    private AccountWxDistributorQryExc qryExc;

    @Override
    public Response<AccountWxDistributorRpcDTO> getDistributorWxAccountById(Integer id) {
        AccountWxDistributorDTO accountById = qryExc.getAccountById(id);
        if (accountById != null) {
            AccountWxDistributorRpcDTO dto = new AccountWxDistributorRpcDTO();
            BeanUtils.copyProperties(accountById, dto);
            return Response.of(dto);
        }
        return Response.buildFailure(ErrorCode.B_ACCOUNT_NOT_EXISTS, MessageUtils.get(ErrorCode.B_ACCOUNT_NOT_EXISTS));
    }

    @Override
    public Response<List<AccountWxDistributorRpcQryDTO>> listWxPayAccountByCondition(Integer distributorId,
        String appId, Short accountType) {
        List<AccountWxDistributorDO> accountWxDistributorDOList =
            qryExc.listByCondition(distributorId, null, appId, accountType);
        if (accountWxDistributorDOList == null || accountWxDistributorDOList.size() == 0) {
            return Response.buildFailure(DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL,
                MessageUtils.get(DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL));
        }
        List<AccountWxDistributorRpcQryDTO> list = new ArrayList<>();
        for (int x = 0; x < accountWxDistributorDOList.size(); x++) {
            AccountWxDistributorDO accountWxDistributorDO = accountWxDistributorDOList.get(x);
            AccountWxDistributorRpcQryDTO rpcQryDTO = new AccountWxDistributorRpcQryDTO();
            BeanUtils.copyProperties(accountWxDistributorDO, rpcQryDTO);
            list.add(rpcQryDTO);
        }
        return Response.of(list);
    }

}
