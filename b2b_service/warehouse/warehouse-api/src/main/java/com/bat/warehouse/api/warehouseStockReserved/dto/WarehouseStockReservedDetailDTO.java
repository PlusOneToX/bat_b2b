package com.bat.warehouse.api.warehouseStockReserved.dto;

import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseStockReservedDetailDTO {

    /**
     * 预留id
     */
    private Integer id;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 状态 0未处理, 1处理
     */
    private Short status;


    private String remark;


    private List<WarehouseStockReservedDetailDO> detailDOList;
}
