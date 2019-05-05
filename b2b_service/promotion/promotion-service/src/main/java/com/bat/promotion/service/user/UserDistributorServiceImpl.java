package com.bat.promotion.service.user;

import java.util.List;

import javax.annotation.Resource;

import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.service.user.executor.UserDistributorQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.user.UserDistributorServiceI;
import com.bat.promotion.api.user.dto.distributor.UserGroupSeckillListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionRuleGoodsListQry;
import com.bat.promotion.api.user.dto.distributor.data.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:42
 */
@Service
public class UserDistributorServiceImpl implements UserDistributorServiceI {

    @Resource
    private UserDistributorQryExe qryExe;

    @Override
    public PageInfo<UserPromotionDTO> listPromotion(UserPromotionListQry qry, String userId) {
        return qryExe.listPromotion(qry, userId);
    }

    @Override
    public List<UserPromotionRuleDTO> listPromotionRule(BaseId qry) {
        return qryExe.listPromotionRule(qry);
    }

    @Override
    public PageInfo<UserPromotionRuleGoodsDTO> listPromotionRuleGoods(UserPromotionRuleGoodsListQry qry,
                                                                      String userId) {
        return qryExe.listPromotionRuleGoods(qry, userId);
    }

    @Override
    public PageInfo<UserGroupSeckillGoodsDTO> listGroupseckillGoods(UserGroupSeckillListQry qry, String userId) {
        return qryExe.listGroupseckillGoods(qry, userId);
    }

    @Override
    public UserGoodsPromotionDTO promotionGroupSeckillGoods(BaseId qry, String userId) {
        return qryExe.promotionGroupSeckillGoods(qry, userId);
    }

    @Override
    public List<UserPromotionDTO> listPromotionOrder(String userId) {
        return qryExe.listPromotionOrder(userId);
    }

    @Override
    public List<UserPromotionRuleConditionPresentDTO> listConditionPresent(BaseId qry) {
        return qryExe.listConditionPresent(qry);
    }
}
