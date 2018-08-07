package com.bat.warehouse.manager.inStock.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyWarehouseServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInventoryQry;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.warehouseSetting.dataobject.WarehouseSettingDO;
import com.bat.warehouse.manager.common.config.ConfigQry;
import com.bat.warehouse.manager.warehouseSetting.executor.WarehouseSettingQryExe;

/**
 * ERP
 */
@Component
public class ErpStockQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpStockQryExe.class);

    @DubboReference(check = false, retries = 0, timeout = 600000)
    private ThirdPartyWarehouseServiceErpRpc thirdPartyWarehouseServiceErpRpc;

    @Resource
    private WarehouseSettingQryExe warehouseSettingQryExe;

    @Resource
    private ConfigQry vmiConfigExe;

    public List<InquiryInfo> queryErpStock(String warehouseNo, List<Long> itemErpIdList, Integer size) {
        InquiryInventoryQry inquiryInventoryQry = new InquiryInventoryQry();
        inquiryInventoryQry.setWarehouseNo(warehouseNo);
        inquiryInventoryQry.setDatetime(getDeliveryTime());
        inquiryInventoryQry.setPage(1);
        inquiryInventoryQry.setItemErpIdList(itemErpIdList);
        inquiryInventoryQry.setSize(size);
        List<InquiryInfo> list = new ArrayList<>();
        com.bat.dubboapi.thirdparty.common.Response response =
            thirdPartyWarehouseServiceErpRpc.syncStockFromERP(inquiryInventoryQry);
        if (response == null || !response.isSuccess()) {
            throw new WarehouseException(response.getErrMessage());
        }
        List<InquiryInfo> infoList = (List<InquiryInfo>)response.getData();
        if (!CollectionUtils.isEmpty(infoList)) {
            list.addAll(infoList);
        }
        LOGGER.info("查询仓库编码{}回来的库存列表共有：{}条", warehouseNo, list.size());
        return list;
    }

    /**
     * 获取交货时间（交期时间）
     * 
     * @return
     */
    private int getDeliveryTime() {
        List<WarehouseSettingDO> data = (List<WarehouseSettingDO>)warehouseSettingQryExe.listAll().getData();
        // 交期时间设置
        WarehouseSettingDO warehouseSettingDO1 = data.stream()
            .filter(warehouseSettingDO -> warehouseSettingDO.getType().equals((short)2)).findFirst().orElse(null);
        if (warehouseSettingDO1 != null) {
            return warehouseSettingDO1.getValue();
        } else {
            return 60;
        }
    }
}
