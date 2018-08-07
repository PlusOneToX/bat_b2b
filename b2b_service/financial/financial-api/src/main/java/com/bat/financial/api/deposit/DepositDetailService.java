package com.bat.financial.api.deposit;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.DepositDetailSummaryUserQry;
import com.bat.financial.api.deposit.dto.data.DepositDetailSummaryDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.deposit.dto.DepositDetailUserQry;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:39
 */
public interface DepositDetailService {

    /**
     * 通过后台用户id 和 查询参数获取余额信息
     * 
     * @param qry
     * @return
     */
    PageInfo<DepositDistributorSubsidiaryBookDTO> listDepositDetail(DepositDetailAdminQry qry);

    /**
     * 通过分销商id获取 余额信息
     * 
     * @param qry
     * @return
     */
    PageInfo<DepositDistributorSubsidiaryBookDTO> listDepositDetailByDistributorId(DepositDetailUserQry qry);

    /**
     * 移动端 月汇总
     * 
     * @param qry
     * @return
     */
    DepositDetailSummaryDTO listDepositDetailSummary(DepositDetailSummaryUserQry qry);

    /**
     * 创建余额明细(信息全)
     * 
     * @param dto
     */
    void createDepositDetail(DepositDistributorSubsidiaryBookDTO dto);

}
