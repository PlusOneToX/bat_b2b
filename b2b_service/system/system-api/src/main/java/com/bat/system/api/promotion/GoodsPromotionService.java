package com.bat.system.api.promotion;

import java.text.ParseException;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.promotion.dto.GoodPromotionId;
import com.bat.system.api.promotion.dto.GoodsPromotionCmd;
import com.bat.system.api.promotion.dto.GoodsPromotionCreateCmd;
import com.bat.system.api.promotion.dto.data.GoodsPromotionDTO;

public interface GoodsPromotionService {

    /**
     * <h2>商品推广列表</h2>
     *
     * @return
     */
    PageInfo<GoodsPromotionCmd> goodsPromotionList(GoodsPromotionListQry qry);

    /**
     * <h2>添加商品推广</h2>
     *
     * @param cmd
     */
    void addGoodsPromotion(GoodsPromotionCreateCmd cmd);

    /**
     * <h2>根据商品推广id查询推广信息</h2>
     *
     * @param qry
     * @return
     */
    GoodsPromotionDTO getGoodsPromotion(GoodPromotionId qry);

    /**
     * <h2>根据用户id查询商品推广数据</h2>
     * 
     * @param userId
     * @return
     * @throws ParseException
     */
    GoodsPromotionDTO promotionsquery(Integer userId) throws ParseException;

    /**
     * <h2>修改商品推广</h2>
     * 
     * @param cmd
     */
    void updateGoodsPromotion(GoodsPromotionCreateCmd cmd);

    /**
     * <h2>根据商品推广id删除商品推广记录</h2>
     * 
     * @param cmd
     */
    void deleteGoodsPromotion(GoodsPromotionId cmd);

    /**
     * <h2>商品推广记录作废</h2>
     * 
     * @param cmd
     */
    void invalidGoodsPromotion(GoodsPromotionId cmd);
}
