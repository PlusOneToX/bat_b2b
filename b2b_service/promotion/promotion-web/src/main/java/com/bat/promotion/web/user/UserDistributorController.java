package com.bat.promotion.web.user;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.web.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.user.UserDistributorServiceI;
import com.bat.promotion.api.user.dto.distributor.UserGroupSeckillListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionListQry;
import com.bat.promotion.api.user.dto.distributor.UserPromotionRuleGoodsListQry;
import com.bat.promotion.api.user.dto.distributor.data.*;
import com.bat.promotion.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/26 17:19
 */
@Api(tags = "分销客户活动前台接口", value = "UserController")
@RestController
@RequestMapping("/promotion/v1/web/user")
public class UserDistributorController extends BaseController {

    @Resource
    private UserDistributorServiceI service;

    @ApiOperation(value = "根据搜索条件查找促销活动列表(分页)")
    @GetMapping(value = "/promotion/list")
    public Response<PageInfo<UserPromotionDTO>> listPromotion(@Valid UserPromotionListQry qry) {
        PageInfo<UserPromotionDTO> pageInfo = service.listPromotion(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据促销活动id查找活动规则列表(不分页)")
    @GetMapping(value = "/promotion/rule/list")
    public Response<List<UserPromotionRuleDTO>> listPromotionRule(@Valid BaseId qry) {
        List<UserPromotionRuleDTO> dtos = service.listPromotionRule(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据促销活动规则id查找货品列表(分页)")
    @GetMapping(value = "/promotion/rule/goods/list")
    public Response<PageInfo<UserPromotionRuleGoodsDTO>>
        listPromotionRuleGoods(@Valid UserPromotionRuleGoodsListQry qry) {
        PageInfo<UserPromotionRuleGoodsDTO> pageInfo = service.listPromotionRuleGoods(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找拼团秒杀商品列表(分页)")
    @GetMapping(value = "/groupseckill/goods/list")
    public Response<PageInfo<UserGroupSeckillGoodsDTO>> listGroupseckillGoods(@Valid UserGroupSeckillListQry qry) {
        PageInfo<UserGroupSeckillGoodsDTO> pageInfo = service.listGroupseckillGoods(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品id查找活动")
    @GetMapping(value = "/goods/promotiongroupseckill")
    public Response<UserGoodsPromotionDTO> promotionGroupSeckillGoods(@Valid BaseId qry) {
        String userId = qry.getDistributorId();
        if(StringUtils.isBlank(userId)){
            userId = getUserId();
        }
        UserGoodsPromotionDTO dto = service.promotionGroupSeckillGoods(qry, userId);
        return Response.of(dto);
    }

    @ApiOperation(value = "获取订单促销活动列表")
    @GetMapping(value = "/order/promotion/list")
    public Response<List<UserPromotionDTO>> listPromotionOrder() {
        List<UserPromotionDTO> dtos = service.listPromotionOrder(getUserId());
        return Response.of(dtos);
    }

    @ApiOperation(value = "根据促销活动条件id获取赠品列表")
    @GetMapping(value = "/order/promotion/present/list")
    public Response<List<UserPromotionRuleConditionPresentDTO>> listConditionPresent(@Valid BaseId qry) {
        List<UserPromotionRuleConditionPresentDTO> dtos = service.listConditionPresent(qry);
        return Response.of(dtos);
    }

}
