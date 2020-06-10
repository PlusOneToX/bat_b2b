package com.bat.flexible.api.wechat;

import com.bat.flexible.api.wechat.dto.GzConfigDTO;

public interface WechatServiceI {

    GzConfigDTO getGzConfig(String url, String appId);
}
