package com.bat.flexible.api.exchange.dto;

public class DownLoadRequest {


    private String type ="";

    private String importOrderType;

    private Long distributorId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImportOrderType() {
        return importOrderType;
    }

    public void setImportOrderType(String importOrderType) {
        this.importOrderType = importOrderType;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }
}

