package com.bat.financial.deposit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/17 22:29
 */
@Service
public class DepositServiceImpl {

    @Resource
    private DepositDetailService depositDetailService;

    @Resource
    private DepositBalanceService depositBalanceService;

    @Resource
    private CommonService commonService;

    @Transactional(rollbackFor = Exception.class)
    public void accountBalanceChange(DepositDistributorSubsidiaryBookDTO bookDTO) {
        DepositDistributorDTO dto = depositBalanceService.getDepositBalanceByDistributorId(bookDTO.getDistributorId());
        /**
         * //TODO 这一块信息补全 感觉确实被我做复杂了
         */
        commonService.calcBalanceAndBook(dto, bookDTO);
        depositBalanceService.updateDepositBalance(dto);
        depositDetailService.createDepositDetail(bookDTO);
    }
}
