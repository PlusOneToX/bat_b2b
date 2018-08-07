package com.bat.financial.api.refund;

import com.bat.financial.api.refund.dto.data.OrderRefundDTO;

/**
 * @author: lim
 * @description: 退款
 * @date: 2018/7/24 17:07
 */
public interface RefundService {
    void createRefund(OrderRefundDTO orderRefundDTO);

    void updateRefund(OrderRefundDTO orderRefundDTO, Integer refundApplyId);
}
