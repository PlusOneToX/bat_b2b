package com.platform.modules.job.task;

import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserLevelEntity;
import com.platform.modules.mall.service.MallUserLevelService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.impl.MallOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户等级自动升级任务
 */
@Slf4j
@Component("userLevelUpgradeTask")
public class UserLevelUpgradeTask {
    @Autowired
    private MallUserLevelService userLevelService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallOrderServiceImpl mallOrderService;

    @SuppressWarnings(value = "unused")
    public void exect() {
        log.info("用户自动升降级任务开始");
        List<MallUserEntity> users = userService.lambdaQuery().list();
        List<MallUserLevelEntity> levels = userLevelService.lambdaQuery().orderByAsc(MallUserLevelEntity::getMoney).list();
        for (MallUserEntity user : users) {
            try {
                upgrade(user, levels);
            } catch (Exception e) {
                log.error("用户升降级失败", e);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void upgrade(MallUserEntity user, List<MallUserLevelEntity> levels) throws Exception {
        BigDecimal amount = mallOrderService.getBaseMapper().sumUserPayAmount(user.getId());
        for (MallUserLevelEntity level : levels) {
            if (amount.compareTo(new BigDecimal(level.getMoney())) >= 0) {
                //满足条件，直接升级
//                user.setUserLevelId(level.getId());
//                userService.update(user);
                userService.lambdaUpdate().set(MallUserEntity::getUserLevelId, level.getId())
                        .eq(MallUserEntity::getId, user.getId()).update();
                break;
            }
        }
    }
}
