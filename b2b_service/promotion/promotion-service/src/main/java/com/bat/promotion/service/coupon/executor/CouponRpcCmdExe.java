package com.bat.promotion.service.coupon.executor;

import static com.bat.promotion.service.common.Constant.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.promotion.dao.coupon.CouponMapper;
import com.bat.promotion.dao.coupon.dataobject.CouponDO;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.PromotionConfig;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:12
 */
@Component
public class CouponRpcCmdExe {

    @DubboReference(check = false, timeout = 5000)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @Resource
    private PromotionConfig promotionConfig;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private PromotionConfig config;

    @Resource
    private CouponCmdExe couponCmdExe;

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponRpcCmdExe.class);

    /**
     * 创建优惠券定时任务
     * 
     * @param couponDO
     */
    public void couponAddXxlJob(CouponDO couponDO) throws ParseException {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        // 获取当天最后时刻
        long time = CommonUtils.getTodayEndTime();
        // 创建优惠券开始定时任务
        if (couponDO.getCouponStatus().equals(COUPON_STATUS_1) && couponDO.getStartTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(couponDO, promotionConfig.getCouponStartJobHandler(), couponDO.getStartTime()));
        }
        // 创建优惠券结束定时任务
        if ((couponDO.getCouponStatus().equals(COUPON_STATUS_1) || couponDO.getCouponStatus().equals(COUPON_STATUS_2)
            || couponDO.getCouponStatus().equals(COUPON_STATUS_4)) && couponDO.getEndTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(couponDO, promotionConfig.getCouponStopJobHandler(), couponDO.getEndTime()));
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    private XxlJobRpcCmd getXxlJobRpcCmd(CouponDO couponDO, String jobHandler, Date item) {
        XxlJobRpcCmd cmd = new XxlJobRpcCmd();
        cmd.setJobGroup(promotionConfig.getJobGroup());
        cmd.setAuthor(promotionConfig.getAuthor());
        cmd.setExecutorHandler(jobHandler);
        cmd.setScheduleConf(CommonUtils.getCronByDate(item));
        cmd.setExecutorParam(String.valueOf(couponDO.getId()));
        cmd.setJobDesc(String.valueOf(couponDO.getName()) + String.valueOf(couponDO.getId()));
        return cmd;
    }

    /**
     * 优惠券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    public void couponStartStop() {
        Date time = CommonUtils.get24Time();
        LOGGER.info("查询到的时间:{}", time);
        List<CouponDO> couponDOS = couponMapper.listCouponByTime(time);
        LOGGER.info("即将启动的优惠卷:{}", JSONObject.toJSONString(couponDOS));
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponAddXxlJob(couponDOS);
        }
    }

    /**
     * 创建优惠券定时任务
     *
     * @param couponDOS
     */
    public void couponAddXxlJob(List<CouponDO> couponDOS) {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        List<Integer> startIds = new ArrayList<>();
        List<Integer> stopIds = new ArrayList<>();
        long time = System.currentTimeMillis();
        LOGGER.info("得到现在的时间:{}", time);
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOS.forEach(couponDO -> {
                LOGGER.info("开始遍历优惠卷活动:{}", JSONObject.toJSONString(couponDO));
                // 判断活动开始
                if (couponDO.getCouponStatus().equals(COUPON_STATUS_1)
                    && couponDO.getStartTime().getTime() < time + config.getIntervalTime()) {
                    LOGGER.info("加入启动列表:{}", couponDO.getId());
                    startIds.add(couponDO.getId());
                } else if (couponDO.getCouponStatus().equals(COUPON_STATUS_1)) {
                    LOGGER.info("加入启动xxj列表:{}", couponDO.getId());
                    cmds.add(getXxlJobRpcCmd(couponDO, promotionConfig.getCouponStartJobHandler(),
                        couponDO.getStartTime()));
                }
                // 判断活动结束
                if ((couponDO.getCouponStatus().equals(COUPON_STATUS_2)
                    || couponDO.getCouponStatus().equals(COUPON_STATUS_4))
                    && couponDO.getEndTime().getTime() < time + config.getIntervalTime()) {
                    LOGGER.info("加入结束列表:{}", couponDO.getId());
                    stopIds.add(couponDO.getId());
                } else if (couponDO.getCouponStatus().equals(COUPON_STATUS_2)) {
                    LOGGER.info("加入结束xxj列表:{}", couponDO.getId());
                    cmds.add(
                        getXxlJobRpcCmd(couponDO, promotionConfig.getCouponStopJobHandler(), couponDO.getEndTime()));
                }
                LOGGER.info("当前优惠卷处理结束");
            });
        }
        if (!CollectionUtils.isEmpty(startIds)) {
            couponCmdExe.updateListCouponStatus(startIds, COUPON_STATUS_2, null);
        }
        if (!CollectionUtils.isEmpty(stopIds)) {
            couponCmdExe.updateListCouponStatus(stopIds, COUPON_STATUS_3, null);
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

}
