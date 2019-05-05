package com.bat.dubboapi.thirdparty.erp.dto.goods;

import java.io.Serializable;
import java.util.List;

/**
 * erp接口请求参数
 */
public class GoodsItemErpListRpcQry implements Serializable {

    /**
     * 搜索条件
     */
    private String content;
    /**
     * 每页大小
     */
    private Integer size = 20;
    /**
     * 页码
     */
    private Integer page = 1;
    /**
     * 销售组织
     */
    private String orgId;
    /**
     * 价格等级erp对应列表
     */
    private List<ErpPriceFieldListRpcQry> erpPriceFields;

    public List<ErpPriceFieldListRpcQry> getErpPriceFields() {
        return erpPriceFields;
    }

    public void setErpPriceFields(List<ErpPriceFieldListRpcQry> erpPriceFields) {
        this.erpPriceFields = erpPriceFields;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
