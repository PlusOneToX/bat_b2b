package com.bat.system.web.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.globalsetting.ShopSettingService;
import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-购物设置前台接口", value = "UserShopSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/shopSetting")
public class UserShopSettingController extends BaseController {

    @Resource
    private ShopSettingService shopSettingService;

    @GetMapping()
    @ApiOperation(value = "查询设置")
    public Response<ShopSettingDTO> getShopSetting() {
        return Response.of(shopSettingService.getShopSetting());
    }

}
