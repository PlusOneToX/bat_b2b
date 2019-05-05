package com.bat.thirdparty.xxljob.service.executor;

import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseJobExe {

    @DubboReference(check = false,timeout = 500000,retries = 0)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @Autowired
    private MessageSendService messageSendService;

    public void stockRedisSyncMysqlJobHandler() {
        warehouseStockServiceRpc.stockRedisSyncMysqlJobHandler();
    }

    /**
     * ERP同步库存到B2B
     */
    public void stockErpSyncB2BJobHandler() {
        messageSendService.syncErpStockToB2B();
    }
}
