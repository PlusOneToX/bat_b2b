package com.platform.modules.job.task;

import com.alibaba.fastjson.JSONObject;
import com.platform.common.utils.*;
import com.platform.modules.mall.dto.BalanceDto;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.mall.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 合伙人佣金到账任务
 */
@Slf4j
@Component("orderPreTask")
public class OrderPreTask {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    @Autowired
    private MallUserServiceImpl mallUserService;
    @Autowired
    private MallRewardPreService preService;
    @Autowired
    private MallRewardRecordService rewardRecordService;
    @Autowired
    private MallAccountLogService mallAccountLogService;
    @Autowired
    private MallUserLevelService userLevelService;
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private MallDistServiceImpl mallDistService;
    @Autowired
    private MallUserLevelServiceImpl mallUserLevelService;


    /**
     * 执行合伙人佣金到账任务
     */
    @SuppressWarnings(value = "unused")
    public void exect() {
        // Map<String, MallDistEntity> userId$Map = mallDistService.list().stream().collect(Collectors.toMap(MallDistEntity::getUserId, e -> e));
        List<MallRewardPreEntity> preEntities = preService.lambdaQuery().eq(MallRewardPreEntity::getStatus, 0).orderByAsc(MallRewardPreEntity::getCreateTime).list();
        log.info("开始执行合伙人分成 {}", new Date());
        if (!ObjectUtils.isEmpty(preEntities)) {
            for (MallRewardPreEntity entity : preEntities) {
                try {
                    reward(entity);
                } catch (Exception e) {
                    log.error("释放奖励出错,id{}", entity.getId());
                    log.error("释放奖励出错,详细信息", e);
                }
            }
        }
        log.info("合伙人分成结束 {}", new Date());
    }

