package com.bat.warehouse.manager.reserved.convertor;

import com.bat.warehouse.api.base.util.BeanUtils;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedDetailDTO;
import com.bat.warehouse.dao.stockReserved.dataobject.WarehouseStockReservedDO;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseStockReservedConvertor {


    public static WarehouseStockReservedDetailDTO toWarehouseStockReservedDetailDTO(WarehouseStockReservedDO warehouseStockReservedDO){
        WarehouseStockReservedDetailDTO reservedDetailDTO  =BeanUtils.copy(warehouseStockReservedDO,WarehouseStockReservedDetailDTO.class);
        return reservedDetailDTO;
    }

    /**
     *
     * @param detailDOList
     * @param isAdd 是否新增 true 是 false 否
     * @return
     */
    public static Map<Integer, Integer> toItemReservedCountMap(List<WarehouseStockReservedDetailDO> detailDOList,Boolean isAdd) {
        if(detailDOList ==null || detailDOList.size() ==0){
            return null;
        }
        Map<Integer, Integer> itemReservedMap = new HashMap<>();
        detailDOList.stream().forEach(warehouseStockReservedDetailDO -> {
            Integer count = itemReservedMap.get(warehouseStockReservedDetailDO.getItemId());
            if(count ==null){
                count=0;
            }
            if(isAdd){
                //新增预留
                count+= warehouseStockReservedDetailDO.getNumReserved();
            }else{
                //释放预留
                count-= warehouseStockReservedDetailDO.getNumReserved();
            }

            itemReservedMap.put(warehouseStockReservedDetailDO.getItemId(),count);
        });
        return itemReservedMap;
    }
}
