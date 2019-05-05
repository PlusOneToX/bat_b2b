package com.bat.dubboapi.financial.refund.api;

import com.bat.dubboapi.financial.refund.dto.data.ErpRefundBillDetailsDTO;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.financial.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface FinancialRefundServiceRpc {

    /**
     * 获取组装的erp退款单
     *
     * @param refundBillId
     * @return
     */
    Response<RefundBillSyncDTO> listErpRefundBillById(Integer refundBillId);

    /**
     * 更新退款单
     * @param detailsDTO
     * @return
     */
    Response updateRefund(ErpRefundBillDetailsDTO detailsDTO);

}
