package com.bat.thirdparty.erp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillQueryRequest {
	

/*1.data：JSON格式数据（详情参考JSON格式数据）（必录）
     1.1.FormId：业务对象表单Id（必录）
     1.2.FieldKeys：需查询的字段key集合，字符串类型，格式："key1,key2,..." （必录） 注（查询单据体内码,需加单据体Key和下划线,如：FEntryKey_FEntryId）
     1.3.FilterString：过滤条件，字符串类型（非必录）
     1.4.OrderString：排序字段，字符串类型（非必录）
     1.5.TopRowCount：返回总行数，整型（非必录）
     1.6.StartRow：开始行索引，整型（非必录）
     1.7.Limit：最大行数，整型，不能超过2000（非必录）*/

    @JsonProperty("FormId")
    private String FormId = "";

    @JsonProperty("FieldKeys")
    private String FieldKeys = "";

    @JsonProperty("FilterString")
    private String FilterString = "";

    @JsonProperty("OrderString")
    private String OrderString = "";

    @JsonProperty("TopRowCount")
    private int TopRowCount = 0;

    @JsonProperty("StartRow")
    private int StartRow = 0;

    @JsonProperty("Limit")
    private int Limit = 0;

    public String getFormId() {
        return FormId;
    }

    public void setFormId(String formId) {
        FormId = formId;
    }

    public String getFieldKeys() {
        return FieldKeys;
    }

    public void setFieldKeys(String fieldKeys) {
        FieldKeys = fieldKeys;
    }

    public String getFilterString() {
        return FilterString;
    }

    public void setFilterString(String filterString) {
        FilterString = filterString;
    }

    public String getOrderString() {
        return OrderString;
    }

    public void setOrderString(String orderString) {
        OrderString = orderString;
    }

    public int getTopRowCount() {
        return TopRowCount;
    }

    public void setTopRowCount(int topRowCount) {
        TopRowCount = topRowCount;
    }

    public int getStartRow() {
        return StartRow;
    }

    public void setStartRow(int startRow) {
        StartRow = startRow;
    }

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
