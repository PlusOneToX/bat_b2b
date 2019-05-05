package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsGroupSeckillRpcCmd implements Serializable {
    private Integer id;
    private Short groupSeckillType;
    private List<GoodsGroupSeckillGoodsRpcCmd> goods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getGroupSeckillType() {
        return groupSeckillType;
    }

    public void setGroupSeckillType(Short groupSeckillType) {
        this.groupSeckillType = groupSeckillType;
    }

    public List<GoodsGroupSeckillGoodsRpcCmd> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsGroupSeckillGoodsRpcCmd> goods) {
        this.goods = goods;
    }
}
