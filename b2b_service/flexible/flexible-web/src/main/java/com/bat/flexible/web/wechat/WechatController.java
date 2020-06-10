package com.bat.flexible.web.wechat;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.wechat.WechatServiceI;
import com.bat.flexible.api.wechat.dto.GzConfigDTO;
import com.bat.flexible.api.wechat.dto.GzConfigQry;
import com.bat.flexible.web.shop.UserShopController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/wechat")
@Api(tags = "微信前台接口")
public class WechatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserShopController.class);

    @Autowired
    private WechatServiceI wechatServiceI;


    @PostMapping("/gz/config")
    @ApiOperation(value = "获取公众号签名配置")
    public Response<GzConfigDTO> getGzConfig(@Valid @RequestBody GzConfigQry gzConfigQry) {
        GzConfigDTO gzConfigDTO = wechatServiceI.getGzConfig(gzConfigQry.getUrl(),gzConfigQry.getAppId());
        return Response.of(gzConfigDTO);
    }


}
