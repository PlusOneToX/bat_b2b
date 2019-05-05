package com.bat.dubboapi.flexible.exchange.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExchangeCodeOrderDTO implements Serializable {

    private static final long serialVersionUID = 2907067635477159633L;

    /**
     * 材质id
     */
    private Integer materialId;

    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 图片id
     */
    private Integer pictureId;

    /**
     * 暗码列表
     */
    private List<String> secretCodeList;


}
