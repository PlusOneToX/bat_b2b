package com.platform.common.mall;

import com.platform.common.utils.BigDecimalUtil;
import com.platform.common.utils.Constant;
import com.platform.common.utils.ObjectUtil;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserIntegralEntity;
import com.platform.modules.mall.entity.MallUserLevelEntity;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallUserIntegralService;
import com.platform.modules.mall.service.impl.MallUserLevelServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MallUserLevel {

    @Autowired
    private MallUserLevelServiceImpl mallUserLevelConfService;

    @Autowired
    private MallUserIntegralService mallUserIntegralService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallUserDao mallUserDao;

    /**
     * 后台管理查询用户等级的通用接口
     */
    @PostMapping("myLevel")
    @ApiOperation(value = "我的等级", notes = "我的等级")
    public Optional<MallUserLevelEntity> myLevel(String id) {
        MallUserEntity mallUserEntity = mallUserDao.selectById(id);

        List<MallUserLevelEntity> mallUserLevelConfEntities = mallUserLevelConfService.list();
        if (!BigDecimalUtil.equal(0, mallUserEntity.getLevel())) {
            Optional<MallUserLevelEntity> first = mallUserLevelConfEntities.stream().filter(e -> StringUtils.equals(e.getId(), mallUserEntity.getLevel().toString())).findFirst();
            return first;
        }

        Map<String, MallUserIntegralEntity> userId$MallUserIntegralEntity = mallUserIntegralService.list().stream().collect(Collectors.toMap(MallUserIntegralEntity::getUserId, e -> e));
        List<MallOrderEntity> mallOrderEntityList = mallOrderService.lambdaQuery().in(MallOrderEntity::getId, id).eq(MallOrderEntity::getOrderStatus, Constant.OrderStatus.QRSH.getValue()).list();
        Map<String, List<MallOrderEntity>> userId$MallOrderEntityList = mallOrderEntityList.stream().collect(Collectors.groupingBy(MallOrderEntity::getUserId));
        //查询用户下单总金额
        List<MallOrderEntity> mallOrderEntities = userId$MallOrderEntityList.get(id);
        BigDecimal reduce = BigDecimal.ZERO;
        if (ObjectUtil.isNotEmpty(mallOrderEntities)) {
            reduce = mallOrderEntities.stream().map(MallOrderEntity::getActualPrice).reduce(BigDecimal.ZERO, BigDecimalUtil::add);
        }
        //查询用户下单总积分
        MallUserIntegralEntity mallUserIntegralEntity = userId$MallUserIntegralEntity.get(id);
        if (ObjectUtil.isEmpty(mallUserIntegralEntity)) {
            mallUserIntegralEntity = new MallUserIntegralEntity().setAmount(BigDecimal.ZERO);
        }
        BigDecimal finalReduce = reduce;
        MallUserIntegralEntity finalMallUserIntegralEntity = mallUserIntegralEntity;
        Optional<MallUserLevelEntity> first = mallUserLevelConfEntities.stream().filter(e -> !BigDecimalUtil.greater(e.getMoney(), finalReduce) && !BigDecimalUtil.greater(e.getIntegral(), finalMallUserIntegralEntity.getAmount())).sorted(Comparator.comparing(MallUserLevelEntity::getLevelValue).reversed()).findFirst();
        return first;
    }

}
