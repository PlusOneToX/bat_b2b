package com.bat.financial.refund.executor;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.RefundBillsCustomerMapper;
import com.bat.financial.dao.refund.RefundBillsDistributorMapper;
import com.bat.financial.dao.refund.dataobject.RefundBillsBaseDO;
import org.springframework.stereotype.Component;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/26 21:19
 */
@Component
public class RefundBillsQryExc {
    @Resource
    private RefundBillsCustomerMapper refundBillsCustomerMapper;

    @Resource
    private RefundBillsDistributorMapper refundBillsDistributorMapper;

    public RefundBillsBaseDO getRefundBillsBaseDO(String outRefundNo) {
        RefundBillsBaseDO byOutRefundNo = refundBillsDistributorMapper.getByOutRefundNo(outRefundNo);
        if (byOutRefundNo == null) {
            byOutRefundNo = refundBillsCustomerMapper.getByOutRefundNo(outRefundNo);
        }
        return byOutRefundNo;
    }
}
