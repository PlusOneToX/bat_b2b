package com.bat.financial.api.refund;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.refund.dto.RefundQry;
import com.bat.financial.api.refund.dto.data.RefundDistributorDTO;

/**
 * @author: lim
 * @description: 退款单
 * @date: 2018/6/12 15:46
 */
public interface RefundDistributorService {
    RefundDistributorDTO getRefundBy(Integer id);

    PageInfo<RefundDistributorDTO> listRefund(RefundQry qry);

    void syncRefundDistributorToErp(String outRefundNo);

    // void createRefund(RefundCreateCmd cmd);
    //
    // void updateRefund(RefundUpdateCmd cmd);
    //
    // void deleteRefund(Integer id);
}
