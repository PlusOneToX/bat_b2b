package com.bat.financial.api.refund;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.refund.dto.RefundApplyManualConfirmCmd;
import com.bat.financial.api.refund.dto.RefundApplyQry;
import com.bat.financial.api.refund.dto.data.RefundApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundCustomerApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundDistributorApplyDTO;

/**
 * @author: lim
 * @description: 退款申请
 * @date: 2018/6/12 15:46
 */
public interface RefundApplyService {
    PageInfo<RefundDistributorApplyDTO> listDistributorRefundApply(RefundApplyQry qry);

    PageInfo<RefundCustomerApplyDTO> listCustomerRefundApply(RefundApplyQry qry);

    void manualConfirmRefundApply(RefundApplyManualConfirmCmd cmd);

    RefundApplyDTO listRefundApply(RefundApplyQry qry);

    // void createRefundApply(RefundApplyCreateCmd cmd);
    //
    // void updateRefundApply(RefundApplyUpdateCmd cmd);
    //
    // void deleteRefundApply(Integer id);

}
