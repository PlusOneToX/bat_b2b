package com.bat.dubboapi.thirdparty.maike.dto.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDetailFTPDTO implements Serializable {


    private static final long serialVersionUID = 2972626616873479095L;
    private Integer orderGoodsDiyId;

    /**
     * FTP文件夹根路径
     */
    private String rootPath;

    /**
     * URL列表
     */
    private List<PictureLocalUrlCmd> localUrlCmdList;
}
