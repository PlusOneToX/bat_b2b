package com.bat.thirdparty.erp.api.request;

public class CategoryAllRequest {
    private String FormId = "BD_MATERIALCATEGORY";
    private String FieldKeys = "";
    private String FilterString = "";
    private String OrderString = "";
    private String TopRowCount = "0";
    private String StartRow = "0";
    private String Limit = "2000";

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

    public String getTopRowCount() {
        return TopRowCount;
    }

    public void setTopRowCount(String topRowCount) {
        TopRowCount = topRowCount;
    }

    public String getStartRow() {
        return StartRow;
    }

    public void setStartRow(String startRow) {
        StartRow = startRow;
    }

    public String getLimit() {
        return Limit;
    }

    public void setLimit(String limit) {
        Limit = limit;
    }
}
