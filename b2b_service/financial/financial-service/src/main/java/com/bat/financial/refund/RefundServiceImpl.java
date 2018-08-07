package com.bat.financial.refund;

import static com.bat.financial.pay.executor.ErrorCode.B_NOT_SUPPORT;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.dao.pay.dataobject.PayBillsDO;
import com.bat.financial.dao.refund.dataobject.RefundBaseApplyDO;
import com.bat.financial.dao.refund.dataobject.RefundBillsBaseDO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;
import com.bat.financial.deposit.DepositServiceImpl;
import com.bat.financial.deposit.constant.ChangeType;
import com.bat.financial.deposit.constant.DepositBusinessType;
import com.bat.financial.refund.constant.*;
import com.bat.financial.refund.executor.ErrorCode;
import com.bat.financial.refund.executor.RefundApplyCmdExc;
import com.bat.financial.refund.executor.RefundBillsCmdExc;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.order.order.dto.OrderPayStatusCmd;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.common.CommonService;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.pay.PayService;
import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.api.pay.dto.RefundTradeCmd;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.refund.RefundService;
import com.bat.financial.api.refund.dto.data.OrderRefundDTO;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.pay.constant.BillsBusinessType;
import com.bat.financial.pay.constant.CustomerFlag;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.refund.constant.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 退款服务 没有单
 * @date: 2018/7/24 17:06
 */
@Service
@Slf4j
public class RefundServiceImpl implements RefundService {
    @Resource
    private VoucherService voucherService;

    @Resource
    private CommonService commonService;

    @Resource
    private RefundApplyCmdExc refundApplyCmdExc;

    @Resource
    private PayCmdExc payCmdExc;

    @Resource
    private DepositServiceImpl depositService;

    @Resource
    private RefundBillsCmdExc refundBillsCmdExc;

    @Resource
    private RefundDistributorServiceImpl refundDistributorService;

    @DubboReference(check = false, timeout = 30000)
    private OrderServiceRpc orderServiceRpc;

