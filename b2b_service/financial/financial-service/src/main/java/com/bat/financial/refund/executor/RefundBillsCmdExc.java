package com.bat.financial.refund.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.dao.refund.RefundBillsCustomerMapper;
import com.bat.financial.dao.refund.RefundBillsDistributorMapper;
import com.bat.financial.dao.refund.RefundCustomerApplyMapper;
import com.bat.financial.dao.refund.RefundDistributorApplyMapper;
import com.bat.financial.dao.refund.dataobject.*;
import com.bat.financial.refund.constant.ReceiverType;
import com.bat.financial.refund.constant.RefundDsitributorStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.refund.dto.RefundQry;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.dao.refund.dataobject.*;
import com.bat.financial.refund.RefundDistributorServiceImpl;
import com.bat.financial.refund.constant.RefundApplyStatus;
import com.bat.financial.refund.constant.RefundBillsStatus;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/26 21:19
 */
@Component
@Slf4j
public class RefundBillsCmdExc {
    @Resource
    private RefundBillsCustomerMapper refundBillsCustomerMapper;

    @Resource
    private RefundBillsDistributorMapper refundBillsDistributorMapper;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    private RefundDistributorServiceImpl refundDistributorService;

    @Resource
    private RefundDistributorApplyMapper refundDistributorApplyMapper;

    @Resource
    private RefundCustomerApplyMapper refundCustomerApplyMapper;

    @Resource
    private VoucherService voucherService;

    public void createRefundBills(RefundBillsBaseDO billsBaseDO, Short receiverType, Integer id, String name) {
        Date date = new Date();
        if (receiverType.equals(ReceiverType.DISTRIBUTOR)) {
            RefundBillsDistributorDO aDo = new RefundBillsDistributorDO();
            BeanUtils.copyProperties(billsBaseDO, aDo);
            aDo.setDistributorId(id);
            aDo.setDistributorName(name);
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            log.info("refundBillsDistributorMapper: {}", aDo);
            refundBillsDistributorMapper.insert(aDo);
        } else if (receiverType.equals(ReceiverType.CUSTOMER)) {
            RefundBillsCustomerDO aDo = new RefundBillsCustomerDO();
            BeanUtils.copyProperties(billsBaseDO, aDo);
            aDo.setCustomerId(id);
            aDo.setCustomerName(name);
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            log.info("refundBillsCustomerMapper: {}", aDo);
            refundBillsCustomerMapper.insert(aDo);
        }
    }

