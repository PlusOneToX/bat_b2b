package com.bat.financial.refund;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;
import com.bat.financial.message.MessageSendService;
import com.bat.financial.refund.executor.RefundDistributorCmdExc;
import com.bat.financial.refund.executor.RefundDistributorQryExc;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.financial.api.refund.RefundDistributorService;
import com.bat.financial.api.refund.dto.RefundQry;
import com.bat.financial.api.refund.dto.data.RefundDistributorDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 退款“单” 服务
 * @date: 2018/6/12 15:47
 */
@Service
@Slf4j
public class RefundDistributorServiceImpl implements RefundDistributorService {

    @Resource
    private RefundDistributorQryExc refundQryExc;

    @Resource
    private RefundDistributorCmdExc refundCmdExc;

    @Resource
    private MessageSendService sendService;

    @Override
    public RefundDistributorDTO getRefundBy(Integer id) {
        return refundQryExc.getRefundById(id);
    }

    @Override
    public PageInfo<RefundDistributorDTO> listRefund(RefundQry qry) {
        return refundQryExc.listRefund(qry);
    }

    /**
     * 有两处来源 自动调用
     * 
     * 1 RefundServiceImpl 支付宝支付直接一路下来
     * 
     * 2 RefundBillsCmdExc 微信支付 回调修改凭证 修改退款单
     * 
     * @param outRefundNo
     */
    @Override
    public void syncRefundDistributorToErp(String outRefundNo) {
        RefundBillSyncDTO dto = refundQryExc.getRefundDistributor(outRefundNo);
        if (dto != null) {
            log.info("syncRefundDistributorToErp success：{}", dto);
            boolean b = sendService.syncRefundBillToErp(dto, outRefundNo);
            if (b) {
                log.info("发送同步退款单消息成功");
            } else {
                log.info("发送同步退款单消息失败");
            }
        }
    }

    public void createRefund(RefundDistributorDO aDo) {
        refundCmdExc.createRefund(aDo);
    }

    public void updateRefund(RefundDistributorDO cmd) {
        refundCmdExc.updateRefund(cmd);
    }

    public List<RefundDistributorDO> listRefundDO(RefundQry qry) {
        return refundQryExc.listRefundDO(qry);
    }

    //
    // @Override
    // public void deleteRefund(Integer id) {
    // refundCmdExc.deleteRefund(id);
    // }
}
