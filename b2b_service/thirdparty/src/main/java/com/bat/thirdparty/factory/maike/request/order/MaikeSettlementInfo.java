package com.bat.thirdparty.factory.maike.request.order;

/**
 * 麦客结算
 */
public class MaikeSettlementInfo {

    /**
     * 结算名称
     */
    private String name="";


    /**
     * 0:现结，1:月结，现只支持 0
     */
    private Integer way;


    /**
     * 下单时付款百分比
     */
    private Double order_percent;
    

    /**
     * 发货时付款百分比
     */
    private Double shipments_percent;

    /**
     * 收货时付款百分比
     */
    private Double take_percent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Double getOrder_percent() {
        return order_percent;
    }

    public void setOrder_percent(Double order_percent) {
        this.order_percent = order_percent;
    }

    public Double getShipments_percent() {
        return shipments_percent;
    }

    public void setShipments_percent(Double shipments_percent) {
        this.shipments_percent = shipments_percent;
    }

    public Double getTake_percent() {
        return take_percent;
    }

    public void setTake_percent(Double take_percent) {
        this.take_percent = take_percent;
    }


    
}