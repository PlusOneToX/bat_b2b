package com.bat.thirdparty.alibaba.taobao.dao.dataobject;

import java.io.Serializable;

public class TaobaoTradeOrderRelevanceDO implements Serializable {
    private Integer id;

    private Long tid;

    private Long oid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
}