package com.bat.system.api.storesetting.dto;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 12:13
 */
@Data
public class ColumnGoodsItem {

    private Integer id;

    private Integer goodsId;

    private Integer columnId;

    private Integer sort;

    private Short operationType;

}
