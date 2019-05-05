package com.bat.promotion.service.promotion.executor;

import static com.bat.promotion.service.common.Constant.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.promotion.convertor.PromotionConvertor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsPromotionRpcCmd;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.promotion.dto.PromotionCmd;
import com.bat.promotion.api.promotion.dto.PromotionDistributorScopeCmd;
import com.bat.promotion.dao.promotion.PromotionMapper;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.PromotionConfig;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:12
 */
@Component
@Slf4j
public class PromotionRpcCmdExe {

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @Resource
    private PromotionConfig promotionConfig;

    @Resource
    private PromotionMapper promotionMapper;

    @Resource
    private PromotionConfig config;

    @Resource
    private PromotionCmdExe promotionCmdExe;

    /**
     * 更新分销商促销活动可视数据
     * 
     * @param promotionId
     * @param cmd
     */
    public void distributorPromotionRelevance(Integer promotionId, PromotionCmd cmd) {
        log.info("更新分销商促销活动可视数据 活动id：{}，PromotionCmd：{}",promotionId, JSON.toJSONString(cmd));
        // 可视关系
        List<Integer> addDistributorIds = new ArrayList<>();
        Short distributorScope = cmd.getDistributorScope();
        Response<List<Integer>> listResponse = null;
        if (distributorScope.equals(SCOPE_SCALE_PRICE)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(cmd.getScalePriceIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(cmd.getDistributors())) {
            addDistributorIds.addAll(cmd.getDistributors().stream().map(PromotionDistributorScopeCmd::getDistributorId)
                .collect(Collectors.toList()));
        } else if (distributorScope.equals(SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(cmd.getDepartmentIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(cmd.getAdminIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        }
        if (listResponse != null && !listResponse.isSuccess()) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_DISTRIBUTOR_ERROR);
        }
        // 不可视关系
        List<Integer> delDistributorIds = new ArrayList<>();
        Short distributorScopeNo = cmd.getDistributorScopeNo();
        if (distributorScopeNo.equals(SCOPE_SCALE_PRICE)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(cmd.getScalePriceNoIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo.equals(SCOPE_DISTRIBUTOR)) {
            delDistributorIds.addAll(cmd.getDistributorNoIds());
        } else if (distributorScopeNo.equals(SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(cmd.getDepartmentNoIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo.equals(SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(cmd.getAdminIds());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        }
        // 剔除不可视范围
        addDistributorIds =
            addDistributorIds.stream().filter(distributorId -> !delDistributorIds.contains(distributorId)).distinct()
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(addDistributorIds)) {
            Response response =
                distributorServiceRpc.distributorPromotionRelevanceByPromotionId(addDistributorIds, promotionId);
            if (!response.isSuccess()) {
                throw PromotionException.buildException(ErrorCode.B_PROMOTION_DISTRIBUTOR_ERROR);
            }
        }
    }

    /**
     * 根据促销活动计算商品最佳价格
     * 
     * @param promotionId
     * @param cmd
     */
    public void goodsPromotionPriceByPromotion(Integer promotionId, PromotionCmd cmd) {
        GoodsPromotionRpcCmd rpcCmd = PromotionConvertor.toGoodsPromotionRpcCmd(promotionId, cmd);
        com.bat.dubboapi.goods.common.Response response = goodsServiceRpc.goodsPromotionPriceByPromotion(rpcCmd);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 根据促销活动计算商品最佳价格
     *
     */
    public void goodsPromotionPriceByPromotion(PromotionDO promotionDO, List<PromotionRuleDO> ruleDOS,
                                               List<PromotionRuleGoodsDO> ruleGoodsDOS, List<PromotionRuleConditionDO> ruleConditionDOS,
                                               List<PromotionRuleConditionSpecialDO> conditionSpecialDOS) {
        GoodsPromotionRpcCmd rpcCmd = PromotionConvertor.toGoodsPromotionRpcCmd(promotionDO, ruleDOS, ruleGoodsDOS,
            ruleConditionDOS, conditionSpecialDOS);
        com.bat.dubboapi.goods.common.Response response = goodsServiceRpc.goodsPromotionPriceByPromotion(rpcCmd);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 根据促销活动id删除最佳价格
     * 
     * @param promotionId
     */
    public void deletePromotionPriceByPromotionId(Integer promotionId) {
        com.bat.dubboapi.goods.common.Response response =
            goodsServiceRpc.deletePromotionPriceByPromotionId(promotionId);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 创建促销活动定时任务
     * 
     * @param promotionDO
     */
    public void promotionAddXxlJob(PromotionDO promotionDO) throws ParseException {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        // 获取当天最后时刻
        long time = CommonUtils.getTodayEndTime();
        // 创建活动开始定时任务
        if (promotionDO.getPromoStatus().equals(PROMO_STATUS_0) && promotionDO.getStartTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(promotionDO, promotionConfig.getPromotionStartJobHandler(),
                promotionDO.getStartTime()));
        }
        // 创建活动结束定时任务
        if ((promotionDO.getPromoStatus().equals(PROMO_STATUS_0) || promotionDO.getPromoStatus().equals(PROMO_STATUS_1))
            && promotionDO.getEndTime().getTime() < time) {
            cmds.add(
                getXxlJobRpcCmd(promotionDO, promotionConfig.getPromotionStopJobHandler(), promotionDO.getEndTime()));
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    private XxlJobRpcCmd getXxlJobRpcCmd(PromotionDO promotionDO, String jobHandler, Date item) {
        XxlJobRpcCmd cmd = new XxlJobRpcCmd();
        cmd.setJobGroup(promotionConfig.getJobGroup());
        cmd.setAuthor(promotionConfig.getAuthor());
        cmd.setExecutorHandler(jobHandler);
        cmd.setScheduleConf(CommonUtils.getCronByDate(item));
        cmd.setExecutorParam(String.valueOf(promotionDO.getId()));
        cmd.setJobDesc(String.valueOf(promotionDO.getName()) + String.valueOf(promotionDO.getId()));
        return cmd;
    }

    /**
     * 促销活动定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    public void promotionStartStop() {
        Date time = CommonUtils.get24Time();
        List<PromotionDO> promotionDOS = promotionMapper.listPromotionByTime(time);
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            promotionAddXxlJob(promotionDOS);
        }
    }

    /**
     * 创建促销活动定时任务
     *
     * @param promotionDOS
     */
    public void promotionAddXxlJob(List<PromotionDO> promotionDOS) {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        List<Integer> startIds = new ArrayList<>();
        List<Integer> stopIds = new ArrayList<>();
        long time = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            promotionDOS.forEach(promotionDO -> {
                // 判断活动开始
                if (promotionDO.getPromoStatus().equals(PROMO_STATUS_0)
                    && promotionDO.getStartTime().getTime() < time + config.getIntervalTime()) {
                    startIds.add(promotionDO.getId());
                } else if (promotionDO.getPromoStatus().equals(PROMO_STATUS_0)) {
                    cmds.add(getXxlJobRpcCmd(promotionDO, promotionConfig.getPromotionStartJobHandler(),
                        promotionDO.getStartTime()));
                }
                // 判断活动结束
                if (promotionDO.getPromoStatus().equals(PROMO_STATUS_1)
                    && promotionDO.getEndTime().getTime() < time + config.getIntervalTime()) {
                    stopIds.add(promotionDO.getId());
                } else if (promotionDO.getPromoStatus().equals(PROMO_STATUS_1)) {
                    cmds.add(getXxlJobRpcCmd(promotionDO, promotionConfig.getPromotionStopJobHandler(),
                        promotionDO.getEndTime()));
                }
            });
        }
        if (!CollectionUtils.isEmpty(startIds)) {
            promotionCmdExe.updateListPromotionStatus(startIds, PROMO_STATUS_1);
        }
        if (!CollectionUtils.isEmpty(stopIds)) {
            promotionCmdExe.updateListPromotionStatus(stopIds, PROMO_STATUS_2);
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    /**
     * 根据促销活动id删除分销商可视促销活动关系
     *
     * @param promotionId
     */
    public void deletePromotionRelevanceByPromotionId(Integer promotionId) {
        distributorServiceRpc.deletePromotionRelevanceByPromotionId(promotionId);
    }

}
