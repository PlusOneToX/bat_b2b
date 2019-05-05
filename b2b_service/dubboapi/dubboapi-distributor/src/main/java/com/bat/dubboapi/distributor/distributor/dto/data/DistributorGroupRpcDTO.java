package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorGroupRpcDTO implements Serializable {
    private Integer id;
    private String name;
    private String erpGroupNo;

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

    public String getErpGroupNo() {
        return erpGroupNo;
    }

    public void setErpGroupNo(String erpGroupNo) {
        this.erpGroupNo = erpGroupNo;
    }
}
