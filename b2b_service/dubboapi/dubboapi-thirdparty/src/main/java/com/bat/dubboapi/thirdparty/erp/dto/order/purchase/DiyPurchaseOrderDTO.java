package com.bat.dubboapi.thirdparty.erp.dto.order.purchase;

import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DiyPurchaseOrderDTO implements Serializable {
    private static final long serialVersionUID = -8433078704714606032L;

    private String orderErpNo;

    private String factoryNo;

    private List<OrderGoodsDetailDTO> detailDTOList;

    private String time;

    /**
     * order_delivery_bil
     */
    private Integer id;
}
