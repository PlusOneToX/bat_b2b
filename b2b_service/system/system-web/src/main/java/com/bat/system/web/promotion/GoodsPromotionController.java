package com.bat.system.web.promotion;

import java.text.ParseException;

import javax.validation.Valid;

import com.bat.system.api.promotion.GoodsPromotionId;
import com.bat.system.api.promotion.GoodsPromotionListQry;
import com.bat.system.api.promotion.GoodsPromotionService;
import com.bat.system.api.promotion.dto.GoodPromotionId;
import com.bat.system.api.promotion.dto.GoodsPromotionCmd;
import com.bat.system.api.promotion.dto.GoodsPromotionCreateCmd;
import com.bat.system.api.promotion.dto.data.GoodsPromotionDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/26 19:32
 */
@Api(tags = "商店配置-商品推广", value = "GoodsPromotionController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/promotion")
public class GoodsPromotionController extends BaseController {

    @Autowired
    private GoodsPromotionService service;

    @ApiOperation(value = "商品推广列表")
    @GetMapping(value = "/goodsPromotionList")
    public Response<PageInfo<GoodsPromotionCmd>> goodsPromotionList(@Valid GoodsPromotionListQry qry) {
        PageInfo<GoodsPromotionCmd> pageInfo = service.goodsPromotionList(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "添加商品推广")
    @PostMapping(value = "addGoodsPromotion")
    @Transactional(rollbackFor = Exception.class)
    public Response addGoodsPromotion(@RequestBody @Valid GoodsPromotionCreateCmd cmd) {
        service.addGoodsPromotion(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "修改商品推广")
    @PutMapping(value = "updateGoodsPromotion")
    public Response updateGoodsPromotion(@RequestBody @Valid GoodsPromotionCreateCmd cmd) {
        service.updateGoodsPromotion(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "商品推广记录作废")
    @PutMapping(value = "invalidGoodsPromotion")
    public Response invalidGoodsPromotion(@RequestBody @Valid GoodsPromotionId cmd) {
        service.invalidGoodsPromotion(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据商品推广id删除商品推广记录")
    @DeleteMapping("deleteGoodsPromotionById")
    @Transactional(rollbackFor = Exception.class)
    public Response deleteGoods(@RequestBody @Valid GoodsPromotionId cmd) {
        service.deleteGoodsPromotion(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据推广商品id获取推广商品详情")
    @GetMapping(value = "getGoodsPromotionDescById")
    public Response<GoodsPromotionDTO> getGoods(@Valid GoodPromotionId qry) {
        GoodsPromotionDTO dto = service.getGoodsPromotion(qry);
        return Response.of(dto);
    }

    @ApiOperation(value = "根据用户id查询商品推广数据")
    @GetMapping(value = "/getGoodsPromotionByUserId")
    public Response<GoodsPromotionDTO> list() throws ParseException {
        String userId = getUserId();
        if (ObjectUtils.isEmpty(userId)) {
            // userId = "1000";
            return Response.of(null);
        }
        // 根据用户id查询用户用户身份以及该用户是否需要推广
        GoodsPromotionDTO promotionsquery = service.promotionsquery(Integer.parseInt(userId));
        return Response.of(promotionsquery);
    }

}
