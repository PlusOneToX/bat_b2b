package com.bat.thirdparty.factory.maike.request.order;

public class MaikeDeliveryMethod {
    
    /**
     * 配送方式
     */
    private Integer id;

    /**
     * 快递名称
     */
    private String name;


    private Integer type;


    private String type_name;

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   

    

}