package com.bat.financial.deposit;

import javax.annotation.Resource;

import com.bat.financial.deposit.executor.DepositDetailCmdExc;
import com.bat.financial.deposit.executor.DepositDetailQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.DepositDetailSummaryUserQry;
import com.bat.financial.api.deposit.dto.DepositDetailUserQry;
import com.bat.financial.api.deposit.dto.data.DepositDetailSummaryDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:57
 */
@Service
@Slf4j
public class DepositDetailServiceImpl implements DepositDetailService {

    @Resource
    private DepositDetailQryExc depositDetailQryExc;

    @Resource
    private DepositDetailCmdExc depositDetailCmdExc;

    /**
     * 根据后台用户获取分销商余额明细(多个分销商id)
     *
     * @param qry
     * @return
     */
    @Override
    public PageInfo<DepositDistributorSubsidiaryBookDTO> listDepositDetail(DepositDetailAdminQry qry) {
        return depositDetailQryExc.listDepositDetailByDistributorIdsAndParams(qry);
    }

    /**
     * 根据分销商id获取余额明细即资产明细（单个分销商id）
     *
     * @param qry
     * @return
     */
    @Override
    public PageInfo<DepositDistributorSubsidiaryBookDTO> listDepositDetailByDistributorId(DepositDetailUserQry qry) {
        return depositDetailQryExc.listDepositDetailByDistributorIdAndParams(qry);
    }

    /**
     * 资产明细汇总 按月统计总收入总支出
     *
     * @param qry
     * @return
     */
    @Override
    public DepositDetailSummaryDTO listDepositDetailSummary(DepositDetailSummaryUserQry qry) {
        return depositDetailQryExc.listDepositDetailSummary(qry);
    }

    @Override
    public void createDepositDetail(DepositDistributorSubsidiaryBookDTO dto) {
        depositDetailCmdExc.createDepositDetail(dto);
    }

}
