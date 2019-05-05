package com.bat.dubboapi.distributor.subaccount.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DistributorSubAccountRatioRpcDTOQry implements Serializable {

    private static final long serialVersionUID = -8975133234995338759L;

    private Integer id;

    private Integer subAccountConfigId;

    private Integer levelId;

    private String levelName;

    private BigDecimal ratio;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubAccountConfigId() {
        return subAccountConfigId;
    }

    public void setSubAccountConfigId(Integer subAccountConfigId) {
        this.subAccountConfigId = subAccountConfigId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}