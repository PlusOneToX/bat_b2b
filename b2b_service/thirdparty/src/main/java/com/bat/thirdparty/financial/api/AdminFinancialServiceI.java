package com.bat.thirdparty.financial.api;

import java.util.List;

import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/17 19:56
 */
public interface AdminFinancialServiceI {
    /**
     * 同步收款单给ERP
     * 
     * @param ids
     * @return
     */
    Response syncVouchersToERP(List<Integer> ids);

    /**
     * 同步退款单
     * 
     * @param dto
     */
    Response syncRefundBillToErp(RefundBillSyncDTO dto);

}
