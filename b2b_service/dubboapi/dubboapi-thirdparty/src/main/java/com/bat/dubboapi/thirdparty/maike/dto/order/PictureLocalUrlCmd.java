package com.bat.dubboapi.thirdparty.maike.dto.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureLocalUrlCmd implements Serializable {
    private static final long serialVersionUID = -1217411245902476756L;

    /**
     * 源URL
     */
    private String sourceUrl;

    /**
     * 返回的本地URL
     */
    private String localUrl;
}
