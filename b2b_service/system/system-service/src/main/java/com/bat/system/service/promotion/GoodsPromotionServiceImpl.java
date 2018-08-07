package com.bat.system.service.promotion;

import com.bat.system.api.promotion.GoodsPromotionId;
import com.bat.system.api.promotion.GoodsPromotionListQry;
import com.bat.system.api.promotion.GoodsPromotionService;
import com.bat.system.api.promotion.dto.GoodPromotionId;
import com.bat.system.api.promotion.dto.GoodsPromotionCmd;
import com.bat.system.api.promotion.dto.GoodsPromotionCreateCmd;
import com.bat.system.api.promotion.dto.data.GoodsPromotionDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageInfo;
import com.bat.system.service.promotion.executor.GoodsPromotionCmdExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/26 19:54
 */
@DubboService
@Slf4j
public class GoodsPromotionServiceImpl implements GoodsPromotionService {

    @Autowired
    private GoodsPromotionCmdExc goodsPromotionCmdExc;

    @Override
    public PageInfo<GoodsPromotionCmd> goodsPromotionList(GoodsPromotionListQry qry) {
        return goodsPromotionCmdExc.goodsPromotionList(qry);
    }

    @Override
    public void addGoodsPromotion(GoodsPromotionCreateCmd cmd) {
        goodsPromotionCmdExc.addGoodsPromotion(cmd);
    }

    @Override
    public GoodsPromotionDTO getGoodsPromotion(GoodPromotionId qry) {
        return goodsPromotionCmdExc.getGoodsPromotion(qry);
    }

    @Override
    public GoodsPromotionDTO promotionsquery(Integer userId) {
        return goodsPromotionCmdExc.promotionsquery(userId);
    }

    @Override
    public void updateGoodsPromotion(GoodsPromotionCreateCmd cmd) {
        goodsPromotionCmdExc.updateGoodsPromotion(cmd);
    }

    @Override
    public void deleteGoodsPromotion(GoodsPromotionId cmd) {
        goodsPromotionCmdExc.deleteGoodsPromotion(cmd);
    }

    @Override
    public void invalidGoodsPromotion(GoodsPromotionId cmd) {
        goodsPromotionCmdExc.invalidGoodsPromotion(cmd);
    }
}
