package com.bat.order.service.order.dto;

import java.util.List;

import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;

import lombok.Data;

@Data
public class OrderLockStockQry {

    /**
     * 锁库列表
     */
    private List<ItemStockLockDTO> itemStockLockDTOS;
}
