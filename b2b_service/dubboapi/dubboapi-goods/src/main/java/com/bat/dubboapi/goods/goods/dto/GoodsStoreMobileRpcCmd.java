package com.bat.dubboapi.goods.goods.dto;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class GoodsStoreMobileRpcCmd implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 移动端配置id
     */
    private Integer mobileId;
    /**
     * 移动端商品id
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

    /**
     * 模块类型 3商品推广模块 4商品列表模块
     */
    private Short moduleType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMobileId() {
        return mobileId;
    }

    public void setMobileId(Integer mobileId) {
        this.mobileId = mobileId;
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

    public Short getModuleType() {
        return moduleType;
    }

    public void setModuleType(Short moduleType) {
        this.moduleType = moduleType;
    }
}
