package com.bat.flexible.manager.wechat;

import com.bat.flexible.api.wechat.WechatServiceI;
import com.bat.flexible.api.wechat.dto.GzConfigDTO;
import com.bat.flexible.manager.wechat.executor.WechatExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatServiceIImpl implements WechatServiceI {

    @Autowired
    private WechatExe wechatExe;

    @Override
    public GzConfigDTO getGzConfig(String url, String appId) {
        GzConfigDTO gzConfigDTO= wechatExe.getGzConfig(url,appId);
        return gzConfigDTO;
    }


}
