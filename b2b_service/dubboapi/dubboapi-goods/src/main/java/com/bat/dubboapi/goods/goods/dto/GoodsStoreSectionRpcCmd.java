package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class GoodsStoreSectionRpcCmd implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 栏目id
     */
    private Integer sectionId;
    /**
     * 栏目商品id
     */
    private Integer goodsId;
    /**
     * 序号排序
     */
    private Integer sort;
    /**
     * 操作类型,1.新增 2.修改 3.删除 4.置顶
     */
    private Short operationType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getOperationType() {
        return operationType;
    }

    public void setOperationType(Short operationType) {
        this.operationType = operationType;
    }
}
