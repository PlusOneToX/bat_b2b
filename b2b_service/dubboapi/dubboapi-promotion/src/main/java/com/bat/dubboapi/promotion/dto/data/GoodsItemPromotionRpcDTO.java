package com.bat.dubboapi.promotion.dto.data;


import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class GoodsItemPromotionRpcDTO implements Serializable {
    private Integer id;
    private List<PromotionRpcDTO> promotions;
    private List<GoodsItemGroupSeckillRpcDTO> groupSeckills;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PromotionRpcDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionRpcDTO> promotions) {
        this.promotions = promotions;
    }

    public List<GoodsItemGroupSeckillRpcDTO> getGroupSeckills() {
        return groupSeckills;
    }

    public void setGroupSeckills(List<GoodsItemGroupSeckillRpcDTO> groupSeckills) {
        this.groupSeckills = groupSeckills;
    }
}