    /**
     * 释放奖励
     *
     * @param entity 需要进行分成的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void reward(MallRewardPreEntity entity) throws Exception {
        MallOrderEntity order = mallOrderService.queryById(entity.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在,订单号:" + entity.getOrderId());
        }
        if (order.getOrderStatus().intValue() != Constant.OrderStatus.QRSH.getValue()) {
            throw new RuntimeException("订单状态不是收货状态,订单号:" + order.getId());
        }
        MallUserEntity user = mallUserService.lambdaQuery().eq(MallUserEntity::getId, order.getUserId()).one();
        if (user == null) {
            throw new RuntimeException("订单用户不存在,订单号:" + order.getId());
        }
        if (StringUtils.isEmpty(user.getInviteUserId())) {
            log.info("订单 {} 无上级邀请人，忽略该订单", order.getId());
            entity.setStatus(1);
            preService.update(entity);
        } else {

            MallUserEntity inviteUser = mallUserService.lambdaQuery().eq(MallUserEntity::getId, user.getInviteUserId()).one();
            if (inviteUser == null) {
                throw new RuntimeException("上级用户不存在,订单号:" + order.getId());
            }
            MallDistEntity userMallDist = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, user.getId()).one();
            userMallDist.setAmountTotal(BigDecimalUtil.add(userMallDist.getAmountTotal(), order.getActualPrice()));
            mallDistService.saveOrUpdate(userMallDist);

            MallDistEntity mallDistEntity = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, inviteUser.getId()).one();
            if (ObjectUtil.isNotEmpty(mallDistEntity)) {
                mallDistEntity.setAmountTotal(BigDecimalUtil.add(mallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(order.getActualPrice(), 0.5)));
                mallDistService.saveOrUpdate(mallDistEntity);
                MallDistEntity twoMallDistEntity = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, mallDistEntity.getSuperiorId()).one();
                if (ObjectUtil.isNotEmpty(twoMallDistEntity)) {
                    twoMallDistEntity.setAmountTotal(BigDecimalUtil.add(twoMallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(order.getActualPrice(), 0.25)));
                    mallDistService.saveOrUpdate(twoMallDistEntity);
                }
            }

            //判断上级用户是否达到了合伙人标准
            MallUserLevelEntity inviteUserLevel = userLevelService.lambdaQuery().eq(MallUserLevelEntity::getId, inviteUser.getUserLevelId()).one();
            inviteUserLevel.setDirectPushRate(null);
            if (inviteUserLevel == null) {
                throw new RuntimeException("上级用户登记不存在,订单号:" + order.getId());
            }
            if (StringUtils.isEmpty(inviteUserLevel.getDirectPushRate()) || inviteUserLevel.getDirectPushRate().equals("0")) {
                log.info("订单 {} 上级无分成比例，忽略该订单", order.getId());
                entity.setStatus(1);
                preService.update(entity);
            } else {

                BigDecimal rate = new BigDecimal(inviteUserLevel.getDirectPushRate());
                BigDecimal rewardAmount = order.getActualPrice().multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                //奖励到余额
                inviteUser.setBalance(inviteUser.getBalance().add(rewardAmount));
                mallUserService.update(inviteUser);
                //记录账户日志
                MallAccountLogEntity logEntity = new MallAccountLogEntity();
                logEntity.setAddTime(new Date());
                logEntity.setFromType(1);
                logEntity.setOrderSn(order.getOrderSn());
                logEntity.setLogDesc("订单合伙人分成");
                logEntity.setNickname(inviteUser.getNickname());
                logEntity.setUserId(inviteUser.getId());
                logEntity.setPrice(rewardAmount);
                logEntity.setOrderType(7);
                logEntity.setType(1);
                mallAccountLogService.save(logEntity);
                //记录日志
                MallRewardRecordEntity rewardRecord = new MallRewardRecordEntity();
                rewardRecord.setActualPrice(order.getActualPrice());
                rewardRecord.setCreateTime(new Date());
                rewardRecord.setOrderId(order.getId());
                rewardRecord.setOrderSn(order.getOrderSn());
                rewardRecord.setOrderUserId(user.getId());
                rewardRecord.setRewardAmount(rewardAmount);
                rewardRecord.setRewardRate(rate);
                rewardRecord.setUserId(inviteUser.getId());
                rewardRecordService.save(rewardRecord);
                entity.setStatus(1);
                preService.update(entity);
            }
        }
    }

    @Autowired
    private MallUserInviteRecordServiceImpl mallUserInviteRecordService;
    @Autowired
    private MallAwardConfServiceImpl awardConfService;
    @Autowired
    private MallUserIntegralServiceImpl mallUserIntegralService;
    @Autowired
    private MallGoodsServiceImpl mallGoodsService;
    @Autowired
    private MallCategoryServiceImpl mallCategoryService;
    @Autowired
    private MallDistributionConfServiceImpl mallDistributionConfService;
    @Autowired
    private MallUserLevelServiceImpl mallUserLevelConfService;
    @Autowired
    private MallDistributionRecordServiceImpl mallDistributionRecordService;


    @Transactional
    public void exect1() {
        List<MallRewardPreEntity> preEntities = preService.lambdaQuery().eq(MallRewardPreEntity::getStatus, 0).orderByAsc(MallRewardPreEntity::getCreateTime).list();
        if (ObjectUtil.isEmpty(preEntities)) {
            return;
        }
        //List<String> orderIds = preEntities.stream().map(MallRewardPreEntity::getOrderId).collect(Collectors.toList());
        //查询平台用户梯度等级
        List<MallUserLevelEntity> mallUserLevelConfEntities = mallUserLevelConfService.list();
        //查询所有商品sku
        Map<String, MallGoodsSkuEntity> id$MallGoodsSkuEntity = mallGoodsSkuService.list().stream().collect(Collectors.toMap(MallGoodsSkuEntity::getId, e -> e));

        //Map<String, List<MallOrderEntity>> userId$MallOrderEntityList = mallOrderService.lambdaQuery().in(MallOrderEntity::getId,orderIds).list().stream().collect(Collectors.groupingBy(MallOrderEntity::getUserId));
        List<MallOrderEntity> mallOrderEntityList = mallOrderService.lambdaQuery().eq(MallOrderEntity::getOrderStatus, Constant.OrderStatus.QRSH.getValue()).list();
        Map<String, MallOrderEntity> id$MallOrder = mallOrderEntityList.stream().collect(Collectors.toMap(MallOrderEntity::getId, e -> e));

        Map<String, List<MallOrderEntity>> userId$MallOrderEntityList = mallOrderEntityList.stream().collect(Collectors.groupingBy(MallOrderEntity::getUserId));

        Map<String, MallDistributionConfEntity> type$Level$MallDistributionConfEntity = mallDistributionConfService.list().stream().collect(Collectors.toMap(e -> e.getType().toString() + e.getLevel() + e.getOrderUserLevel(), e -> e));


        Map<String, MallUserIntegralEntity> userId$MallUserIntegralEntity = mallUserIntegralService.list().stream().collect(Collectors.toMap(MallUserIntegralEntity::getUserId, e -> e));

        List<Integer> confTypes = Arrays.asList(3, 4, 5, 6, 7);
        Map<Integer, MallAwardConfEntity> type$MallAwardConfEntity = awardConfService.lambdaQuery().in(MallAwardConfEntity::getType, confTypes).list().stream().collect(Collectors.toMap(MallAwardConfEntity::getType, e -> e));

        Map<String, MallGoodsEntity> id$MallGoodsEntity = mallGoodsService.list().stream().collect(Collectors.toMap(MallGoodsEntity::getId, e -> e));

        // Map<String, MallCategoryEntity> name$MallCategoryEntity = mallCategoryService.list().stream().collect(Collectors.toMap(MallCategoryEntity::getName, e -> e));

        Map<String, MallUserInviteRecordEntity> userId$MallUserInviteRecord = mallUserInviteRecordService.list().stream().collect(Collectors.toMap(MallUserInviteRecordEntity::getUserId, e -> e));

        //  Map<String, MallDistEntity> userId$MallDist = mallDistService.list().stream().collect(Collectors.toMap(MallDistEntity::getUserId, e -> e));
        Map<String, MallUserEntity> userId$MallUser = mallUserService.list().stream().collect(Collectors.toMap(MallUserEntity::getId, e -> e));
        Map<String, MallUserLevelEntity> userId$MallUserLevel = userLevelService.list().stream().collect(Collectors.toMap(MallUserLevelEntity::getId, e -> e));

        Map<String, List<MallOrderGoodsEntity>> orderId$List$MallOrderGoodsEntity = mallOrderGoodsService.list().stream().collect(Collectors.groupingBy(MallOrderGoodsEntity::getOrderId));

        Map<String, MallCategoryEntity> id$MallCategoryEntity = mallCategoryService.list().stream().collect(Collectors.toMap(MallCategoryEntity::getId, e -> e));

        ArrayList<MallAccountLogEntity> mallAccountLogs = new ArrayList<>();
        ArrayList<MallRewardRecordEntity> mallRewardRecords = new ArrayList<>();
        HashMap<String, MallUserEntity> userId$MallUser$SaveList = new HashMap<>();
        //HashMap<String, MallDistEntity> userId$MallDist$SaveList = new HashMap<>();
        ArrayList<IntegralDto> integralDtos = new ArrayList<>();
        ArrayList<BalanceDto> balanceDtos = new ArrayList<>();

        ArrayList<MallDistributionRecordEntity> mallDistributionRecordEntities = new ArrayList<>();
        ArrayList<MallOrderAwardRecordEntity> mallOrderAwardRecordEntities = new ArrayList<>();

        Date updateTime = new Date();
        Date createTime = updateTime;

         for (MallRewardPreEntity preEntity : preEntities) {
            MallOrderEntity mallOrderEntity = id$MallOrder.get(preEntity.getOrderId());
            if (ObjectUtil.isNotEmpty(mallOrderEntity)) {
                String pid = mallOrderEntity.getId();
                BigDecimal actualPrice = mallOrderEntity.getActualPrice();
                MallUserInviteRecordEntity mallUserInviteRecordEntity = userId$MallUserInviteRecord.get(mallOrderEntity.getUserId());


                if (ObjectUtil.isNotEmpty(mallUserInviteRecordEntity)) {
                    MallUserIntegralEntity orderMallUserIntegralEntity = userId$MallUserIntegralEntity.get(mallUserInviteRecordEntity.getInviteUserId());
                    if (ObjectUtil.isEmpty(orderMallUserIntegralEntity)) {
                        orderMallUserIntegralEntity = new MallUserIntegralEntity().setAmount(BigDecimal.ZERO);
                    }
                    List<MallOrderEntity> orderMallOrderEntities = userId$MallOrderEntityList.get(mallOrderEntity.getUserId());
                    BigDecimal orderReduce = BigDecimal.ZERO;
                    if (ObjectUtil.isNotEmpty(orderMallOrderEntities)) {
                        orderReduce = orderMallOrderEntities.stream().map(MallOrderEntity::getActualPrice).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
                    }

                    BigDecimal orderFinalReduce = orderReduce;
                    MallUserIntegralEntity orderFinalMallUserIntegralEntity = orderMallUserIntegralEntity;

                    Optional<MallUserLevelEntity> orderFirst = mallUserLevelConfEntities.stream()
                            .filter(e -> !BigDecimalUtil.greater(e.getMoney(), orderFinalReduce) &&
                                    !BigDecimalUtil.greater(e.getIntegral(), orderFinalMallUserIntegralEntity.getAmount()))
                            .sorted(Comparator.comparing(MallUserLevelEntity::getLevelValue).reversed()).findFirst();

                    Integer orderLevelValue = orderFirst.get().getLevelValue();

                    MallUserEntity inviteUser = userId$MallUser.get(mallUserInviteRecordEntity.getInviteUserId());

                    recursion(1, userId$MallUserInviteRecord, mallDistributionRecordEntities, mallUserInviteRecordEntity);

                    Integer count = 0;
                    Integer pushCount = 0;


                    for (MallDistributionRecordEntity mallDistributionRecordEntity : mallDistributionRecordEntities) {
                        if (2 == userId$MallUser.get(mallOrderEntity.getUserId()).getRoleType()) {
                            break;
                        }
                        mallDistributionRecordEntity.setActualPrice(actualPrice).setCreateTime(createTime).setOriginId(mallOrderEntity.getUserId()).setOriginNikeName(mallUserInviteRecordEntity.getUserName());
                        BigDecimal reduce = BigDecimal.ZERO;
                        List<MallOrderEntity> mallOrderEntities = userId$MallOrderEntityList.get(mallDistributionRecordEntity.getUserId());
                        if (ObjectUtil.isNotEmpty(mallOrderEntities)) {
                            reduce = mallOrderEntities.stream().map(MallOrderEntity::getActualPrice).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
                        }
                        MallUserIntegralEntity mallUserIntegralEntity = userId$MallUserIntegralEntity.get(mallUserInviteRecordEntity.getInviteUserId());
                        if (ObjectUtil.isEmpty(mallUserIntegralEntity)) {
                            mallUserIntegralEntity = new MallUserIntegralEntity().setAmount(BigDecimal.ZERO);
                        }
                        BigDecimal finalReduce = reduce;
                        MallUserIntegralEntity finalMallUserIntegralEntity = mallUserIntegralEntity;
                        Optional<MallUserLevelEntity> first = mallUserLevelConfEntities.stream().filter(e -> !BigDecimalUtil.greater(e.getMoney(), finalReduce) && !BigDecimalUtil.greater(e.getIntegral(), finalMallUserIntegralEntity.getAmount())).sorted(Comparator.comparing(MallUserLevelEntity::getLevelValue).reversed()).findFirst();
                        mallDistributionRecordEntity.setIntegral(BigDecimalUtil.toDecimal(mallUserIntegralEntity.getAmount())).setPid(mallOrderEntity.getId()).setOrderActualPrice(reduce);
                        MallUserEntity mallUserEntity = userId$MallUser.get(mallDistributionRecordEntity.getUserId());
                        if (first.isPresent() || !BigDecimalUtil.equal(0, mallUserEntity.getLevel())) {

                            Integer levelValue = 0;
                            if (!BigDecimalUtil.equal(0, mallUserEntity.getLevel())) {
                                levelValue = mallUserEntity.getLevel();
                            } else {
                                levelValue = first.get().getLevelValue();
                            }
                            String pre = mallUserEntity.getRoleType().toString();//区分用户类型 1普通用户 2商家 3认证中 4推广者
                            if (2 == mallUserEntity.getRoleType()) {
                                count++;
                            }

                            if (2 != mallUserEntity.getRoleType()) {
                                pre = pre + levelValue;
                            } else {
                                pre = pre + "1";
                            }
                            pre = pre + orderLevelValue;


                            if (count > 1 || (mallDistributionRecordEntity.getRelation() >= 3 && count <= 1) || pushCount == 1) {
                                break;
                            }
                            mallDistributionRecordEntity.setLevel(levelValue);

                            //获取奖励配置信息
                            MallDistributionConfEntity mallDistributionConfEntity = type$Level$MallDistributionConfEntity.get(pre);
                            BigDecimal ratio = BigDecimal.ZERO;
                            if (ObjectUtil.isNotEmpty(mallDistributionConfEntity)) {
                                if (ObjectUtil.isNotEmpty(mallDistributionConfEntity)) {
                                    if (1 == mallDistributionRecordEntity.getRelation() || (2 == mallDistributionRecordEntity.getRelation() && mallUserEntity.getRoleType() == 3)) {
                                        ratio = mallDistributionConfEntity.getDirect();//直推奖励的比例
                                    } else {
                                        ratio = mallDistributionConfEntity.getIndirect();//间推奖励比例
                                    }
                                }
                            }
                            BigDecimal award = BigDecimalUtil.multiply(actualPrice, BigDecimalUtil.percentage(ratio));
                            mallDistributionRecordEntity.setAward(award).setRatio(ratio);
                            BalanceDto balanceDto = new BalanceDto().setAmount(award).setBusinessType(7).setType(1).setLabel("分销奖励").setPid(mallOrderEntity.getId()).setUpdateTime(updateTime).setUserId(mallDistributionRecordEntity.getUserId());
                            balanceDtos.add(balanceDto);
                            if (3 == mallUserEntity.getRoleType()) {
                                pushCount++;
                            }
                        }

                    }


                    if (ObjectUtil.isNotEmpty(inviteUser)) {
                        //MallUserLevelEntity mallUserLevelEntity = userId$MallUserLevel.get(inviteUser.getId());
                        //mallUserLevelEntity.setDirectPushRate(null);
                        //if (ObjectUtil.isNotEmpty(mallUserLevelEntity) && !BigDecimalUtil.equal(mallUserLevelEntity.getDirectPushRate(), 0)) {

                        //    BigDecimal rewardAmount = BigDecimalUtil.setScale(BigDecimalUtil.multiply(actualPrice, mallUserLevelEntity.getDirectPushRate()));
                        //    BalanceDto balanceDto = new BalanceDto().setAmount(rewardAmount).setBusinessType(8).setType(1).setLabel("合伙人奖励").setPid(mallOrderEntity.getId()).setUpdateTime(updateTime).setUserId(inviteUser.getId());
                        //    balanceDtos.add(balanceDto);
                        //    /*
                        //    inviteUser.setBalance(BigDecimalUtil.add(inviteUser.getBalance(), rewardAmount));
                        //    userId$MallUser$SaveList.put(inviteUser.getId(), inviteUser);
                        //    //记录账户日志
                        //    MallAccountLogEntity logEntity = new MallAccountLogEntity().setAddTime(createTime).setFromType(1).setOrderSn(mallOrderEntity.getOrderSn()).setLogDesc("订单合伙人分成").setNickname(inviteUser.getNickname()).setUserId(inviteUser.getId()).setPrice(rewardAmount).setOrderType(7).setType(1);
                        //    mallAccountLogs.add(logEntity);*/
                        //    //记录日志
                        //    MallRewardRecordEntity rewardRecord = new MallRewardRecordEntity().setActualPrice(actualPrice).setCreateTime(createTime).setOrderId(pid).setOrderSn(mallOrderEntity.getOrderSn()).setOrderUserId(mallOrderEntity.getUserId()).setRewardAmount(rewardAmount).setRewardRate(BigDecimalUtil.toDecimal(actualPrice)).setUserId(inviteUser.getId());
                        //    mallRewardRecords.add(rewardRecord);
                        //}

