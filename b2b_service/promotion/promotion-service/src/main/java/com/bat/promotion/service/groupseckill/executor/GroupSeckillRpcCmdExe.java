package com.bat.promotion.service.groupseckill.executor;

import static com.bat.promotion.service.common.Constant.*;
import static com.bat.promotion.service.promotion.executor.ErrorCode.B_PROMOTION_DISTRIBUTOR_ERROR;
import static com.bat.promotion.service.promotion.executor.ErrorCode.B_PROMOTION_GOODS_PRICE_ERROR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsGroupSeckillRpcCmd;
import com.bat.dubboapi.thirdparty.xxljob.api.XxlJobServiceRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.groupseckill.dto.GroupSeckillCmd;
import com.bat.promotion.api.groupseckill.dto.GroupSeckillDistributorScopeCmd;
import com.bat.promotion.dao.groupseckill.GroupSeckillMapper;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillGoodsDO;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.promotion.convertor.PromotionConvertor;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/21 14:35
 */
@Component
public class GroupSeckillRpcCmdExe {
    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Resource
    private PromotionConfig promotionConfig;

    @DubboReference(check = false, timeout = 30000)
    private XxlJobServiceRpc xxlJobServiceRpc;

    @Resource
    private GroupSeckillMapper groupSeckillMapper;

    @Resource
    private PromotionConfig config;

    @Resource
    private GroupSeckillCmdExe groupSeckillCmdExe;

