package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WarehouseStockInfoRpcDTO implements Serializable {

    private List<WarehouseInStockRpcDTO> warehouseInStockRpcDTOS;

    // private List<GoodsOnWayStockRpcDTO> goodsOnWayStockRpcDTOS;

    private List<GoodsVmiStockRpcDTO> goodsVmiStockRpcDTOS;
}