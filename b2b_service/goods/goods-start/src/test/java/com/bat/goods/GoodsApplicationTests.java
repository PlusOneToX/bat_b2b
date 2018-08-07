package com.bat.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.goods.dao.goods.GoodsUserMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.bat.goods.dao.goods.dataobject.UserGoodsListDO;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class GoodsApplicationTests {

    @Resource
    private GoodsUserMapper userMapper;

    @Test
    void contextLoads() {
        String json =
            "{\"collectionFlag\":0,\"brandIds\":[7,14,33,43],\"goodsType\":1,\"goodsIds\":[1828,2384,1868,2170,1573,2445,1580,2414,2443,2081,2047,1980,2078,2412,2425,2169,1998,2359,2439,2339,2358,2328,2175,2026,1975,1976,2268,1823,1666,1694,1822,2421,2361,2420,2190,2188,2426,2424,2422,2423,2212,2220,2230,2229,2228,2219,2207,2202,2079,2256,2056,1815,2271,2269,2270,2449,2173,2172,2032,2018,2017,1974,1973,1737,1712,1704,1703],\"cacheKey\":\"userGoodsList\",\"size\":5,\"page\":1}";
        Map qryMap = JSON.parseObject(json, Map.class);
        List<UserGoodsListDO> userGoodsListDOS = userMapper.listUserGoodsList(qryMap);
        log.info(JSON.toJSONString(userGoodsListDOS));
    }

    /**
     * <h2>根据货品id刷新增的商品id字段值</h2>
     */
    @Test
    // @Transactional
    void updateGoodsStockFlag() {
        // 1、查询全部的货品在库库存是否缺货标记表数据
        List<GoodsStockFlagDO> goodsStockFlagDOS = userMapper.queryGoodsStockFlagAll();

        for (GoodsStockFlagDO goodsStockFlagDO : goodsStockFlagDOS) {
            Integer itemId = goodsStockFlagDO.getItemId();
            // 根据货品id查询该货品所属的商品id
            Integer goodsId = userMapper.queryGoodsIdByItemId(itemId);
            // 根据货品id将商品id插入到表中去
            userMapper.updateGoodsStockFlag(itemId, goodsId);
        }
    }

    /**
     * <h2>删除没有对应商品的货品id</h2>
     */
    @Test
    // @Transactional
    void delGoodsStockFlag() {
        // 1、查询全部的货品在库库存是否缺货标记表数据
        List<GoodsStockFlagDO> goodsStockFlagDOS = userMapper.queryGoodsStockFlagAll();

        List<Integer> delIds = new ArrayList<>();

        for (GoodsStockFlagDO goodsStockFlagDO : goodsStockFlagDOS) {
            Integer itemId = goodsStockFlagDO.getItemId();
            // 根据货品id查询该货品所属的商品id
            Integer goodsId = userMapper.queryGoodsIdByItemId(itemId);

            // 存储没有对应商品id的在库库存表id
            if (ObjectUtils.isEmpty(goodsId)) {
                delIds.add(goodsStockFlagDO.getId());
            }
        }

        if (ObjectUtils.isNotEmpty(delIds) && delIds.size() > 0) {
            userMapper.delGoodsStockFlagByIds(delIds);
        }
    }

}