    /**
     * 通过退款凭证号 更新为已退款
     *
     * @param refundNo
     * @param refundStatus
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRefundBills(String refundNo, Short refundStatus) {
        boolean updateFlag = false;
        String outTradeNo = null;
        RefundBillsDistributorDO byOutRefundNo = refundBillsDistributorMapper.getByOutRefundNo(refundNo);
        if (byOutRefundNo != null && byOutRefundNo.getRefundStatus().equals(RefundBillsStatus.PROCESS)) {
            log.info("refundBillsDistributor 更新：{},{}", refundNo, refundStatus);
            byOutRefundNo.setRefundStatus(refundStatus);
            byOutRefundNo.setUpdateTime(new Date());
            refundBillsDistributorMapper.updateByPrimaryKey(byOutRefundNo);
            // 微信原路返还时 是异步的 退款申请单 为待确认状态 需要更改为确认状态
            // refundDistributorApplyMapper
            RefundDistributorApplyDO likeRemark =
                refundDistributorApplyMapper.getLikeRemark(byOutRefundNo.getOutRefundNo());
            if (likeRemark != null) {
                log.info("微信回调 修改支付凭证");
                if (likeRemark.getApplyStatus().equals(RefundApplyStatus.UNTREATED)) {
                    likeRemark.setApplyStatus(RefundApplyStatus.PROCESSED);
                    likeRemark.setRemark(likeRemark.getRemark().replace(byOutRefundNo.getOutRefundNo() + "-", ""));
                    refundDistributorApplyMapper.updateByPrimaryKey(likeRemark);
                }
            }
            outTradeNo = byOutRefundNo.getOutTradeNo();
            updateFlag = true;
        }
        RefundBillsCustomerDO byOutRefundNo1 = refundBillsCustomerMapper.getByOutRefundNo(refundNo);
        if (byOutRefundNo1 != null && byOutRefundNo1.getRefundStatus().equals(RefundBillsStatus.PROCESS)) {
            log.info("refundBillsCustomer 更新：{},{}", refundNo, refundStatus);
            byOutRefundNo1.setRefundStatus(refundStatus);
            byOutRefundNo1.setUpdateTime(new Date());
            refundBillsCustomerMapper.updateByPrimaryKey(byOutRefundNo1);
            RefundCustomerApplyDO likeRemark = refundCustomerApplyMapper.getLikeRemark(byOutRefundNo1.getOutRefundNo());
            if (likeRemark != null) {
                log.info("微信回调 修改支付凭证");
                if (likeRemark.getApplyStatus().equals(RefundApplyStatus.UNTREATED)) {
                    likeRemark.setApplyStatus(RefundApplyStatus.PROCESSED);
                    likeRemark.setRemark(likeRemark.getRemark().replace(byOutRefundNo1.getOutRefundNo() + "-", ""));
                    refundCustomerApplyMapper.updateByPrimaryKey(likeRemark);
                }
            }
            outTradeNo = byOutRefundNo1.getOutTradeNo();
            updateFlag = true;
        }
        log.info("updateFlag:{},outTradeNo:{}", updateFlag, outTradeNo);
        if (updateFlag && outTradeNo != null) {
            VoucherQry qry = new VoucherQry();
            qry.setOutTradeNo(outTradeNo);
            List<VoucherDistributorDTO> vouchers = voucherService.getVoucher(qry);
            if (!CollectionUtils.isEmpty(vouchers)) {
                if (vouchers.size() != 1) {
                    log.info("收款单不唯一");
                }
                VoucherDistributorDTO voucher = vouchers.get(0);
                log.info("获取收款单：{}", voucher);
                // if (voucher != null && StringUtils.isNotBlank(voucher.getVoucherErpNo())) {
                if (voucher != null) {
                    boolean b = updateRefund(refundNo, refundStatus);
                    if (!b) {
                        log.info("更新退款单 状态从未生效转为 待确认 失败，手动回滚");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    }
                    return b;
                }
            }
            log.info("没有收款单 不用生成退款单 不用同步收款单：{},{}", refundNo, refundStatus);
        }
        // 也有可能回调太快 行数据还没有生成 等重试就行
        log.info("没有查询到退款凭证 或退款凭证不处于退款处理中状态：{},{}", refundNo, refundStatus);
        return false;
    }

    /**
     * 微信收到退款回调通知更新收款单状态 从无效变为待确认
     *
     * 然后进行退款单同步 同步成功后从待确认 变为已确认
     *
     * @param refundNo
     * @param refundStatus
     * @return
     */
    @SneakyThrows
    private boolean updateRefund(String refundNo, Short refundStatus) {
        RefundQry qry = new RefundQry();
        qry.setRefundStatus(RefundDsitributorStatus.INEFFECTIVE);
        qry.setOutRefundNo(refundNo);
        List<RefundDistributorDO> refundDistributorDOS = refundDistributorService.listRefundDO(qry);
        log.info("refundDistributorDOS:{}", JSON.toJSONString(refundDistributorDOS));
        if (refundDistributorDOS != null && refundDistributorDOS.size() == 1) {
            RefundDistributorDO aDo = refundDistributorDOS.get(0);
            // 微信回调 退款成功 收款单从无效变为待确认
            if (refundStatus.equals(RefundDsitributorStatus.SUCCESS)) {
                aDo.setRefundStatus(RefundDsitributorStatus.WAIT_CONFIRM);
            } else {
                // 回调失败 直接收款单取消
                aDo.setRefundStatus(RefundDsitributorStatus.FAIL);
            }
            aDo.setUpdateTime(new Date());
            log.info("refundDistributorService.updateRefund:{}", JSON.toJSONString(aDo));
            refundDistributorService.updateRefund(aDo);
            // 异步同步退款单 出错也不要影响 单据生成，单据太多
            // 开启子线程需指定租户编码
            String tenantNo = TenantContext.getTenantNo();
            threadPoolTaskScheduler.submit(() -> {
                TenantContext.setTenantNo(tenantNo);
                refundDistributorService.syncRefundDistributorToErp(refundNo);
                TenantContext.removeTenantNo();
            });
            return true;
        } else {
            // 还有一种情况 查询的时候，退款生成无效退款单 还没有生成，但这种概率很小
            log.info("没有查询到退款单 或退款单不处于无效状态");
            // Thread.sleep(100);
            // updateRefund(refundNo, refundStatus);
            return false;
        }
    }
}
