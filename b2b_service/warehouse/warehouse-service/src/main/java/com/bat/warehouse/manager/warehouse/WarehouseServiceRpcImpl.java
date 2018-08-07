package com.bat.warehouse.manager.warehouse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import com.bat.warehouse.manager.common.error.WarehouseErrorCode;
import com.bat.warehouse.manager.warehouse.executor.WarehouseQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.BeanUtils;
import com.bat.warehouse.api.base.util.MessageUtils;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;

@DubboService
public class WarehouseServiceRpcImpl implements WarehouseServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceRpcImpl.class);

    @Autowired
    private WarehouseQryExe warehouseQryExe;

    @Autowired
    private WarehouseInStockServiceI warehouseInStockServiceI;

    @Override
    public Response<WarehouseDTORpcQry> getByIdOrWarehouseNo(Integer id, String warehouseNo) {
        if (id == null && StringUtils.isBlank(warehouseNo)) {
            throw new WarehouseException(WarehouseErrorCode.W_ID_AND_WAREHOUSE_NO_ALL_NULL);
        }
        WarehouseDO warehouseDO = null;
        try {
            if (id != null) {
                warehouseDO = warehouseQryExe.getById(id);
            } else {
                warehouseDO = warehouseQryExe.getByWarehouseNo(warehouseNo);
            }
        } catch (WarehouseException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), e.getMessage());
        }
        WarehouseDTORpcQry warehouseDTORpcQry = BeanUtils.copy(warehouseDO, WarehouseDTORpcQry.class);
        return Response.of(warehouseDTORpcQry);
    }

    @Override
    public void test(Integer index) {
        LOGGER.info(index + "开始处理:" + new Date());
        warehouseInStockServiceI.testLock(index);
    }

    @Override
    public byte[] testInputStream() {
        try {
            File file = new File("D://test.xls");
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = fileInputStream.read(buff, 0, 100)) > 0) {
                byteArrayOutputStream.write(buff, 0, rc);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 根据销售区域id列表查询仓库id列表
     * 
     * @param salesAreaIds
     * @return
     */
    @Override
    public Response<List<WarehouseDTORpcQry>> listByAreaIdList(List<Integer> salesAreaIds) {
        if (salesAreaIds == null || salesAreaIds.size() == 0) {
            return Response.buildFailure(WarehouseErrorCode.W_AREA_ID_LIST_NULL,
                MessageUtils.get(WarehouseErrorCode.W_AREA_ID_LIST_NULL));
        }
        List<WarehouseDO> warehouseDOList = warehouseQryExe.listByAreaIdListAndOpenFlagAndSyncType(salesAreaIds,
            WarehouseCommonConstant.COMMON_OPEN_FLAG_YES, null);
        return Response.of(BeanUtils.copyList(warehouseDOList, WarehouseDTORpcQry.class));
    }
}
