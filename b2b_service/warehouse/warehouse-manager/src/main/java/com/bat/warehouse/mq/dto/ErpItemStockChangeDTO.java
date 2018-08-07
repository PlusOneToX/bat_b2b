package com.bat.warehouse.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by apple on 2018/7/15.
 */
@Data
public class ErpItemStockChangeDTO implements Serializable {

    private static final long serialVersionUID = 8349701167159507421L;

    /**
     * 货品编码 80码
     */
    private String itemNo;

    /**
     * 仓库编码
     */
    private String warehouseNo;

    /**
     * 变更数量 changType为2时、负数
     */
    private Integer num;

    /**
     * 类型 1、增加库存 2、减少库存 （根据num 手动设置）
     */
    private Short changeType;

    /**
     * ERP订单号
     */
    private String orderErpNo;

    /***
     * 行序号、销售单明细内码
     */
    private Integer lineNumberId;

    /**
     * 本单单据内码
     */
    private Integer entryId;

    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 库存单据类型
     */
    private String stockBillType;
}
