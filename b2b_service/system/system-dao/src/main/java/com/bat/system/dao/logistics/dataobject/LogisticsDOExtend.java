package com.bat.system.dao.logistics.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class LogisticsDOExtend extends LogisticsDO {
    private Integer subId;

    private Integer logisticsId;

    private Double firstWeightCost;

    private Double firstVolumeCost;

    private Double additionalWeightCost;

    private Double additionalVolumeCost;

    private Short defaultFlag;

    private Short formulaFlag;

    private String formula;

    private Date createTime;

    private Date updateTime;

    private String groupId;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    @Override
    public String toString() {
        return "LogisticsDO1{" + "subId=" + subId + ", logisticsId=" + logisticsId + ", firstWeightCost="
            + firstWeightCost + ", firstVolumeCost=" + firstVolumeCost + ", additionalWeightCost="
            + additionalWeightCost + ", additionalVolumeCost=" + additionalVolumeCost + ", defaultFlag=" + defaultFlag
            + ", formulaFlag=" + formulaFlag + ", formula='" + formula + '\'' + ", createTime=" + createTime
            + ", updateTime=" + updateTime + ", groupId='" + groupId + '\'' + ", countryId=" + countryId
            + ", provinceId=" + provinceId + ", cityId=" + cityId + ", districtId=" + districtId + '}';
    }
}