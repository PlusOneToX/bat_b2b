package com.bat.financial.api.deposit;

import com.bat.financial.api.deposit.dto.DepositReChargeQry;
import com.bat.financial.api.deposit.dto.data.PayCallBackDTO;
import com.bat.financial.api.deposit.dto.DepositReChargeCreateCmd;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:39
 */
public interface RechargeService {

    /**
     * 创建充值
     * 
     * @param cmd
     * @return
     */
    CreateTradeRespDTO createRecharge(DepositReChargeCreateCmd cmd);

    /**
     * 更新充值（消息队列回调通知）
     *
     * @param payCallBackDTO
     * @return
     */
    boolean updateRecharge(PayCallBackDTO payCallBackDTO);

    /**
     * 查询充值状态
     * 
     * @param qry
     * @return
     */
    Boolean queryRechargeStatus(DepositReChargeQry qry);

}
