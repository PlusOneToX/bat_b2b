package com.bat.dubboapi.thirdparty.maike.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDetailLocalUploadCmd implements Serializable {

    private static final long serialVersionUID = -37407756303488487L;


    private Integer orderGoodsDiyId;

    private String factoryCode;

    /**
     * 需要上传的url
     */
    private List<String> urlList;
}
