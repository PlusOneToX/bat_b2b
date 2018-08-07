package com.bat.order.service.stock;

import java.util.List;

import com.bat.order.service.stock.executor.OrderGoodsStockCmdExe;
import com.bat.order.service.stock.executor.OrderGoodsStockQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.order.api.stock.api.OrderGoodsStockServiceI;
import com.bat.order.api.stock.dto.OrderGoodsStockQry;
import com.bat.order.api.stock.dto.OrderGoodsStockQryDTO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;

@Service
public class OrderGoodsStockServiceImpl implements OrderGoodsStockServiceI {

    @Autowired
    private OrderGoodsStockQryExe orderGoodsStockQryExe;

    @Autowired
    private OrderGoodsStockCmdExe orderGoodsStockCmdExe;

    @Override
    public List<OrderGoodsStockQryDTO> listDTOByCondition(OrderGoodsStockQry orderGoodsStockQry) {

        return orderGoodsStockQryExe.listDTOByCondition(orderGoodsStockQry);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchCreate(List<OrderGoodsStockDO> createList) {
        orderGoodsStockCmdExe.batchCreate(createList);
    }

    /**
     *
     * @param orderIdList
     * @return
     */
    @Override
    public List<OrderGoodsStockDO> listByOrderIdList(List<Integer> orderIdList) {
        return orderGoodsStockQryExe.listByOrderIdList(orderIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdate(List<OrderGoodsStockDO> updateGoodsStockList) {
        orderGoodsStockCmdExe.batchUpdate(updateGoodsStockList);
    }
}
