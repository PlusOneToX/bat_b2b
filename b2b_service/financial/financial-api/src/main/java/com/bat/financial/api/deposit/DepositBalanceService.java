package com.bat.financial.api.deposit;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.BalanceInfoSyncQry;
import com.bat.financial.api.deposit.dto.DepositAvailableQry;
import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:39
 */
public interface DepositBalanceService {

    /**
     * 获取余额相关配置信息
     * 
     * @return
     */
    DepositDTO getDepositSet();

    /**
     * 根据后台用户id 获取对应分销商余额信息
     *
     * @param qry
     * @return
     */
    PageInfo<DepositDistributorDTO> listDepositAvailableByUserId(DepositAvailableQry qry);

    /**
     * 根据分销商id获取分销商余额信息
     * 
     * @param id
     * @return
     */
    DepositDistributorDTO getDepositBalanceByDistributorId(Integer id);

    /**
     * 根据分销商ids获取分销商余额信息
     *
     * @param ids
     * @return
     */
    List<DepositDistributorDTO> listDepositAvailableByDistributorIds(List<Integer> ids);

    /**
     * 同步erp余额信息
     *
     * @param syncCmd
     */
    void syncDepositBalanceInfo(BalanceInfoSyncQry syncCmd);

    /**
     * 更新余额信息(信息全)
     * 
     * @param dto
     */
    void updateDepositBalance(DepositDistributorDTO dto);

}
