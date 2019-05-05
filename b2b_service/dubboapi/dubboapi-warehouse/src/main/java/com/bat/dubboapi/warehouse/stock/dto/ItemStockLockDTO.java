package com.bat.dubboapi.warehouse.stock.dto;

import java.io.Serializable;
import java.util.List;

public class ItemStockLockDTO implements Serializable {

    private static final long serialVersionUID = 2812194176567858925L;
    /**
     * 锁库的货品id
     */
    private Integer itemId;

    /**
     * VMI锁定
     */
    private VminStockLockDTO vmiLock;

    /**
     * 在途锁定(别的模块调库存（或者库存调别的模块） 在库在途可能是分开的，但是在库存内部处理，都是用的下面的（在库库存锁定列表）有个合并的过程)
     */
    private List<WarehouseInStockLockDTO> onWayLockDTOList;
    // private OnWayStockLockDTO onWayLock;

    /**
     * 在库库存锁定列表
     */
    private List<WarehouseInStockLockDTO> inStockLockDTOList;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public VminStockLockDTO getVmiLock() {
        return vmiLock;
    }

    public void setVmiLock(VminStockLockDTO vmiLock) {
        this.vmiLock = vmiLock;
    }

    // public OnWayStockLockDTO getOnWayLock() {
    // return onWayLock;
    // }
    //
    // public void setOnWayLock(OnWayStockLockDTO onWayLock) {
    // this.onWayLock = onWayLock;
    // }

    public List<WarehouseInStockLockDTO> getOnWayLockDTOList() {
        return onWayLockDTOList;
    }

    public void setOnWayLockDTOList(List<WarehouseInStockLockDTO> onWayLockDTOList) {
        this.onWayLockDTOList = onWayLockDTOList;
    }

    public List<WarehouseInStockLockDTO> getInStockLockDTOList() {
        return inStockLockDTOList;
    }

    public void setInStockLockDTOList(List<WarehouseInStockLockDTO> inStockLockDTOList) {
        this.inStockLockDTOList = inStockLockDTOList;
    }
}
