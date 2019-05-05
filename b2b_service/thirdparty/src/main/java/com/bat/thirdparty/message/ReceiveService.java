package com.bat.thirdparty.message;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.message.api.Sink;
import com.bat.thirdparty.mongodb.MongoDbContext;
import com.bat.thirdparty.mongodb.dao.*;
import com.bat.thirdparty.mongodb.dao.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNewOrderMsgDTO;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.VoucherDistributorRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyOutboundSyncErpCmd;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.DiyPurchaseOrderDTO;
import com.bat.dubboapi.thirdparty.erp.dto.order.purchase.ErpPurchaseOrderOutboundCmd;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ThirdPartyConfig;
import com.bat.thirdparty.coupon.api.SumsungCouponService;
import com.bat.thirdparty.erp.service.executor.DistributorExe;
import com.bat.thirdparty.financial.api.AdminFinancialServiceI;
import com.bat.thirdparty.mongodb.dao.*;
import com.bat.thirdparty.mongodb.dao.dataobject.*;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.service.executor.WechatProgramMsgExe;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.service.executor.OrderServiceCmdExe;
import com.bat.thirdparty.tenant.TenantContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/4 13:54
 */
@Service
@Slf4j
public class ReceiveService {

    @Resource
    private AdminFinancialServiceI adminFinancialServiceI;

    @Resource
    private OrderLogDao orderLogDao;

    @Resource
    private CommonLogDao commonLogDao;

    @Resource
    private OrderDeliverBillLogDao orderDeliverBillLogDao;

    @Resource
    private VoucherLogDao voucherLogDao;

    @Resource
    private RefundLogDao refundLogDao;

    @Resource
    private DistributorLogDao distributorLogDao;

    @Resource
    private DistributorExe distributorExe;

    @DubboReference(check = false, timeout = 10000)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    @Resource
    private WithdrawApplyLogDao withdrawApplyLogDao;

    @Resource
    private OrderServiceCmdExe orderServiceCmdExe;

    @Resource
    private SumsungCouponService sumsungCouponService;

    @Resource
    private OrderOpenServiceI orderOpenServiceI;

    @Resource
    private ThirdPartyConfig config;

    @Resource
    private MongoDbContext mongoDbContext;

    @Resource
    private WechatProgramMsgExe wechatProgramMsgExe;

