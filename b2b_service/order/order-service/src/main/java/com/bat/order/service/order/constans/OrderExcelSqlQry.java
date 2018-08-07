package com.bat.order.service.order.constans;

import lombok.Data;

@Data
public class OrderExcelSqlQry {

    /**
     * 前面sql
     */
    private String frontStr = "SELECT order_info.id order_id, order_goods.item_id,";

    /**
     * 后面sql（from条件）
     */
    private String endSql;

    public String getFullSql() {
        if (frontStr.endsWith(",")) {
            // 最后是英文逗号结束、需要删除
            frontStr = frontStr.substring(0, frontStr.length() - 1);
        }
        return frontStr + endSql;
    }
}
