package com.bat.promotion.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.user.dto.distributor.UserGroupSeckillListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionRuleGoodsListQry;
import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.api.user.dto.distributor.data.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface UserDistributorServiceI {
    /**
     * 根据搜索条件查找促销活动列表(分页)
     * 
     * @param qry
     * @param userId
     * @return
     */
    PageInfo<UserPromotionDTO> listPromotion(UserPromotionListQry qry, String userId);

    /**
     * 根据搜索条件查找促销活动id查找活动规则列表(分页)
     * 
     * @param qry
     * @return
     */
    List<UserPromotionRuleDTO> listPromotionRule(BaseId qry);

    /**
     * 根据促销活动规则id查找货品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<UserPromotionRuleGoodsDTO> listPromotionRuleGoods(UserPromotionRuleGoodsListQry qry, String userId);

    /**
     * 根据搜索条件查找拼团秒杀商品列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<UserGroupSeckillGoodsDTO> listGroupseckillGoods(UserGroupSeckillListQry qry, String userId);

    /**
     * 根据商品id查找活动
     * 
     * @param qry
     * @param userId
     * @return
     */
    UserGoodsPromotionDTO promotionGroupSeckillGoods(BaseId qry, String userId);

    /**
     * 获取订单促销活动列表
     * 
     * @param userId
     * @return
     */
    List<UserPromotionDTO> listPromotionOrder(String userId);

    /**
     * 根据促销活动条件id获取赠品列表
     * 
     * @param qry
     * @return
     */
    List<UserPromotionRuleConditionPresentDTO> listConditionPresent(BaseId qry);
}
