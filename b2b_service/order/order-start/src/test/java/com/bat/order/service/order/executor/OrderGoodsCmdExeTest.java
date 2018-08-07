package com.bat.order.service.order.executor;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeCmd;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.mq.dto.GoodsSaleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class OrderGoodsCmdExeTest {

    @Resource
    private OrderGoodsCmdExe orderGoodsCmdExe;

    @Test
    void orderChangeByErp() {
        String json1 =
            "";
        ErpOrderChangeCmd cmd = JSON.parseObject(json1, ErpOrderChangeCmd.class);
        String json2 =
            "";
        OrderInfoDO order = JSON.parseObject(json2, OrderInfoDO.class);
        String json3 =
            "";
        DistributorRpcDTO distributor = JSON.parseObject(json3, DistributorRpcDTO.class);
        String json4 =
            "";
        OrderDistributorDataDO erpDistributorData = JSON.parseObject(json4, OrderDistributorDataDO.class);
        List<ItemStockLockDTO> unLockDTOS = new ArrayList<>();
        List<ItemStockLockDTO> lockDTOS = new ArrayList<>();
        Map<String, List<Object>> changeMap = new HashMap<>();
        List<GoodsSaleDTO> saleDTOS = new ArrayList<>();
        orderGoodsCmdExe.orderChangeByErp(cmd, order, distributor, erpDistributorData, unLockDTOS, lockDTOS, changeMap,
            saleDTOS);
    }
}