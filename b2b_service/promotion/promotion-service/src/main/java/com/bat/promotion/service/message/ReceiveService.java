package com.bat.promotion.service.message;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.promotion.service.groupseckill.executor.GroupSeckillCmdExe;
import com.bat.promotion.mq.dto.RebateVoucherDTO;
import com.bat.promotion.mq.dto.RebateVoucherRollBackDTO;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bat.promotion.Tenant.TenantContext;
import com.bat.promotion.mq.api.Sink;
import com.bat.promotion.mq.dto.CouponStatusDTO;
import com.bat.promotion.mq.dto.GoodsSaleDTO;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.coupon.executor.CouponCmdExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/4 13:54
 */
@Service
public class ReceiveService {

    private static Logger logger = LoggerFactory.getLogger(ReceiveService.class);

    @Resource
    private CouponCmdExe couponCmdExe;

    @Resource
    private GroupSeckillCmdExe groupSeckillCmdExe;

    @Resource
    private RebateVoucherCmdExe rebateVoucherCmdExe;

    @Resource
    private PromotionConfig config;

    /**
     * 订单生成分销层级数据消息
     * 
     * @param dto
     */
    @StreamListener(value = Sink.PROMOTION_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'updateCouponStatus'")
    public void updateCouponStatus(@Headers Map headers, @Payload CouponStatusDTO dto) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        couponCmdExe.updateCustomerListCouponStatus(dto.getCouponNos(), dto.getCouponStatus(), null);
        TenantContext.removeTenantNo();
    }

    /**
     * 商品订单销售数量更新
     *
     */
    @StreamListener(value = Sink.PROMOTION_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderGoodsSale'")
    public void orderGoodsSaleInput(@Headers Map headers, @Payload List<GoodsSaleDTO> saleDTOS) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        groupSeckillCmdExe.updateRealSumList(saleDTOS);
        TenantContext.removeTenantNo();
    }

    /**
     * 抵扣代金券
     * @param headers
     * @param rebateVoucherDTO
     */
    @StreamListener(value = Sink.PROMOTION_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'deductionRebateVoucher'")
    public void deductionRebateVoucher(@Headers Map headers, @Payload RebateVoucherDTO rebateVoucherDTO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        rebateVoucherCmdExe.deductionRebateVoucher(rebateVoucherDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 回退代金券
     * @param headers
     */
    @StreamListener(value = Sink.PROMOTION_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'rollBackRebateVoucher'")
    public void rollBackRebateVoucher(@Headers Map headers, @Payload RebateVoucherRollBackDTO rebateVoucherRollBackDTO) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        rebateVoucherCmdExe.rollBackRebateVoucher(rebateVoucherRollBackDTO);
        TenantContext.removeTenantNo();
    }
}
