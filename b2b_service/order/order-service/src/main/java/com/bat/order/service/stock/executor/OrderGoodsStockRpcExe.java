package com.bat.order.service.stock.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.order.service.stock.convertor.OrderGoodsStockConvertor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberList;
import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.mq.dto.OrderErpNoLineDTO;

@Component
public class OrderGoodsStockRpcExe {

    @DubboReference(check = false, timeout = 5000)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @Resource
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;

    /**
     * 根据订单解锁明细解锁库存
     *
     */
    public void releaseOrderGoodsStock(List<OrderGoodsStockDO> orderGoodsStockDOS) {
        List<ItemStockLockDTO> dtos = OrderGoodsStockConvertor.toItemStockLockDTOList(orderGoodsStockDOS);
        if (!CollectionUtils.isEmpty(dtos)) {
            Response response = warehouseStockServiceRpc.unLockStock(dtos);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
        }
    }

    /**
     * 根据ERP订单号和行序号列表解锁B2B订单
     *
     * @param lineNumberLists
     */
    public void unLockOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberList> lineNumberLists) {
        if (!CollectionUtils.isEmpty(lineNumberLists)) {
            List<OrderErpNoLineDTO> orderErpNoLineDTOS = OrderGoodsStockConvertor.toOrderErpNoLineDTOS(lineNumberLists);
            orderGoodsStockCmdExe.unLockOrderErpNoLineDTO(orderErpNoLineDTOS);
        }
    }
}
