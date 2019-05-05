package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class UserGoodsListRpcQry implements Serializable {
    private String content;
    private List<Integer> noGoodsIds;
    private List<Integer> goodsIds;
    private Integer size;
    private Integer page;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getNoGoodsIds() {
        return noGoodsIds;
    }

    public void setNoGoodsIds(List<Integer> noGoodsIds) {
        this.noGoodsIds = noGoodsIds;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(List<Integer> goodsIds) {
        this.goodsIds = goodsIds;
    }
}
