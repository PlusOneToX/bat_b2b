package com.bat.order.api.deliver.dto;

import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import lombok.Data;

/**
 * Created by apple on 2018/5/10.
 */
@Data
public class OrderGoodsDeliverBean {

    private Integer id;
    private Integer count;
    private String warehouseNo;//仓库编码
    private Integer itemId;
    private Integer alreadyNum;

    /**
     * 关联的order_goods对象
     */
    private OrderGoodsDO orderGoodsDO;
}