                       /* MallDistEntity mallDistEntity = userId$MallDist.get(mallOrderEntity.getUserId());
                        mallDistEntity.setAmountTotal(BigDecimalUtil.add(mallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(mallOrderEntity.getActualPrice(), 0.5)));
                        userId$MallDist$SaveList.put(mallDistEntity.getUserId(), mallDistEntity);*/

                        Boolean flag = false;

                        MallAwardConfEntity mallAwardConfEntity = type$MallAwardConfEntity.get(3);
                        if (ObjectUtil.isNotEmpty(mallAwardConfEntity)) {
                            BigDecimal amount = BigDecimalUtil.multiply(actualPrice, BigDecimalUtil.percentage(mallAwardConfEntity.getRatio()));
                            IntegralDto integralDto = new IntegralDto().setLabel("直推").setType(mallAwardConfEntity.getType()).setPid(pid).setRemark(JSONObject.toJSONString(mallOrderEntity)).setUserId(inviteUser.getId()).setLabel(mallAwardConfEntity.getRemark()).setAmount(amount).setUpdateTime(updateTime);
                            if (2 != inviteUser.getRoleType()) {
                                integralDtos.add(integralDto);//返回直推积分奖励
                                MallOrderAwardRecordEntity mallOrderAwardRecordEntity = new MallOrderAwardRecordEntity().setUserId(inviteUser.getInviteUserId()).setAmount(amount).setCreateTime(createTime).setOrderPrice(actualPrice).setOriginUserId(mallOrderEntity.getUserId()).setPid(mallOrderEntity.getId()).setRelation(1);
                                mallOrderAwardRecordEntities.add(mallOrderAwardRecordEntity);
                            } else {
                                flag = true;
                            }

                        }

