package com.bat.warehouse.manager.common.constant;

/**
 * 仓库模块 redis key
 */
public class WarehouseKeyConstant {

    /**
     * 在途库存、根据item_id
     */
    public static final String GOODS_ON_WAY_DO_PRE = "WAREHOUSE:GOODS_ON_WAY_STOCK:";

    /**
     * VMI库存、根据item_id
     */
    public static final String GOODS_VMI_STOCK_DO_PRE = "WAREHOUSE:GOODS_VMI_STOCK:";


    /**
     * 在库库存、根据仓库id和item_id存储（包含在途）
     */
    public static final String WAREHOUSE_IN_STOCK_ITEM_WAREHOUSE_ID_PRE = "WAREHOUSE:WAREHOUSE_IN_STOCK_ITEM:";

    /**
     * 在库库存、根据仓库编码和item_id存储 (锁库分布式key)
     */
    public static final String WAREHOUSE_IN_STOCK_LOCK_KEY_PRE = "WAREHOUSE:WAREHOUSE_IN_STOCK_LOCK:";

    /**
     * VMI库存、根据item_id存储 (锁库分布式key)
     */
    public static final String WAREHOUSE_VMI_STOCK_LOCK_KEY_PRE = "WAREHOUSE:WAREHOUSE_VMI_STOCK_LOCK:";

    /**
     * 在途库存、根据item_id存储 (锁库分布式key)（放弃）
     */
    public static final String WAREHOUSE_ON_WAY_STOCK_LOCK_KEY_PRE = "WAREHOUSE:WAREHOUSE_ON_WAY_STOCK_LOCK:";

    /**
     * 同步ERP库存分布式锁
     */
    public static final String WAREHOUSE_SYNC_ERP_STOCK_KEY_PRE="WAREHOUSE:WAREHOUSE_SYNC_ERP_STOCK_KEY_PRE";
}
