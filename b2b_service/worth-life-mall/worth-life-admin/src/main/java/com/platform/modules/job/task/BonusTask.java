package com.platform.modules.job.task;

import com.alibaba.fastjson.JSONObject;
import com.platform.common.utils.*;
import com.platform.modules.mall.dto.BalanceDto;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component("bonustask")
public class BonusTask {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    @Autowired
    private MallBonusPoolServiceImpl mallBonusPoolService;
    @Autowired
    private MallBonusPoolDetailServiceImpl mallBonusPoolDetailService;
    @Autowired
    private MallUserIntegralLogServiceImpl mallUserIntegralLogService;
    @Autowired
    private MallUserServiceImpl mallUserService;
    @Autowired
    private MallUserBonusDetailServiceImpl mallUserBonusDetailService;
    @Autowired
    private MallUserIntegralServiceImpl mallUserIntegralService;
    @Autowired
    private JedisUtil jedisUtil;


    @Transactional(rollbackFor = Exception.class)
    public void gainBonus() {


        Date updateTime = new Date();
        List<MallBonusPoolEntity> mallBonusPoolEntities = mallBonusPoolService.lambdaQuery().le(MallBonusPoolEntity::getStartTime, updateTime).eq(MallBonusPoolEntity::getIsGain, 0).orderByAsc(MallBonusPoolEntity::getStartTime).list();
        if (ObjectUtil.isEmpty(mallBonusPoolEntities)) {
            return;
        }

        List<MallUserBonusDetailEntity> mallUserBonusDetailEntities = new ArrayList<>();

        List<MallBonusPoolEntity> saveMallBonusPoolEntitys = new ArrayList<>();
        List<BalanceDto> balanceDtos = new ArrayList<>();

        Map<String, MallUserEntity> userId$MallUserEntity = mallUserService.lambdaQuery().select(MallUserEntity::getId, MallUserEntity::getHeadImgUrl, MallUserEntity::getNickname).list().stream().collect(Collectors.toMap(MallUserEntity::getId, e -> e));

        Integer index = 0;

        for (MallBonusPoolEntity mallBonusPoolEntity : mallBonusPoolEntities) {
            List<MallUserIntegralLogEntity> mallUserIntegralLogEntities = mallUserIntegralLogService.lambdaQuery().ge(MallUserIntegralLogEntity::getCreateTime, mallBonusPoolEntity.getStartTime()).le(MallUserIntegralLogEntity::getCreateTime, mallBonusPoolEntity.getEndTime()).notIn(MallUserIntegralLogEntity::getType, 11).list();
            Map<String, List<MallUserIntegralLogEntity>> userId$MallUserIntegralLog = mallUserIntegralLogEntities.stream().collect(Collectors.groupingBy(MallUserIntegralLogEntity::getUserId));
            ArrayList<MallUserBonusDetailEntity> tampMallUserBonusDetailEntities = new ArrayList<>();
            userId$MallUserIntegralLog.forEach((k, v) -> {
                BigDecimal reduce = v.stream().map(MallUserIntegralLogEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
                MallUserEntity mallUserEntity = userId$MallUserEntity.get(k);
                MallUserBonusDetailEntity mallUserBonusDetailEntity = new MallUserBonusDetailEntity().setRemark("").setHead(mallUserEntity.getHeadImgUrl()).setIntegral(reduce).setUserId(mallUserEntity.getId()).setNikeName(mallUserEntity.getNickname()).setBonusPoolId(mallBonusPoolEntity.getId());
                tampMallUserBonusDetailEntities.add(mallUserBonusDetailEntity);
                mallUserBonusDetailEntities.add(mallUserBonusDetailEntity);
            });
            List<MallBonusPoolDetailEntity> bonusPoolDetailEntities = mallBonusPoolDetailService.lambdaQuery().eq(MallBonusPoolDetailEntity::getBonusPoolId, mallBonusPoolEntity.getId()).orderByAsc(MallBonusPoolDetailEntity::getStartOrder).list();
            List<MallUserBonusDetailEntity> collect = tampMallUserBonusDetailEntities.stream().map(e -> e.setAward(BigDecimal.ZERO)).sorted(Comparator.comparing(MallUserBonusDetailEntity::getIntegral).reversed()).collect(Collectors.toList());
            Integer sort = 1;
            for (MallBonusPoolDetailEntity bonusPoolDetailEntity : bonusPoolDetailEntities) {
                Integer end = 0;
                if (collect.size() < bonusPoolDetailEntity.getEndOrder()) {
                    end = collect.size();
                } else {
                    end = bonusPoolDetailEntity.getEndOrder();
                }
                Integer start = bonusPoolDetailEntity.getStartOrder() - 1;
                if (start > collect.size()) {
                    continue;
                }
                List<MallUserBonusDetailEntity> list = collect.subList(start, end);
                if (ObjectUtil.isNotEmpty(list)) {
                    for (MallUserBonusDetailEntity mallUserBonusDetailEntity : list) {
                        BigDecimal totalAward = BigDecimalUtil.multiply(mallBonusPoolEntity.getAmountTotal(), BigDecimalUtil.percentage(bonusPoolDetailEntity.getAwardRatio()));
                        BigDecimal award = BigDecimalUtil.divide(totalAward, list.size());
                        mallUserBonusDetailEntity.setMallBonusPoolDetailId(bonusPoolDetailEntity.getId()).setRemark(bonusPoolDetailEntity.getAwardRatio().intValue() + "%奖金池奖励").setAward(award).setSort(sort++).setCreateTime(updateTime).setCount(list.size()).setRatio(bonusPoolDetailEntity.getAwardRatio());
                    }
                }
            }
            if (0 == index) {
                jedisUtil.set(Constant.INTEGRAL_ORDER, JSONObject.toJSONString(collect), Integer.MAX_VALUE);
            }
            index++;
            if (mallBonusPoolEntity.getEndTime().before(updateTime)) {
                mallBonusPoolEntity.setIsGain(1);
                saveMallBonusPoolEntitys.add(mallBonusPoolEntity);
            }
            if (mallBonusPoolEntity.getStartTime().after(updateTime)) {
                mallBonusPoolEntity.setStatus(0);
            } else if (mallBonusPoolEntity.getStartTime().before(updateTime) && updateTime.before(mallBonusPoolEntity.getEndTime())) {
                mallBonusPoolEntity.setStatus(1);
            } else {
                mallBonusPoolEntity.setStatus(2);
            }
        }

        mallBonusPoolService.saveOrUpdateBatch(mallBonusPoolEntities);
        if (ObjectUtil.isNotEmpty(saveMallBonusPoolEntitys)) {
            //mallBonusPoolService.saveOrUpdateBatch(saveMallBonusPoolEntitys);
            Set<Integer> ids = saveMallBonusPoolEntitys.stream().map(MallBonusPoolEntity::getId).collect(Collectors.toSet());
            List<MallUserBonusDetailEntity> collect = mallUserBonusDetailEntities.stream().filter(e -> ids.contains(e.getBonusPoolId())).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(collect)) {
                mallUserBonusDetailService.saveBatch(collect);
            }
            for (MallUserBonusDetailEntity mallUserBonusDetailEntity : collect) {
                BalanceDto balanceDto = new BalanceDto().setUserId(mallUserBonusDetailEntity.getUserId()).setUpdateTime(updateTime).setPid(mallUserBonusDetailEntity.getId().toString()).setLabel("排行奖励").setType(1).setBusinessType(10).setAmount(mallUserBonusDetailEntity.getAward()).setRemark(JSONObject.toJSONString(mallUserBonusDetailEntity));
                balanceDtos.add(balanceDto);
            }
        }

        if (ObjectUtil.isNotEmpty(balanceDtos)) {
            for (BalanceDto balanceDto : balanceDtos) {
                executor.submit(() -> mallUserService.exchangeBalance(balanceDto));
            }
        }


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