    /**
     * 订单收款单 同步erporderVoucherSyncErp(旧)
     * 
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderVoucherSyncErp'")
    public void syncOrderVouchersToERPOld(@Headers Map headers, @Payload Integer id) {
        log.info("{} receive: {}", Sink.THIRDPARTY_ORDER_INPUT, JSON.toJSONString(id));
        // 此id为订单id
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        Response<VoucherDistributorRpcDTO> dtoResponse = financialVoucherServiceRpc.listVouchersByBusinessId(id);
        if (dtoResponse.isSuccess()) {
            adminFinancialServiceI.syncVouchersToERP(Collections.singletonList(dtoResponse.getData().getId()));
        }
        TenantContext.removeTenantNo();
    }

    /**
     * 订单收款单 同步erporderVoucherSyncErp(新)
     *
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_ERP_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderVoucherSyncErp'")
    public void syncOrderVouchersToERPNew(@Headers Map headers, @Payload Integer id) {
        log.info("{} receive: {}", Sink.THIRDPARTY_ORDER_ERP_INPUT, JSON.toJSONString(id));
        // 此id为订单id
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        Response<VoucherDistributorRpcDTO> dtoResponse = financialVoucherServiceRpc.listVouchersByBusinessId(id);
        if (dtoResponse.isSuccess()) {
            adminFinancialServiceI.syncVouchersToERP(Collections.singletonList(dtoResponse.getData().getId()));
        }
        TenantContext.removeTenantNo();
    }

    /**
     * 充值收款单 同步ERP
     * 
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'rechargeVoucherSyncErp'")
    public void syncRechargeVouchersToERP(@Headers Map headers, @Payload Integer id) {
        log.info("{} receive: {}", Sink.THIRDPARTY_FINANCIAL_INPUT, JSON.toJSONString(id));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        adminFinancialServiceI.syncVouchersToERP(Collections.singletonList(id));
        TenantContext.removeTenantNo();
    }

    /**
     * 退款单 同步ERP
     *
     * @param dto
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncRefundBill'")
    public void syncRefundBillToErp(@Headers Map headers, @Payload RefundBillSyncDTO dto) {
        log.info("{} receive: {}", Sink.THIRDPARTY_FINANCIAL_INPUT, JSON.toJSONString(dto));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        Response response = adminFinancialServiceI.syncRefundBillToErp(dto);
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        TenantContext.removeTenantNo();
    }

    /**
     * 创建订单日志
     * 
     * @param orderLog
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderLog'")
    public void orderLog(@Headers Map headers, @Payload OrderLogDO orderLog) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        orderLogDao.save(orderLog);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void commonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 订单同步到（旧）
     * 
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderToSumsung'")
    public void orderToSumsungOld(@Headers Map headers, @Payload Integer orderId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        try {
            orderOpenServiceI.sumsungOrder(orderId);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {
            TenantContext.removeTenantNo();
            log.error("订单同步到出现异常:{}", orderId);
            e.printStackTrace();
            return;
        }
    }

    /**
     * 订单同步到（新）
     *
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_CUSTOMER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderToSumsung'")
    public void orderToSumsungNew(@Headers Map headers, @Payload Integer orderId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        try {
            orderOpenServiceI.sumsungOrder(orderId);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {
            TenantContext.removeTenantNo();
            log.error("订单同步到出现异常:{}", orderId);
            e.printStackTrace();
            return;
        }
    }

    /**
     * 优惠卷同步到
     * 
     * @param userIds
     */
    @StreamListener(value = Sink.THIRDPARTY_PROMOTION_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'couponToSumsung'")
    public void couponToSumsung(@Headers Map headers, @Payload String userIds) throws Exception {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        sumsungCouponService.syn(userIds);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void financialCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_DISTRIBUTOR_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void distributorCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_FLEXIBLE_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void flexibleCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void goodsCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_PROMOTION_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void promotionCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SYSTEM_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void systemCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void selfCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建通用操作日志
     *
     * @param commonLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_WAREHOUSE_INPUT, condition = "headers['rocketmq_TAGS'] == 'commonLog'")
    public void warehouseCommonLog(@Headers Map headers, @Payload CommonLogDO commonLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        commonLogDao.save(commonLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建发货单日志
     *
     * @param orderDeliverBillLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderDeliverBillLog'")
    public void selfOrderDeliverBillLog(@Headers Map headers, @Payload OrderDeliverBillLogDO orderDeliverBillLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        orderDeliverBillLogDao.save(orderDeliverBillLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建发货单日志
     *
     * @param orderDeliverBillLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderDeliverBillLog'")
    public void orderDeliverBillLog(@Headers Map headers, @Payload OrderDeliverBillLogDO orderDeliverBillLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        orderDeliverBillLogDao.save(orderDeliverBillLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建收款单日志
     *
     * @param voucherLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'voucherLog'")
    public void selfVoucherLog(@Headers Map headers, @Payload VoucherLogDO voucherLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        voucherLogDao.save(voucherLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建收款单日志
     *
     * @param voucherLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'voucherLog'")
    public void financialVoucherLog(@Headers Map headers, @Payload VoucherLogDO voucherLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        voucherLogDao.save(voucherLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建退款单日志
     *
     * @param refundLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'refundLog'")
    public void selfRefundLog(@Headers Map headers, @Payload RefundLogDO refundLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        refundLogDao.save(refundLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建退款单日志
     *
     * @param refundLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT, condition = "headers['rocketmq_TAGS'] == 'refundLog'")
    public void financialRefundLog(@Headers Map headers, @Payload RefundLogDO refundLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        refundLogDao.save(refundLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 分销客户提现申请日志
     * 
     * @param withdrawApplyLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_FINANCIAL_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'withdrawApplyLog'")
    public void financialWithdrawApplyLog(@Headers Map headers, @Payload WithdrawApplyLogDO withdrawApplyLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        withdrawApplyLogDao.save(withdrawApplyLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建分销商日志
     *
     * @param distributorLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'distributorLog'")
    public void distributorVoucherLog(@Headers Map headers, @Payload DistributorLogDO distributorLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        distributorLogDao.save(distributorLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 分销商信息同步erp消息
     *
     * @param distributorId
     */
    @StreamListener(value = Sink.THIRDPARTY_DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'distributorSyncErp'")
    public void distributorSyncErp(@Headers Map headers, @Payload Integer distributorId) throws Exception {
        // 同步erp
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        distributorExe.syncDistributorToERP(distributorId);
        TenantContext.removeTenantNo();
    }

    /**
     * 创建订单日志
     *
     * @param orderLogDO
     */
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderLog'")
    public void selfOrderLog(@Headers Map headers, @Payload OrderLogDO orderLogDO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        mongoDbContext.getMongoDbFactory();
        orderLogDao.save(orderLogDO);
        TenantContext.removeTenantNo();
    }

    /**
     * 推送销售采购单到ERP(柔性)（合并为下面的方法syncErpPurchaseAndOutboundOrder）
     *
     * @param diyPurchaseOrderDTO
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncErpPurchase'")
    public void syncErpPurchase(@Headers Map headers, @Payload DiyPurchaseOrderDTO diyPurchaseOrderDTO) {
        log.info("消费 推送采购销售单到第三方服务、同步ERP,{}", JSON.toJSONString(diyPurchaseOrderDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderServiceCmdExe.syncErpPurchase(diyPurchaseOrderDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 推送出库单到ERP(柔性)（合并为下面的方法syncErpPurchaseAndOutboundOrder）
     *
     * @param diyOutboundSyncErpCmd
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncOutStockToERP'")
    public void syncOutStockToERP(@Headers Map headers, @Payload DiyOutboundSyncErpCmd diyOutboundSyncErpCmd) {
        log.info("消费 推送出库单到第三方服务、同步ERP,{}", JSON.toJSONString(diyOutboundSyncErpCmd));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        orderServiceCmdExe.syncOutStockToERP(diyOutboundSyncErpCmd);
        TenantContext.removeTenantNo();
    }

    /**
     * 柔性订单出库单同步消息（旧）
     * 
     * @param headers
     * @param erpPurchaseOrderOutboundCmd
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'syncErpPurchaseAndOutboundOrder'")
    public void syncErpPurchaseAndOutboundOrderOld(@Headers Map headers,
        @Payload ErpPurchaseOrderOutboundCmd erpPurchaseOrderOutboundCmd) {
        try {
            log.info("消费 推送采购销售单和出库单到第三方服务、同步ERP,{}", JSON.toJSONString(erpPurchaseOrderOutboundCmd));
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.syncErpPurchaseAndOutboundOrder(tenantNo, erpPurchaseOrderOutboundCmd);
            TenantContext.removeTenantNo();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("消费 推送采购销售单和出库单到第三方服务、同步ERP失败,{}", e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * 柔性订单出库单同步消息（新）
     * 
     * @param headers
     * @param erpPurchaseOrderOutboundCmd
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_ERP_PURCHASE_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'syncErpPurchaseAndOutboundOrder'")
    public void syncErpPurchaseAndOutboundOrderNew(@Headers Map headers,
        @Payload ErpPurchaseOrderOutboundCmd erpPurchaseOrderOutboundCmd) {
        try {
            log.info("消费 推送采购销售单和出库单到第三方服务、同步ERP,{}", JSON.toJSONString(erpPurchaseOrderOutboundCmd));
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.syncErpPurchaseAndOutboundOrder(tenantNo, erpPurchaseOrderOutboundCmd);
            TenantContext.removeTenantNo();
        } catch (Exception e) {
            log.info("消费 推送采购销售单和出库单到第三方服务、同步ERP失败,{}", e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * 推送订单到工厂（旧）
     *
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderAsynFactory'")
    public void orderAsynFactoryOld(@Headers Map headers, @Payload Integer orderId) {
        log.info("消费 推送订单到工厂,{}", orderId);
        try {
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.orderAsynFactory(orderId);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {// 同步工厂失败(已知bug)，需工厂处理失败订单再手工触发同步工厂,{"msg":"商家单号重复","result":"","code":"24002","success":false}
            log.info("同步工厂消息失败：,{}", orderId);
            e.printStackTrace();
            return;
        }
    }

    /**
     * 推送订单到工厂（新）
     *
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_FACTORY_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderAsynFactory'")
    public void orderAsynFactoryNew(@Headers Map headers, @Payload Integer orderId) {
        log.info("消费 推送订单到工厂,{}", orderId);
        try {
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.orderAsynFactory(orderId);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {// 同步工厂失败(已知bug)，需工厂处理失败订单再手工触发同步工厂,{"msg":"商家单号重复","result":"","code":"24002","success":false}
            log.info("同步工厂消息失败：,{}", orderId);
            e.printStackTrace();
            return;
        }
    }

    /**
     * 同步物流信息到第三方系统(柔性，旧)
     *
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderLogistictToThird'")
    public void orderLogistictToThirdOld(@Headers Map headers, @Payload Integer id) {
        try {
            log.info("消费 同步物流信息到第三方系统,{}", id);
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.orderLogistictToThird(id);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {
            TenantContext.removeTenantNo();
            e.printStackTrace();
            log.error("消费 同步物流信息到第三方系统失败,{}", id + e.getMsg());
        }
    }

    /**
     * 同步物流信息到第三方系统(柔性，新)
     *
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_CUSTOMER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderLogistictToThird'")
    public void orderLogistictToThirdNew(@Headers Map headers, @Payload Integer id) {
        try {
            log.info("消费 同步物流信息到第三方系统,{}", id);
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.orderLogistictToThird(id);
            TenantContext.removeTenantNo();
        } catch (Exception e) {
            TenantContext.removeTenantNo();
            e.printStackTrace();
            log.error("消费 同步物流信息到第三方系统失败,{}", id);
            e.printStackTrace();
        }

    }

    /**
     * 订单审核通过发送消息
     * 
     * @param headers
     * @param orderId
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_SUB_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderConfirmMsg'")
    public void orderConfirmMsg(@Headers Map headers, @Payload Integer orderId) {
        try {
            log.info("接收到订单审核通知:{}", orderId);
            String tenantNo = (String) headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            wechatProgramMsgExe.sendFromOrder(orderId, null, MsgCenterConstant.MSG_TYPE_STATUS);
            TenantContext.removeTenantNo();
        }catch (Exception e){
            log.error("订单审核出现异常:{}", e);
        }
    }

    /**
     * 订单订单发货通知
     * 
     * @param headers
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_SUB_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderDeliverMsg'")
    public void orderDeliverMsg(@Headers Map headers, @Payload Integer orderDeliverBillId) {
        try {
            //停顿两秒后处理
            Thread.sleep(2000);
            log.info("开始接收到订单发货通知:{}",orderDeliverBillId);
            String tenantNo = (String) headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            wechatProgramMsgExe.sendFromOrder(null, orderDeliverBillId, MsgCenterConstant.MSG_TYPE_DELIVERY);
            TenantContext.removeTenantNo();
        }catch (Exception e){
            log.info("订单发货通知出现异常:{}",e);
        }
    }

/*    *//**
     * 订单订单发货通知
     * 
     * @param headers
     *//*
    @StreamListener(value = Sink.THIRDPARTY_SELF_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderDeliverMsg'")
    public void selfOrderDeliverMsg(@Headers Map headers, @Payload Integer orderDeliverBillId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        wechatProgramMsgExe.sendFromOrder(null, orderDeliverBillId, MsgCenterConstant.MSG_TYPE_DELIVERY);
        TenantContext.removeTenantNo();
    }*/

    /**
     * 订单未支付提醒
     * 
     * @param headers
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_SUB_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderUnpaidMsg'")
    public void orderUnpaidMsg(@Headers Map headers, @Payload Integer orderId) {
        try {
            log.info("接收订单未支付消息:{}",orderId);
            String tenantNo = (String) headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            wechatProgramMsgExe.sendFromOrder(orderId, null, MsgCenterConstant.MSG_TYPE_UNPAID);
            TenantContext.removeTenantNo();
        }catch (Exception e){
            log.info("未支付消息处理失败:{}",e);
        }
    }

    /**
     * 分销商审核通知
     *
     * @param distributorId
     */
    @StreamListener(value = Sink.THIRDPARTY_DISTRIBUTOR_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'distributorExamineMsg'")
    public void distributorExamineMsg(@Headers Map headers, @Payload Integer distributorId) {
        try {
            log.info("接收分销商审核信息:{}",distributorId);
            String tenantNo = (String) headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            wechatProgramMsgExe.sendFromDistributor(null, distributorId, MsgCenterConstant.MSG_TYPE_EXAMINE);
            TenantContext.removeTenantNo();
        }catch (Exception e){
            log.info("分销商审核信息处理失败:{}",e);
        }
    }

    /**
     * 新订单提醒
     * 
     * @param headers
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_SUB_INPUT, condition = "headers['rocketmq_TAGS'] == 'newOrderMsg'")
    public void newOrderMsg(@Headers Map headers, @Payload DistributorNewOrderMsgDTO dto) {
        try {
            log.info("新订单提醒消息:{}", JSONObject.toJSONString(dto));
            String tenantNo = (String) headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            wechatProgramMsgExe.sendFromDistributor(dto.getOrderId(), dto.getDistributorId(),
                    MsgCenterConstant.MSG_TYPE_NEW);
            TenantContext.removeTenantNo();
        }catch (Exception e){
            log.info("新订单提醒消息处理异常:{}", e);
        }
    }

    /**
     * 取消订单同步到第三方（柔性，旧）
     *
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderCancelToThird'")
    public void orderCancelToThirdOld(@Headers Map headers, @Payload Integer id) {
        try {
            log.info("消费同步取消订单到第三方系统,{}", id);
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.pushCancelOrderNoToThird(id);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {
            log.error("消费同步取消订单到第三方系统,{}", id + e.getMsg());
        }
    }

    /**
     * 取消订单同步到第三方（柔性，新）
     *
     * @param id
     */
    @StreamListener(value = Sink.THIRDPARTY_ORDER_CUSTOMER_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'orderCancelToThird'")
    public void orderCancelToThirdNew(@Headers Map headers, @Payload Integer id) {
        try {
            log.info("消费同步取消订单到第三方系统,{}", id);
            String tenantNo = (String)headers.get("tenantNo");
            if (StringUtils.isBlank(tenantNo)) {
                tenantNo = config.getDefaultTenantNo();
            }
            TenantContext.setTenantNo(tenantNo);
            orderServiceCmdExe.pushCancelOrderNoToThird(id);
            TenantContext.removeTenantNo();
        } catch (ThirdPartyException e) {
            log.error("消费同步取消订单到第三方系统,{}", id + e.getMsg());
        }
    }
}