    /**
     * 整个退款逻辑 包含退款申请 退款单 退款凭证等
     * <p>
     * 1 如果原路返还<br/>
     * -1.1 前提必须有支付凭证，根据支付凭证获取商户资料，进行退款<br/>
     * -1.1.1 退款成功 生产退款单<br/>
     * -1.1.1.1 会生成退款凭证<br/>
     * -1.1.2 退款失败 生成申请单，人工处理<br/>
     * <p>
     * 2 退回余额<br/>
     * -2.1 退回余额 100% 成功，与erp数据一致等其他情况不管 不生成退款单
     * <p>
     * 3 其他退回方式<br/>
     * -3.1 线下退款等其他方式 不生成退款单
     * <p>
     * 4混合退款 </br>
     * -4.1暂不支持
     *
     * @param orderRefundDTO
     */
    @Override
    // @Transactional(rollbackFor = Exception.class)
    public void createRefund(OrderRefundDTO orderRefundDTO) {
        try {
            doRefund(orderRefundDTO, null);
        } catch (FinancialException e) {
            if (e.getCode().equals(B_NOT_SUPPORT)) {
                log.error("支付方式不支持退款，消息默认处理成功");
            } else {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新退款（退款单申请手动触发）
     *
     * @param orderRefundDTO
     * @param refundApplyId
     */
    @Override
    public void updateRefund(OrderRefundDTO orderRefundDTO, Integer refundApplyId) {
        doRefund(orderRefundDTO, refundApplyId);
    }

    /**
     * @param orderRefundDTO
     * @param refundApplyId
     */
    private void doRefund(OrderRefundDTO orderRefundDTO, Integer refundApplyId) {
        // {"businessTypes":1,"cashAmount":0.01,"companyName":"九得","distributorId":3952,"distributorName":"九得","operatorId":3952,"operatorName":"??","orderId":941,"outTradeNo":"wxc0ce1420015518072631296","receiverType":1,"refundMode":1,"refundType":1,"remark":"0"}
        // 退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式 (如果不传默认不处理退款申请单，人工处理) 4 混合退款方式
        Short refundType = orderRefundDTO.getRefundType();
        if (refundType == null) {
            createOrUpdateRefundApply(orderRefundDTO, refundApplyId, false, null);
        } else {
            switch (refundType) {
                case RefundType.REFUND_TO_PAYMENT_ACCOUNT:
                    // 在线退钱
                    RefundTradeRespDTO refundTradeRespDTO = onlineRefund(orderRefundDTO);
                    Boolean refundStatus = refundTradeRespDTO.getRefundStatus();
                    // 生成退款申请单
                    // 提一嘴 支付宝 支持立马返回结果也支持异步，微信只支持异步通知。所以此返回值 微信永远为false。微信在异步处理
                    createOrUpdateRefundApply(orderRefundDTO, refundApplyId, refundStatus,
                        refundTradeRespDTO.getOutRefundNo());
                    // 生成退款单
                    createRefundDistributor(orderRefundDTO, refundStatus, refundTradeRespDTO.getOutRefundNo());
                    break;
                case RefundType.REFUND_TO_BALANCE_ACCOUNT:
                    // 直接退回余额
                    boolean flag = refundBalance(orderRefundDTO);
                    // 生成退款申请单
                    createOrUpdateRefundApply(orderRefundDTO, refundApplyId, flag, null);
                    OrderPayStatusCmd cmd = new OrderPayStatusCmd();
                    cmd.setReceiverType(orderRefundDTO.getReceiverType());
                    cmd.setDistributorId(orderRefundDTO.getDistributorId());
                    cmd.setCustomerId(orderRefundDTO.getCustomerId());
                    cmd.setOrderId(orderRefundDTO.getOrderId());
                    cmd.setPayStatus(PayStatus.REFUND.getValue());
                    cmd.setRefundedAmount(orderRefundDTO.getDepositAmount());
                    orderServiceRpc.orderPayStatus(cmd);
                    break;
                case RefundType.REFUND_TO_OTHER_WAY:
                    // 不做任何处理
                    createOrUpdateRefundApply(orderRefundDTO, refundApplyId, true, null);
                    OrderPayStatusCmd cmd1 = new OrderPayStatusCmd();
                    cmd1.setReceiverType(orderRefundDTO.getReceiverType());
                    cmd1.setDistributorId(orderRefundDTO.getDistributorId());
                    cmd1.setCustomerId(orderRefundDTO.getCustomerId());
                    cmd1.setOrderId(orderRefundDTO.getOrderId());
                    cmd1.setPayStatus(PayStatus.REFUND.getValue());
                    BigDecimal sum = BigDecimal.ZERO;
                    if (orderRefundDTO.getCashAmount() != null) {
                        sum = sum.add(orderRefundDTO.getCashAmount());
                    }
                    if (orderRefundDTO.getDepositAmount() != null) {
                        sum = sum.add(orderRefundDTO.getDepositAmount());
                    }
                    sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
                    cmd1.setRefundedAmount(sum);
                    orderServiceRpc.orderPayStatus(cmd1);
                    break;
                case RefundType.REFUND_TO_COMBINE_WAY:
                    // 还不支持
                    break;
                default:
                    createOrUpdateRefundApply(orderRefundDTO, refundApplyId, false, null);
                    break;
            }
        }
    }

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 创建退款单
     *
     * @param orderRefundDTO
     * @param refundStatus
     * @param outRefundNo
     */
    private void createRefundDistributor(OrderRefundDTO orderRefundDTO, boolean refundStatus, String outRefundNo) {
        log.info("创建退款单：{},{},{}", JSON.toJSONString(orderRefundDTO), refundStatus, outRefundNo);
        // 退款成功生成退款单：PS 支付宝会立马返回结果。微信要等待微信回调通知 微信的收款单会处于无效状态
        VoucherQry qry = new VoucherQry();
        qry.setOutTradeNo(orderRefundDTO.getOutTradeNo());
        List<VoucherDistributorDTO> vouchers = voucherService.getVoucher(qry);
        if (CollectionUtils.isEmpty(vouchers)) {
            log.error("收款单不存在，不生成退款单");
            // throw FinancialException.buildException(ErrorCode.B_VOUCHER_NULL);
            return;
        }
        if (vouchers.size() != 1) {
            log.error("收款单不唯一，不生成退款单");
            return;
            // throw
            // FinancialException.buildException(ErrorCode.B_VOUCHER_NOT_UNIQUE);
        }
        VoucherDistributorDTO voucher = vouchers.get(0);
        log.info("voucher:{}", voucher);
        // 有收款单才会生成退款单 不是所有在线支付都会有收款单 所以不是所有在线退款都会有退款单
        if (voucher != null && outRefundNo != null) {
            log.info("支付凭证：{}，有对应的收款单，生成退款单，退款凭证：{}", orderRefundDTO.getOutTradeNo(), outRefundNo);
            RefundDistributorDO aDo = new RefundDistributorDO();
            BeanUtils.copyProperties(orderRefundDTO, aDo);
            // 根据收款单 设置 退款单分销商信息
            aDo.setDistributorId(voucher.getDistributorId());
            aDo.setDistributorName(voucher.getDistributorName());
            aDo.setCompanyName(voucher.getCompanyName());
            aDo.setAmount(orderRefundDTO.getCashAmount());
            aDo.setRefundWay(voucher.getPayWay());
            aDo.setOutRefundNo(outRefundNo);
            if (orderRefundDTO.getReceiverType().equals(ReceiverType.CUSTOMER)) {
                aDo.setCustomerFlag(CustomerFlag.IS_CUSTOMER);
            } else {
                aDo.setCustomerFlag(CustomerFlag.IS_NOT_CUSTOMER);
            }
            aDo.setCurrencyType("CNY");
            aDo.setBusinessType(BillsBusinessType.ORDER);
            aDo.setBusinessId(orderRefundDTO.getOrderId() + "");
            if (refundStatus) {
                // 即便是线上退款成功了。没有同步ERP也是待确认状态
                aDo.setRefundStatus(RefundDsitributorStatus.WAIT_CONFIRM);
            } else {
                // 微信无效状态 收款单
                aDo.setRefundStatus(RefundDsitributorStatus.INEFFECTIVE);
            }
            log.info("createRefund:{}", JSON.toJSONString(aDo));
            refundDistributorService.createRefund(aDo);
            // 异步同步退款单 出错也不要影响 单据生成，单据太多
            if (refundStatus) {
                log.info("在线退款成功 同步退款单");
                // 开启子线程需指定租户编码
                String tenantNo = TenantContext.getTenantNo();
                threadPoolTaskScheduler.submit(() -> {
                    log.info("syncRefundDistributorToErp:{}", outRefundNo);
                    TenantContext.setTenantNo(tenantNo);
                    refundDistributorService.syncRefundDistributorToErp(outRefundNo);
                    TenantContext.removeTenantNo();
                });
            }
        } else {
            log.info("支付凭证：{}，没有对应的收款单 退款凭证：{}", orderRefundDTO.getOutTradeNo(), outRefundNo);
        }
    }

    /**
     * 原路返回 包含返回 以及生成退款凭证
     *
     * @param orderRefundDTO
     * @return
     */
    public RefundTradeRespDTO onlineRefund(OrderRefundDTO orderRefundDTO) {
        log.info("线上退款开始：{}", JSON.toJSONString(orderRefundDTO));
        if (orderRefundDTO.getCashAmount() == null || orderRefundDTO.getCashAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("退款金额不合法：{}", orderRefundDTO.getCashAmount());
            throw FinancialException.buildException(ErrorCode.B_REFUND_AMOUNT_ERROR);
        }
        String outTradeNo = orderRefundDTO.getOutTradeNo();
        RefundTradeRespDTO refundTradeRespDTO = new RefundTradeRespDTO();
        if (StringUtils.isNotBlank(outTradeNo)) {
            PayBillsDO payBillByOutTradeNo = payCmdExc.getPayBillByOutTradeNo(null, outTradeNo);
            log.info("payBillByOutTradeNo:{}", JSON.toJSONString(payBillByOutTradeNo));
            String payMethod = payBillByOutTradeNo.getPayMethod();
            Short tradeMode = payBillByOutTradeNo.getTradeMode();
            Integer payeeId = payBillByOutTradeNo.getPayeeId();
            Integer organizationId = payBillByOutTradeNo.getOrganizationId();
            String payChannel = commonService.getPayChannel(payMethod, tradeMode, payeeId, organizationId);
            PayService payService = commonService.getMap().get(PayChannel.valueOf(payChannel.toUpperCase()));
            RefundTradeCmd cmd = new RefundTradeCmd();
            cmd.setTradeMode(tradeMode);
            cmd.setPayMethod(payMethod);
            cmd.setPayeeId(payeeId);
            cmd.setOrganizationId(organizationId);
            cmd.setOutTradeNo(outTradeNo);
            cmd.setTotalAmount(payBillByOutTradeNo.getTotalFee().doubleValue());
            cmd.setRefundAmount(orderRefundDTO.getCashAmount().doubleValue());
            if (orderRefundDTO.getBusinessTypes().equals(RefundBusinessTypes.ORDER_CANCEL)) {
                cmd.setReason("订单取消退款");
            } else if (orderRefundDTO.getBusinessTypes().equals(RefundBusinessTypes.ORDER_CHANGE)) {
                cmd.setReason("订单变更退款");
            }
            // 在线退款成功
            refundTradeRespDTO = payService.refundTrade(cmd);
            // 生成退款凭证
            log.info("生成退款凭证开始：{}", JSON.toJSONString(refundTradeRespDTO));
            RefundBillsBaseDO refundBillsBaseDO = new RefundBillsBaseDO();
            BeanUtils.copyProperties(orderRefundDTO, refundBillsBaseDO);
            refundBillsBaseDO.setOutRefundNo(refundTradeRespDTO.getOutRefundNo());
            refundBillsBaseDO.setBusinessType(BillsBusinessType.ORDER);
            refundBillsBaseDO.setBusinessId(orderRefundDTO.getOrderId() + "");
            // 此refundType 为退款方式 支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付 不是 退款的refundType
            refundBillsBaseDO.setRefundType(payBillByOutTradeNo.getPayType());
            if (refundTradeRespDTO.getRefundStatus()) {
                refundBillsBaseDO.setRefundStatus(RefundBillsStatus.SUCCESS);
            } else {
                // 不成功有两种情况 一是支付失败 而是还没有回调结果
                refundBillsBaseDO.setRefundStatus(RefundBillsStatus.PROCESS);
            }
            // 退款金额 不是总金额
            refundBillsBaseDO.setTotalFee(orderRefundDTO.getCashAmount());
            refundBillsBaseDO.setOnlineTradeNo(refundTradeRespDTO.getOnlineTradeNo());
            refundBillsBaseDO.setRefundTime(refundTradeRespDTO.getRefundTime());
            Integer id;
            String name;
            if (orderRefundDTO.getReceiverType().equals(ReceiverType.CUSTOMER)) {
                id = orderRefundDTO.getCustomerId();
                name = orderRefundDTO.getCustomerName();
            } else {
                id = orderRefundDTO.getDistributorId();
                name = orderRefundDTO.getDistributorName();
            }
            log.info("createRefundBills params:{},{},{},{}", JSON.toJSONString(refundBillsBaseDO),
                orderRefundDTO.getReceiverType(), id, name);
            refundBillsCmdExc.createRefundBills(refundBillsBaseDO, orderRefundDTO.getReceiverType(), id, name);
        }
        return refundTradeRespDTO;
    }

    /**
     * 退回余额
     *
     * @param orderRefundDTO
     * @return
     */
    private boolean refundBalance(OrderRefundDTO orderRefundDTO) {
        log.info("退回余额开始：{}", JSON.toJSONString(orderRefundDTO));
        // C端用户不支持余额退款
        if (orderRefundDTO.getReceiverType().equals(ReceiverType.CUSTOMER)) {
            throw FinancialException.buildException(ErrorCode.B_BALANCE_CUSTOMER_NOT_SUPPORT);
        }
        if (orderRefundDTO.getDepositAmount() == null
            || orderRefundDTO.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("退款金额不合法：{}", orderRefundDTO.getCashAmount());
            throw FinancialException.buildException(ErrorCode.B_REFUND_AMOUNT_ERROR);
        }
        boolean flag;
        try {
            DepositDistributorSubsidiaryBookDTO bookDTO = new DepositDistributorSubsidiaryBookDTO();
            BeanUtils.copyProperties(orderRefundDTO, bookDTO);
            if (orderRefundDTO.getBusinessTypes().equals(RefundBusinessTypes.ORDER_CANCEL)) {
                bookDTO.setBusinessType(DepositBusinessType.ORDER_CANCEL_ADD);
            } else if (orderRefundDTO.getBusinessTypes().equals(RefundBusinessTypes.ORDER_CHANGE)) {
                bookDTO.setBusinessType(DepositBusinessType.ORDER_ADJUSTMENT);
            }
            bookDTO.setPayWay(null);
            bookDTO.setBusinessId(orderRefundDTO.getOrderId() + "");
            bookDTO.setChangeType(ChangeType.INCREASE);
            bookDTO.setAmount(orderRefundDTO.getDepositAmount());
            log.info("accountBalanceChange:{}", JSON.toJSONString(bookDTO));
            depositService.accountBalanceChange(bookDTO);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 生成退款申请单
     * 
     * @param orderRefundDTO
     * @param flag
     * @param outRefundNo
     */
    private void createOrUpdateRefundApply(OrderRefundDTO orderRefundDTO, Integer refundApplyId, boolean flag,
        String outRefundNo) {
        log.info("生成退款申请单：{}", JSON.toJSONString(orderRefundDTO));
        log.info("线上退款结果：flag:{}", flag);
        // 生成退款申请单
        RefundBaseApplyDO applyDO = new RefundBaseApplyDO();
        BeanUtils.copyProperties(orderRefundDTO, applyDO);
        applyDO.setBusinessId(orderRefundDTO.getOrderId());
        if (applyDO.getCashAmount() == null) {
            applyDO.setCashAmount(BigDecimal.ZERO);
        }
        if (applyDO.getDepositAmount() == null) {
            applyDO.setDepositAmount(BigDecimal.ZERO);
        }
        applyDO.setAmount(applyDO.getCashAmount().add(applyDO.getDepositAmount()));
        if (flag) {
            applyDO.setApplyStatus(RefundApplyStatus.PROCESSED);
        } else {
            applyDO.setApplyStatus(RefundApplyStatus.UNTREATED);
        }
        // 有可能没有值 这种情况 写个默认值(admin)
        if (orderRefundDTO.getOperatorId() == null) {
            applyDO.setOperatorId(10000);
            applyDO.setOperatorName("admin");
        }
        Integer id = null;
        String name = null;
        if (orderRefundDTO.getReceiverType().equals(ReceiverType.DISTRIBUTOR)) {
            id = orderRefundDTO.getDistributorId();
            name = orderRefundDTO.getDistributorName();
        } else if (orderRefundDTO.getReceiverType().equals(ReceiverType.CUSTOMER)) {
            id = orderRefundDTO.getCustomerId();
            name = orderRefundDTO.getDistributorName();
        }
        // 新增收款单时 把退款凭证塞到备注中，微信回调时用，知道方法很烂
        if (refundApplyId == null) {
            applyDO.setRemark(outRefundNo + "-" + applyDO.getRemark());
        }
        log.info("createOrUpdateRefundApply:{},{},{},{},{}", JSON.toJSONString(applyDO),
            orderRefundDTO.getReceiverType(), id, name, refundApplyId);
        refundApplyCmdExc.createOrUpdateRefundApply(applyDO, orderRefundDTO.getReceiverType(), id, name, refundApplyId);
    }
}
