package com.bat.thirdparty.xxljob.handler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.api.response.dto.PromotionDTO;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.PromotionExe;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/1 13:58
 */
@Component
public class PromotionXxlJob {

    private Logger logger = LoggerFactory.getLogger(PromotionXxlJob.class);

    @Resource
    private PromotionExe promotionExe;

    @Resource
    private CommonUtils commonUtils;

    /**
     * 促销活动开始执行器（参数为活动id，多个情况中间用逗号隔开）
     */
    @XxlJob("promotionStartJobHandler")
    public void promotionStartJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.promotionStart(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 促销活动结束执行器（参数为活动id，多个情况中间用逗号隔开）
     */
    @XxlJob("promotionStopJobHandler")
    public void promotionStopJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.promotionStop(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 促销活动定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    @XxlJob("promotionStartStopJobHandler")
    public void promotionStartStopJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        promotionExe.promotionStartStop();
        TenantContext.removeTenantNo();
    }

    /**
     * 拼团秒杀开始执行器（参数为拼团秒杀id，多个情况中间用逗号隔开）
     */
    @XxlJob("groupSeckillStartJobHandler")
    public void groupSeckillStartJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.groupSeckillStart(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 拼团秒杀结束执行器（参数为拼团秒杀id，多个情况中间用逗号隔开）
     */
    @XxlJob("groupSeckillStopJobHandler")
    public void groupSeckillStopJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.groupSeckillStop(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 拼团秒杀定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    @XxlJob("groupSeckillStartStopJobHandler")
    public void groupSeckillStartStopJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        promotionExe.groupSeckillStartStop();
        TenantContext.removeTenantNo();
    }

    /**
     * 优惠券开始执行器（参数为优惠券id，多个情况中间用逗号隔开）
     */
    @XxlJob("couponStartJobHandler")
    public void couponStartJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.couponStart(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 优惠券结束执行器（参数为优惠券id，多个情况中间用逗号隔开）
     */
    @XxlJob("couponStopJobHandler")
    public void couponStopJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
            .boxed().collect(Collectors.toList());
        promotionExe.couponStop(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 优惠券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    @XxlJob("couponStartStopJobHandler")
    public void couponStartStopJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        promotionExe.couponStartStop();
        TenantContext.removeTenantNo();
    }

    /**
     * 返利代金券开始执行器（参数为返利代金券id，多个情况中间用逗号隔开）
     */
    @XxlJob("rebateVoucherStartJobHandler")
    public void rebateVoucherStartJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
                .boxed().collect(Collectors.toList());
        promotionExe.rebateVoucherStart(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 返利代金券结束执行器（参数为返利代金券id，多个情况中间用逗号隔开）
     */
    @XxlJob("rebateVoucherStopJobHandler")
    public void rebateVoucherStopJobHandler() throws Exception {
        PromotionDTO dto = commonUtils.getPromotionDTO();
        List<Integer> ids = Arrays.stream(Arrays.stream(dto.getIds().split(",")).mapToInt(Integer::parseInt).toArray())
                .boxed().collect(Collectors.toList());
        promotionExe.rebateVoucherStop(ids);
        TenantContext.removeTenantNo();
    }

    /**
     * 返利代金券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    @XxlJob("rebateVoucherStartStopJobHandler")
    public void rebateVoucherStartStopJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        promotionExe.rebateVoucherStartStop();
        TenantContext.removeTenantNo();
    }

}
