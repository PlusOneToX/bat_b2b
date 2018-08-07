package com.bat.financial.pay;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.api.pay.dto.*;
import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.constant.PayWay;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.api.pay.dto.data.QueryRefundDTO;
import com.bat.financial.api.pay.dto.data.QueryTradeRespDTO;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.pay.notify.CreateNotifyReq;
import com.bat.financial.api.pay.notify.CreateNotifyResp;
import com.bat.financial.api.pay.notify.RefundNotifyReq;
import com.bat.financial.api.pay.notify.RefundNotifyResp;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 余额支付 没有支付凭证，支付凭证在充值的时候就已经生成 余额直接扣减
 * @date: 2018/5/27 11:58
 */
@Service("BalancePayServiceImpl")
@Slf4j
public class BalancePayServiceImpl implements PayService {

    @Resource
    private PayCmdExc cmdExc;

    @Resource
    private DepositDetailService depositDetailService;

    @Resource
    private DepositBalanceService depositBalanceService;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateTradeRespDTO createTrade(CreateTradeCmd cmd) {
        if (cmd.getCustomerFlag() == CustomerFlag.IS_NOT_CUSTOMER) {
            if (cmd.getPayerId() == null) {
                throw FinancialException.buildException(ErrorCode.B_BALANCE_PAYER_ID_NULL);
            }
            // 查余额明细 判断同一订单有没有重复支付
            checkPayStatus(cmd);
            // 更新余额
            DepositDistributorDTO dto = updateDepositDistributor(cmd);
            // 增加明细
            createDepositDetail(cmd, dto);
            // 开启子线程需指定租户编码
            String tenantNo = TenantContext.getTenantNo();
            threadPoolTaskScheduler.submit(() -> {
                TenantContext.setTenantNo(tenantNo);
                PayBillsDistributorDO aDo = new PayBillsDistributorDO();
                aDo.setDistributorId(cmd.getPayerId());
                aDo.setDistributorName(cmd.getPayerName());
                aDo.setBusinessId(cmd.getOrderId());
                aDo.setTotalFee(cmd.getAmount());
                aDo.setPayTime(new Date());
                cmdExc.transactionSuccessCallback(aDo);
                TenantContext.removeTenantNo();
            });
        }
        return new CreateTradeRespDTO();
    }

    /**
     * 更新账户余额
     *
     * @param cmd
     * @return
     */
    private DepositDistributorDTO updateDepositDistributor(CreateTradeCmd cmd) {
        DepositDistributorDTO dto = depositBalanceService.getDepositBalanceByDistributorId(cmd.getPayerId());
        log.info("余额支付前余额：{}", dto);
        if (dto.getAccountAvailable().compareTo(cmd.getAmount()) < 0) {
            throw FinancialException.buildException(ErrorCode.B_BALANCE_AVAILABLE_NOT_ENOUGH);
        }
        dto.setAccountAvailable(dto.getAccountAvailable().subtract(cmd.getAmount()));
        dto.setAccountBalance(dto.getAccountBalance().subtract(cmd.getAmount()));
        // 更新余额账户
        log.info("余额支付后余额：{}", dto);
        depositBalanceService.updateDepositBalance(dto);
        return dto;
    }

    /**
     * 增加明细
     * 
     * @param cmd
     * @param dto
     */
    private void createDepositDetail(CreateTradeCmd cmd, DepositDistributorDTO dto) {
        DepositDistributorSubsidiaryBookDTO bookDO = new DepositDistributorSubsidiaryBookDTO();
        bookDO.setDepositDistributorId(dto.getId());
        bookDO.setDistributorId(cmd.getPayerId());
        bookDO.setDistributorName(cmd.getPayerName());
        bookDO.setBusinessType(DepositBusinessType.ORDER_CREATE_SUB);
        bookDO.setPayWay(PayWay.BALANCE);
        bookDO.setBusinessId(cmd.getOrderId());
        bookDO.setChangeType(ChangeType.DECREASE);
        bookDO.setAmount(cmd.getAmount());
        bookDO.setBeforeDepositAmount(dto.getAccountBalance().add(cmd.getAmount()));
        bookDO.setAfterDepositAmount(dto.getAccountBalance());
        bookDO.setRemark("余额支付");
        bookDO.setCreateTime(new Date());
        depositDetailService.createDepositDetail(bookDO);
    }

    /**
     * 检查支付状态
     * 
     * @param cmd
     */
    private void checkPayStatus(CreateTradeCmd cmd) {
        DepositDetailAdminQry qry = new DepositDetailAdminQry();
        qry.setDistributorId(cmd.getPayerId());
        qry.setBusinessType(DepositBusinessType.ORDER_CREATE_SUB + "");
        qry.setBusinessId(cmd.getOrderId().split(",")[0]);
        qry.setChangeType(ChangeType.INCREASE);
        qry.setPayWay(PayWay.BALANCE);
        qry.setPage(1);
        qry.setSize(10);
        PageInfo<DepositDistributorSubsidiaryBookDTO> pageInfo = depositDetailService.listDepositDetail(qry);
        List<DepositDistributorSubsidiaryBookDTO> depositDetail = pageInfo.getList();
        if (!CollectionUtils.isEmpty(depositDetail)) {
            throw FinancialException.buildException(ErrorCode.B_TRADE_HAS_BEEN_PAID);
        }
    }

    @Override
    public QueryTradeRespDTO queryTrade(QueryTradeQry cmd) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);

    }

    @Override
    public boolean closeTrade(CloseTradeCmd cmd) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public RefundTradeRespDTO refundTrade(RefundTradeCmd cmd) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);

    }

    @Override
    public QueryRefundDTO queryRefund(QueryRefundCmd cmd) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public CreateNotifyResp createTradeNotify(CreateNotifyReq notify) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }

    @Override
    public RefundNotifyResp refundTradeNotify(RefundNotifyReq req) {
        throw FinancialException.buildException(ErrorCode.B_NOT_SUPPORT);
    }
}
