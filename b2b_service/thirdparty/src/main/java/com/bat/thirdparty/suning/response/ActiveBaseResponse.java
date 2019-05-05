package com.bat.thirdparty.suning.response;

import lombok.Data;

@Data
public class ActiveBaseResponse  {

    /**
     * 处理状态 S-成功；F-失败；
     */
    private String returnCode;

    /**
     * 报错信息
     */
    private String returnMessage;
}
