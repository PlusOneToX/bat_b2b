package com.bat.system.web.globalsetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.ShopSettingService;
import com.bat.system.api.globalsetting.dto.ShopSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-购物设置后台接口", value = "AdminShopSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/shopSetting")
public class AdminShopSettingController extends BaseController {

    @Resource
    private ShopSettingService shopSettingService;

    @GetMapping()
    @ApiOperation(value = "查询设置")
    public Response<ShopSettingDTO> getShopSetting() {
        return Response.of(shopSettingService.getShopSetting());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShopSetting, value = CommonLogTypeConstantDTO.AdminShopSettingUpdate)
    @PutMapping()
    @ApiOperation(value = "更新设置")
    public Response updateShopSetting(@RequestBody @Valid ShopSettingUpdateCmd cmd) {
        shopSettingService.updateShopSetting(cmd);
        return Response.buildSuccess();
    }

}
