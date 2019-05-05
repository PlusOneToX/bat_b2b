package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
public class PromotionRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String nameEn;
    private String promoDesc;
    private Date startTime;
    private Date endTime;
    private Short promoStatus;
    private Short promoType;
    private Short onWayFlag;
    private List<PromotionRuleRpcDTO> rules;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getPromoDesc() {
        return promoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        this.promoDesc = promoDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Short promoStatus) {
        this.promoStatus = promoStatus;
    }

    public Short getPromoType() {
        return promoType;
    }

    public void setPromoType(Short promoType) {
        this.promoType = promoType;
    }

    public Short getOnWayFlag() {
        return onWayFlag;
    }

    public void setOnWayFlag(Short onWayFlag) {
        this.onWayFlag = onWayFlag;
    }

    public List<PromotionRuleRpcDTO> getRules() {
        return rules;
    }

    public void setRules(List<PromotionRuleRpcDTO> rules) {
        this.rules = rules;
    }
}