    /**
     * 更新分销商拼团秒杀可视数据
     *
     * @param groupSeckillId
     * @param cmd
     */
    public void distributorGroupSeckillRelevance(Integer groupSeckillId, GroupSeckillCmd cmd) {
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
            addDistributorIds.addAll(cmd.getDistributors().stream()
                .map(GroupSeckillDistributorScopeCmd::getDistributorId).collect(Collectors.toList()));
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
            throw PromotionException.buildException(B_PROMOTION_DISTRIBUTOR_ERROR);
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
            Response response = distributorServiceRpc
                .distributorGroupSeckillRelevanceByGroupSeckillId(addDistributorIds, groupSeckillId);
            if (!response.isSuccess()) {
                throw PromotionException.buildException(B_PROMOTION_DISTRIBUTOR_ERROR);
            }
        }
    }

    /**
     * 根据拼团秒杀活动计算商品最佳价格
     *
     * @param groupSeckillId
     * @param cmd
     */
    public void goodsPromotionPriceByGroupSeckill(Integer groupSeckillId, GroupSeckillCmd cmd) {
        GoodsGroupSeckillRpcCmd rpcCmd = PromotionConvertor.toGoodsGroupSeckillRpcCmd(groupSeckillId, cmd);
        com.bat.dubboapi.goods.common.Response response = goodsServiceRpc.goodsPromotionPriceByGroupSeckill(rpcCmd);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 根据拼团秒杀活动计算商品最佳价格
     *
     */
    public void goodsPromotionPriceByGroupSeckill(GroupSeckillDO groupSeckillDO, List<GroupSeckillGoodsDO> goodsDOS) {
        GoodsGroupSeckillRpcCmd rpcCmd = PromotionConvertor.toGoodsGroupSeckillRpcCmd(groupSeckillDO, goodsDOS);
        com.bat.dubboapi.goods.common.Response response = goodsServiceRpc.goodsPromotionPriceByGroupSeckill(rpcCmd);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 根据拼团秒杀活动id删除最佳价格
     *
     * @param groupSeckillId
     */
    public void deletePromotionPriceByGroupSeckillId(Integer groupSeckillId) {
        com.bat.dubboapi.goods.common.Response response =
            goodsServiceRpc.deletePromotionPriceByGroupSeckillId(groupSeckillId);
        if (!response.isSuccess()) {
            throw PromotionException.buildException(B_PROMOTION_GOODS_PRICE_ERROR);
        }
    }

    /**
     * 创建拼团秒杀定时任务
     *
     * @param groupSeckillDO
     */
    public void groupSeckillAddXxlJob(GroupSeckillDO groupSeckillDO) throws ParseException {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        // 获取当天最后时刻
        long time = CommonUtils.getTodayEndTime();
        // 创建活动开始定时任务
        if (groupSeckillDO.getGroupSeckillStatus().equals(PROMO_STATUS_0)
            && groupSeckillDO.getStartTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(groupSeckillDO, promotionConfig.getGroupSeckillStartJobHandler(),
                groupSeckillDO.getStartTime()));
        }
        // 创建活动结束定时任务
        if ((groupSeckillDO.getGroupSeckillStatus().equals(PROMO_STATUS_0)
            || groupSeckillDO.getGroupSeckillStatus().equals(PROMO_STATUS_1))
            && groupSeckillDO.getEndTime().getTime() < time) {
            cmds.add(getXxlJobRpcCmd(groupSeckillDO, promotionConfig.getGroupSeckillStopJobHandler(),
                groupSeckillDO.getEndTime()));
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    private XxlJobRpcCmd getXxlJobRpcCmd(GroupSeckillDO groupSeckillDO, String jobHandler, Date time) {
        XxlJobRpcCmd cmd = new XxlJobRpcCmd();
        cmd.setJobGroup(promotionConfig.getJobGroup());
        cmd.setAuthor(promotionConfig.getAuthor());
        cmd.setExecutorHandler(jobHandler);
        cmd.setScheduleConf(CommonUtils.getCronByDate(time));
        cmd.setExecutorParam(String.valueOf(groupSeckillDO.getId()));
        cmd.setJobDesc(String.valueOf(groupSeckillDO.getName()) + String.valueOf(groupSeckillDO.getId()));
        return cmd;
    }

    /**
     * 拼团秒杀定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    public void groupSeckillStartStop() {
        Date time = CommonUtils.get24Time();
        List<GroupSeckillDO> groupSeckillDOS = groupSeckillMapper.listGroupSeckillByTime(time);
        if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
            groupSeckillAddXxlJob(groupSeckillDOS);
        }
    }

    /**
     * 创建拼团秒杀定时任务
     *
     * @param groupSeckillDOS
     */
    public void groupSeckillAddXxlJob(List<GroupSeckillDO> groupSeckillDOS) {
        List<XxlJobRpcCmd> cmds = new ArrayList<>();
        List<Integer> startIds = new ArrayList<>();
        List<Integer> stopIds = new ArrayList<>();
        long time = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
            groupSeckillDOS.forEach(groupSeckillDO -> {
                // 判断活动开始
                if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_0)
                    && groupSeckillDO.getStartTime().getTime() < time + config.getIntervalTime()) {
                    startIds.add(groupSeckillDO.getId());
                } else if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_0)) {
                    cmds.add(getXxlJobRpcCmd(groupSeckillDO, promotionConfig.getGroupSeckillStartJobHandler(),
                        groupSeckillDO.getStartTime()));
                }
                // 判断活动结束
                if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)
                    && groupSeckillDO.getEndTime().getTime() < time + config.getIntervalTime()) {
                    stopIds.add(groupSeckillDO.getId());
                } else if (groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_1)) {
                    cmds.add(getXxlJobRpcCmd(groupSeckillDO, promotionConfig.getGroupSeckillStopJobHandler(),
                        groupSeckillDO.getEndTime()));
                }
            });
        }
        if (!CollectionUtils.isEmpty(startIds)) {
            groupSeckillCmdExe.updateListGroupSeckillStatus(startIds, GROUP_SECKILL_STATUS_1);
        }
        if (!CollectionUtils.isEmpty(stopIds)) {
            groupSeckillCmdExe.updateListGroupSeckillStatus(stopIds, GROUP_SECKILL_STATUS_3);
        }
        if (!CollectionUtils.isEmpty(cmds)) {
            xxlJobServiceRpc.xxlJobAdd(cmds);
        }
    }

    /**
     * 根据拼团秒杀id删除分销商可视拼团秒杀关系
     * 
     * @param groupSeckillId
     */
    public void deleteGroupSeckillRelevanceByGroupSeckillId(Integer groupSeckillId) {
        distributorServiceRpc.deleteGroupSeckillRelevanceByGroupSeckillId(groupSeckillId);
    }

}
