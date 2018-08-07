package com.bat.warehouse.manager.message;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.warehouse.manager.common.config.WarehouseConfig;
import com.bat.warehouse.manager.inStock.executor.WarehouseInStockCmdExe;
import com.bat.warehouse.manager.inStock.executor.WarehouseStockSyncCmdExe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.inStock.dto.GoodsItemStockFlagCmd;
import com.bat.warehouse.mq.api.Sink;
import com.bat.warehouse.mq.dto.ErpItemStockChangeDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:54
 */
@Service
public class ReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveService.class);

    @Autowired
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @Autowired
    private WarehouseInStockServiceI inStockServiceI;

    @Resource
    private WarehouseStockSyncCmdExe warehouseStockSyncCmdExe;

    @Resource
    private WarehouseInStockCmdExe warehouseInStockCmdExe;

    @Resource
    private WarehouseConfig config;

    /**
     * 解锁库存
     * 
     * @param itemStockLockDTOList
     */
    @StreamListener(value = Sink.WAREHOUSE_ORDER_INPUT, condition = "headers['rocketmq_TAGS'] == 'orderUnLockStock'")
    public void orderUnLockStock(@Headers Map headers, @Payload List<ItemStockLockDTO> itemStockLockDTOList) {
        LOGGER.info("消费消息、订单库存解锁：{}", JSON.toJSONString(itemStockLockDTOList));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        warehouseStockServiceRpc.unLockStock(itemStockLockDTOList);
        TenantContext.removeTenantNo();
    }

    /**
     * ERP同步库存到B2B
     * 
     * @param time
     */
    @StreamListener(value = Sink.WAREHOUSE_THIRD_INPUT, condition = "headers['rocketmq_TAGS'] == 'syncErpStockToB2B'")
    public void syncErpStockToB2B2(@Headers Map headers, @Payload Long time) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        LOGGER.info("消费消息、ERP同步库存到B2B异步、开启线程：{}", time);
        TenantContext.setTenantNo(tenantNo);
        inStockServiceI.syncStock(tenantNo);
        TenantContext.removeTenantNo();
    }

    /**
     * 修改货品是否缺货标记
     */
    @StreamListener(value = Sink.WAREHOUSE_INPUT,
        condition = "headers['rocketmq_TAGS'] == 'dealwithGoodsUnderStockFlag'")
    public void dealwithGoodsUnderStockFlag(@Headers Map headers,
        @Payload GoodsItemStockFlagCmd goodsItemStockFlagCmd) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        inStockServiceI.dealwithGoodsUnderStockFlag(tenantNo, goodsItemStockFlagCmd);
        TenantContext.removeTenantNo();
    }

    /**
     * 修改货品是否缺货标记
     */
    @StreamListener(value = Sink.WAREHOUSE_INPUT, condition = "headers['rocketmq_TAGS'] == 'freshItemUnderStockFlag'")
    public void freshItemUnderStockFlag(@Headers Map headers, @Payload List<Integer> itemIds) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        warehouseInStockCmdExe.freshItemUnderStockFlag(tenantNo, itemIds);
        TenantContext.removeTenantNo();
    }

    /**
     * 修改货品是否缺货标记
     */
    @StreamListener(value = Sink.WAREHOUSE_INPUT, condition = "headers['rocketmq_TAGS'] == 'initGoodsStock'")
    public void initGoodsStock(@Headers Map headers, @Payload GoodsMsgRpcDTO goodsMsgRpcDTO) {
        LOGGER.info("消费消息、初始化库存{}", JSON.toJSONString(goodsMsgRpcDTO));
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        warehouseStockSyncCmdExe.initGoodsStock(tenantNo, goodsMsgRpcDTO);
        TenantContext.removeTenantNo();
    }

    /**
     * 变更货品库存
     */
    @StreamListener(value = Sink.WAREHOUSE_INPUT, condition = "headers['rocketmq_TAGS'] == 'changeGoodsStock'")
    public void changeGoodsStock(@Headers Map headers, @Payload List<ErpItemStockChangeDTO> stockChangeDTOS) {
        String tenantNo = (String)headers.get("tenantNo");
        if (StringUtils.isBlank(tenantNo)) {
            tenantNo = config.getDefaultTenantNo();
        }
        TenantContext.setTenantNo(tenantNo);
        warehouseStockSyncCmdExe.changeGoodsStockByErp(tenantNo, stockChangeDTOS);
        TenantContext.removeTenantNo();
    }
}
