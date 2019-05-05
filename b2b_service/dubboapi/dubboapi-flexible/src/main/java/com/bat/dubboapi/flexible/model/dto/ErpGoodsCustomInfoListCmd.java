package com.bat.dubboapi.flexible.model.dto;

import java.io.Serializable;
import java.util.List;

public class ErpGoodsCustomInfoListCmd implements Serializable {


    private static final long serialVersionUID = 4842886545609009156L;

    private List<ErpGoodsCustomInfoBomCmd> bomCodeList;

    /**
     * 备注
     */
    private String remark;


    private String operateUserName;

    public List<ErpGoodsCustomInfoBomCmd> getBomCodeList() {
        return bomCodeList;
    }

    public void setBomCodeList(List<ErpGoodsCustomInfoBomCmd> bomCodeList) {
        this.bomCodeList = bomCodeList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }
}
