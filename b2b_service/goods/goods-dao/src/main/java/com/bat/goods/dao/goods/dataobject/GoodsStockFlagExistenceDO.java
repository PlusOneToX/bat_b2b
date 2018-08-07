package com.bat.goods.dao.goods.dataobject;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/5/7 14:45
 */
@Data
public class GoodsStockFlagExistenceDO {
    // SELECT count(*) as skuCount,goods_id as goodsId,SUM(under_stock_flag) as stockFlagSum FROM goods_stock_flag GROUP
    // BY goods_id

    // 同一商品下所有货品的之和，就是这个商品有多少sku
    private Integer skuCount;

    private Integer goodsId;

    // 同一个商品下所有货品的under_stock_flag字段之和
    private Integer stockFlagSum;
}
