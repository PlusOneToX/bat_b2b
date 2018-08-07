package com.bat.goods.api.message;

import java.util.Date;
import java.util.List;

public interface MessageSendServiceI {
    void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time);

    /**
     * 全量同步商品价格消息
     */
    void syncAllGoodsPrice();

    /**
     * 全量同步商品信息消息
     */
    void syncAllGoodsItem();

    /**
     * 发送全量商品信息同步
     */
    void syncAllGoodsItemBox();

    /**
     * 发送全量商品可视范围同步
     */
    void syncAllGoodsScope();

    /**
     * 发送根据品牌id刷新商品可视范围刷新同步
     */
    void syncBrandGoodsScope(Integer brandId);

    /**
     * 发送货品上下架消息
     * 
     * @param itemIds
     */
    void goodsItemSaleStatus(List<Integer> itemIds);
}
