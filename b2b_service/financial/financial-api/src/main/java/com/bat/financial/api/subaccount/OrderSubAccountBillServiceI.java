package com.bat.financial.api.subaccount;

import com.bat.financial.api.subaccount.dto.OrderSubAccountBillBatchCmd;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillDTO;
import com.bat.financial.api.base.Response;

import java.util.List;

public interface OrderSubAccountBillServiceI {

    /**
     * 根据订单分账id查询分账流水列表
     * @param orderSubAccountId
     * @return
     */
    List<OrderSubAccountBillDTO> listDTOByOrderSubAccountId(Integer orderSubAccountId);

    Response updateActualSubAccountAmount(OrderSubAccountBillBatchCmd billBatchCmd);

    /**
     * 重新分账
     * @param id
     */
    Response subAccountAgain(Integer id);

    /**
     * 线程同步分账
     */
    void wxSubAccountTimer();
}
