package com.bat.financial.deposit;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.constant.DepositBusinessType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.ReceiptService;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.ReceiptPayBillsQry;
import com.bat.financial.api.deposit.dto.ReceiptQry;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.deposit.dto.data.ReceiptDTO;
import com.bat.financial.api.pay.dto.data.PayBillsDTO;
import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.PayStatus;
import com.bat.financial.pay.executor.PayCmdExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 19:44
 */
@Service
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {

    // @Resource
    // private ReceiptCmdExc receiptCmdExc;
    //
    // @Resource
    // private ReceiptQryExc receiptQryExc;

    @Resource
    private DepositDetailService depositDetailService;

    @Resource
    private PayCmdExc cmdExc;

    @Override
    public PageInfo<ReceiptDTO> listReceipt(ReceiptQry qry) {
        DepositDetailAdminQry qry1 = new DepositDetailAdminQry();
        BeanUtils.copyProperties(qry, qry1);
        qry1.setChangeType(ChangeType.INCREASE);
        // 1 充值 8 分销佣金（订单收款）
        if (StringUtils.isBlank(qry.getBusinessType())) {
            qry1.setBusinessType("1,8");
        }
        PageInfo pageInfo = depositDetailService.listDepositDetail(qry1);
        List<DepositDistributorSubsidiaryBookDTO> list = pageInfo.getList();
        List<ReceiptDTO> collect = list.stream().map(bookDTO -> {
            ReceiptDTO receiptDTO = new ReceiptDTO();
            receiptDTO.setBookDTO(bookDTO);
            // 充值才有凭证
            if (bookDTO.getBusinessType().equals(DepositBusinessType.RECHARGE)) {
                ReceiptPayBillsQry qry2 = new ReceiptPayBillsQry();
                qry2.setBusinessType(BillsBusinessType.RECHARGE);
                qry2.setPayStatus(PayStatus.COMPLETE_PAYMENT);
                qry2.setTotalFee(bookDTO.getAmount());
                qry2.setPayType(bookDTO.getPayWay());
                qry2.setDistributorId(bookDTO.getDistributorId());
                qry2.setPayTime(bookDTO.getCreateTime());
                List<PayBillsDistributorDO> payBillsDOS = cmdExc.listPayBillsDistributorByParams(qry2);
                if (!CollectionUtils.isEmpty(payBillsDOS)) {
                    if (payBillsDOS.size() == 1) {
                        PayBillsDTO payBillsDTO = new PayBillsDTO();
                        PayBillsDO payBillsDO = payBillsDOS.get(0);
                        BeanUtils.copyProperties(payBillsDO, payBillsDTO);
                        receiptDTO.setPayBillsDTO(payBillsDTO);
                        return receiptDTO;
                    } else {
                        log.info("余额充值变动 与 凭证对不上");
                    }
                } else {
                    log.info("余额充值变动 与 没有凭证对应");
                }
            }
            return receiptDTO;
        }).collect(Collectors.toList());
        pageInfo.setList(collect);
        return pageInfo;
    }
}
