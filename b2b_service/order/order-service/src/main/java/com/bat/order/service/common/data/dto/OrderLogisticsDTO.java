package com.bat.order.service.common.data.dto;

import lombok.Data;

@Data
public class OrderLogisticsDTO {

    private Short logisticsType;
    private String manufactors;
    private Integer logisticsId;
    private String logisticsName;

}