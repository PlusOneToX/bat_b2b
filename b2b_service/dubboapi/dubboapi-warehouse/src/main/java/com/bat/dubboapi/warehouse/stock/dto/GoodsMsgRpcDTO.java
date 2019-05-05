package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsMsgRpcDTO implements Serializable {

    private static final long serialVersionUID = -937156072398341876L;
    /**
     * 商品id
     */
    private Integer goodsId;


    /**
     * 商品所属的货品列表
     */
    private List<GoodsItemErpDTO> itemErpDTOList;
}
