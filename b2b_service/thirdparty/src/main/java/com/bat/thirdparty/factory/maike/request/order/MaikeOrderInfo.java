package com.bat.thirdparty.factory.maike.request.order;

import java.util.List;

public class MaikeOrderInfo {
    
    /**
     * 订单备注
     */
    private String remark="";


    /**
     * 订单类型
     */
    private String third_party_type;


    private List<MaikeDetailInfo> detail_info_list;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getThird_party_type() {
        return third_party_type;
    }

    public void setThird_party_type(String third_party_type) {
        this.third_party_type = third_party_type;
    }

    public List<MaikeDetailInfo> getDetail_info_list() {
        return detail_info_list;
    }

    public void setDetail_info_list(List<MaikeDetailInfo> detail_info_list) {
        this.detail_info_list = detail_info_list;
    }

    
}