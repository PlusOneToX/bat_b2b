package com.bat.goods.service.message;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.goods.manager.Tenant.TenantContext;
import com.bat.goods.manager.mq.api.Sink;
import com.bat.goods.manager.mq.dto.GoodsSaleDTO;
import com.bat.goods.service.common.GoodsConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.bat.goods.service.goods.executor.GoodsCmdExe;
import com.bat.goods.service.stock.executor.GoodsStockFlagCmdExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
public class ReceiveService {

    @Resource
    private GoodsCmdExe goodsCmdExe;

    @Resource
    private GoodsStockFlagCmdExe goodsStockFlagCmdExe;

    @Resource
    private GoodsConfig config;

    /**
     * 全量商品价格同步
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncAllGoodsPrice'")
    public void syncAllGoodsPriceInput(@Headers Map headers) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.syncAllGoodsPrice(tenantNo);
        TenantContext.removeTenantNo();
    }

    /**
     * 全量商品货品信息同步
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncAllGoodsItem'")
    public void syncAllGoodsItemInput(@Headers Map headers) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.syncAllGoodsItem(tenantNo);
        TenantContext.removeTenantNo();
    }

    /**
     * 全量商品货品装箱信息同步
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncAllGoodsItemBox'")
    public void syncAllGoodsItemBoxInput(@Headers Map headers) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.syncAllGoodsItemBox(tenantNo);
        TenantContext.removeTenantNo();
    }

    /**
     * 全量商品可视范围刷新同步
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncAllGoodsScope'")
    public void syncAllGoodsScopeInput(@Headers Map headers) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.syncAllGoodsScope(tenantNo);
    }

    /**
     * 根据品牌id刷新商品可视范围刷新同步
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncBrandGoodsScope'")
    public void syncBrandGoodsScopeInput(@Headers Map headers, @Payload Integer brandId) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.syncBrandGoodsScope(tenantNo, brandId);
        TenantContext.removeTenantNo();
    }

    /**
     * 商品订单销售数量更新
     *
     */
    @StreamListener(value = Sink.GOODS_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderGoodsSale'")
    public void orderGoodsSaleInput(@Headers Map headers, @Payload List<GoodsSaleDTO> saleDTOS) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsCmdExe.updateGoodsSaleData(saleDTOS);
        TenantContext.removeTenantNo();
    }

    /**
     * 根据货品ids刷新货品上下架状态
     *
     */
    @StreamListener(value = Sink.GOODS_INPUT, condition = "headers['rocketmq_TAGS'] == 'goodsItemSaleStatus'")
    public void goodsItemSaleStatus(@Headers Map headers, @Payload List<Integer> itemIds) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        goodsStockFlagCmdExe.goodsItemSaleStatus(itemIds);
        TenantContext.removeTenantNo();
    }

}