                        MallUserInviteRecordEntity twoMallUserInviteRecordEntity = userId$MallUserInviteRecord.get(mallUserInviteRecordEntity.getInviteUserId());


                        if (ObjectUtil.isNotEmpty(twoMallUserInviteRecordEntity)) {
                            Integer type = 4;
                            String label = "间推";
                            if (flag) {
                                type = 3;
                                label = "直推";
                            }
                            MallAwardConfEntity twoMallAwardConfEntity = type$MallAwardConfEntity.get(type);
                            if (ObjectUtil.isNotEmpty(twoMallAwardConfEntity)) {
                                BigDecimal amount = BigDecimalUtil.multiply(actualPrice, BigDecimalUtil.percentage(twoMallAwardConfEntity.getRatio()));
                                if (2 != userId$MallUser.get(twoMallUserInviteRecordEntity.getInviteUserId()).getRoleType()) {
                                    IntegralDto twoIntegralDto = new IntegralDto().setLabel(label).setType(mallAwardConfEntity.getType()).setPid(pid).setRemark(JSONObject.toJSONString(mallOrderEntity)).setUserId(twoMallUserInviteRecordEntity.getInviteUserId()).setLabel(twoMallAwardConfEntity.getRemark()).setAmount(amount).setUpdateTime(updateTime);
                                    MallOrderAwardRecordEntity mallOrderAwardRecordEntity = new MallOrderAwardRecordEntity().setUserId(twoMallUserInviteRecordEntity.getInviteUserId()).setAmount(amount).setCreateTime(createTime).setOrderPrice(actualPrice).setOriginUserId(mallOrderEntity.getUserId()).setPid(mallOrderEntity.getId()).setRelation(1);
                                    mallOrderAwardRecordEntities.add(mallOrderAwardRecordEntity);
                                    integralDtos.add(twoIntegralDto);
                                }
                            }
                        }

                       /* MallDistEntity twoMallDistEntity = userId$MallDist.get(mallDistEntity.getSuperiorId());
                        if (ObjectUtil.isNotEmpty(twoMallDistEntity)) {
                            twoMallDistEntity.setAmountTotal(BigDecimalUtil.add(twoMallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(actualPrice, 0.25)));
                            userId$MallDist$SaveList.put(twoMallDistEntity.getUserId(), twoMallDistEntity);
                        }*/

                    }
                }

                List<MallOrderGoodsEntity> mallOrderGoodsEntities = orderId$List$MallOrderGoodsEntity.get(mallOrderEntity.getId());
                for (MallOrderGoodsEntity mallOrderGoodsEntity : mallOrderGoodsEntities) {

                    MallGoodsEntity mallGoodsEntity = id$MallGoodsEntity.get(mallOrderGoodsEntity.getGoodsId());
                    MallCategoryEntity mallCategoryEntity = id$MallCategoryEntity.get(mallGoodsEntity.getCategoryId());
                    MallGoodsSkuEntity mallGoodsSkuEntity = id$MallGoodsSkuEntity.get(mallOrderGoodsEntity.getSkuId());
                    MallAwardConfEntity mallAwardConfEntity = null;
                    if (StringUtils.equals(mallCategoryEntity.getName(), "海报")) {
                        mallAwardConfEntity = type$MallAwardConfEntity.get(6);
                    } else {
                        mallAwardConfEntity = type$MallAwardConfEntity.get(7);
                    }

                    if (ObjectUtil.isNotEmpty(mallAwardConfEntity)) {
                        IntegralDto integralDto = new IntegralDto().setLabel(mallGoodsEntity.getCategoryName()).setAmount(BigDecimalUtil.multiply(BigDecimalUtil.multiply(mallGoodsSkuEntity.getRetailPrice(), mallOrderGoodsEntity.getNumber()), mallAwardConfEntity.getRatio())).setType(mallAwardConfEntity.getType()).setLabel(mallAwardConfEntity.getRemark()).setUserId(mallOrderEntity.getUserId()).setPid(pid).setRemark(JSONObject.toJSONString(mallOrderEntity)).setUpdateTime(updateTime);
                        integralDtos.add(integralDto);//自己购买积分奖励
                    }
                }





                /*MallDistEntity mallDistEntity = userId$MallDist.get(mallOrderEntity.getUserId());
                mallDistEntity.setAmountTotal(BigDecimalUtil.add(mallDistEntity.getAmountTotal(), mallOrderEntity.getActualPrice()));
                userId$MallDist$SaveList.put(mallDistEntity.getUserId(), mallDistEntity);*/
            }
            preEntity.setStatus(1);
        }

        if (ObjectUtil.isNotEmpty(mallDistributionRecordEntities)) {
            mallDistributionRecordService.saveBatch(mallDistributionRecordEntities);
        }

        if (ObjectUtil.isNotEmpty(mallOrderAwardRecordEntities)) {
            mallOrderAwardRecordService.saveBatch(mallOrderAwardRecordEntities);
        }

        preService.saveOrUpdateBatch(preEntities);
        if (ObjectUtil.isNotEmpty(mallAccountLogs)) {
            mallAccountLogService.saveBatch(mallAccountLogs);
        }
        if (ObjectUtil.isNotEmpty(mallRewardRecords)) {
            rewardRecordService.saveBatch(mallRewardRecords);
        }
        if (!userId$MallUser$SaveList.isEmpty()) {
            ArrayList<MallUserEntity> mallUserEntities = new ArrayList<>(userId$MallUser$SaveList.values());
            mallUserService.saveOrUpdateBatch(mallUserEntities);
        }
        for (IntegralDto integralDto : integralDtos) {
//            executor.submit(() -> mallUserIntegralService.exchange(integralDto));
            mallUserIntegralService.exchange(integralDto);
        }
        for (BalanceDto balanceDto : balanceDtos) {
            executor.submit(() -> mallUserService.exchangeBalance(balanceDto));
            mallUserService.exchangeBalance(balanceDto);
        }
       /* if (!userId$MallDist$SaveList.isEmpty()) {
            ArrayList<MallDistEntity> mallDistEntities = new ArrayList<>(userId$MallDist$SaveList.values());
            mallDistService.saveOrUpdateBatch(mallDistEntities);
        }*/


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private MallOrderGoodsServiceImpl mallOrderGoodsService;

    @Autowired
    private MallOrderAwardRecordServiceImpl mallOrderAwardRecordService;
    @Autowired
    private MallGoodsSkuServiceImpl mallGoodsSkuService;


    public void recursion(Integer relation, Map<String, MallUserInviteRecordEntity> userId$MallUserInviteRecord, ArrayList<MallDistributionRecordEntity> mallDistributionRecordEntities, MallUserInviteRecordEntity mallUserInviteRecordEntity) {
        if (ObjectUtil.isNotEmpty(mallUserInviteRecordEntity) && relation < 5) {
            MallDistributionRecordEntity mallDistributionRecordEntity = new MallDistributionRecordEntity().setUserId(mallUserInviteRecordEntity.getInviteUserId()).setSubordinateId(mallUserInviteRecordEntity.getUserId()).setRelation(relation);
            mallDistributionRecordEntities.add(mallDistributionRecordEntity);
            MallUserInviteRecordEntity inviter = userId$MallUserInviteRecord.get(mallUserInviteRecordEntity.getInviteUserId());
            recursion(relation + 1, userId$MallUserInviteRecord, mallDistributionRecordEntities, inviter);
        }


    }

    /**
     * 释放奖励
     *
     * @param entity 需要进行分成的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void reward1(MallRewardPreEntity entity) throws Exception {
        MallOrderEntity order = mallOrderService.queryById(entity.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在,订单号:" + entity.getOrderId());
        }
        if (order.getOrderStatus().intValue() != Constant.OrderStatus.QRSH.getValue()) {
            throw new RuntimeException("订单状态不是收货状态,订单号:" + order.getId());
        }
        MallUserEntity user = mallUserService.lambdaQuery().eq(MallUserEntity::getId, order.getUserId()).one();
        if (user == null) {
            throw new RuntimeException("订单用户不存在,订单号:" + order.getId());
        }
        if (StringUtils.isEmpty(user.getInviteUserId())) {
            log.info("订单 {} 无上级邀请人，忽略该订单", order.getId());
            entity.setStatus(1);
            preService.update(entity);
        } else {

            MallUserEntity inviteUser = mallUserService.lambdaQuery().eq(MallUserEntity::getId, user.getInviteUserId()).one();
            if (inviteUser == null) {
                throw new RuntimeException("上级用户不存在,订单号:" + order.getId());
            }
            MallDistEntity userMallDist = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, user.getId()).one();
            userMallDist.setAmountTotal(BigDecimalUtil.add(userMallDist.getAmountTotal(), order.getActualPrice()));
            mallDistService.saveOrUpdate(userMallDist);

            MallDistEntity mallDistEntity = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, inviteUser.getId()).one();
            if (ObjectUtil.isNotEmpty(mallDistEntity)) {
                mallDistEntity.setAmountTotal(BigDecimalUtil.add(mallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(order.getActualPrice(), 0.5)));
                mallDistService.saveOrUpdate(mallDistEntity);
                MallDistEntity twoMallDistEntity = mallDistService.lambdaQuery().eq(MallDistEntity::getUserId, mallDistEntity.getSuperiorId()).one();
                if (ObjectUtil.isNotEmpty(twoMallDistEntity)) {
                    twoMallDistEntity.setAmountTotal(BigDecimalUtil.add(twoMallDistEntity.getAmountTotal(), BigDecimalUtil.multiply(order.getActualPrice(), 0.25)));
                    mallDistService.saveOrUpdate(twoMallDistEntity);
                }
            }

            //判断上级用户是否达到了合伙人标准
            MallUserLevelEntity inviteUserLevel = userLevelService.lambdaQuery().eq(MallUserLevelEntity::getId, inviteUser.getUserLevelId()).one();
            inviteUserLevel.setDirectPushRate(null);
            if (inviteUserLevel == null) {
                throw new RuntimeException("上级用户登记不存在,订单号:" + order.getId());
            }
            if (StringUtils.isEmpty(inviteUserLevel.getDirectPushRate()) || inviteUserLevel.getDirectPushRate().equals("0")) {
                log.info("订单 {} 上级无分成比例，忽略该订单", order.getId());
                entity.setStatus(1);
                preService.update(entity);
            } else {

                BigDecimal rate = new BigDecimal(inviteUserLevel.getDirectPushRate());
                BigDecimal rewardAmount = order.getActualPrice().multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                //奖励到余额
                inviteUser.setBalance(inviteUser.getBalance().add(rewardAmount));
                mallUserService.update(inviteUser);
                //记录账户日志
                MallAccountLogEntity logEntity = new MallAccountLogEntity();
                logEntity.setAddTime(new Date());
                logEntity.setFromType(1);
                logEntity.setOrderSn(order.getOrderSn());
                logEntity.setLogDesc("订单合伙人分成");
                logEntity.setNickname(inviteUser.getNickname());
                logEntity.setUserId(inviteUser.getId());
                logEntity.setPrice(rewardAmount);
                logEntity.setOrderType(7);
                logEntity.setType(1);
                mallAccountLogService.save(logEntity);
                //记录日志
                MallRewardRecordEntity rewardRecord = new MallRewardRecordEntity();
                rewardRecord.setActualPrice(order.getActualPrice());
                rewardRecord.setCreateTime(new Date());
                rewardRecord.setOrderId(order.getId());
                rewardRecord.setOrderSn(order.getOrderSn());
                rewardRecord.setOrderUserId(user.getId());
                rewardRecord.setRewardAmount(rewardAmount);
                rewardRecord.setRewardRate(rate);
                rewardRecord.setUserId(inviteUser.getId());
                rewardRecordService.save(rewardRecord);
                entity.setStatus(1);
                preService.update(entity);
            }
        }
    }


}
