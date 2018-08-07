package com.bat.system.web.globalsetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.FactorySettingService;
import com.bat.system.api.globalsetting.dto.FactorySettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.FactorySettingDTO;
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

@Api(tags = "商店配置模块-工厂设置后台接口", value = "AdminFactorySettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/factorySetting")
public class AdminFactorySettingController extends BaseController {

    @Resource
    private FactorySettingService factorySettingService;

    @GetMapping("")
    @ApiOperation(value = "查询工厂生产推送时效设置")
    public Response<FactorySettingDTO> getFactorySettingDelayPushes() {
        FactorySettingDTO dto = new FactorySettingDTO();
        dto.setInvalidDefault(factorySettingService.getFactorySettingOrderInvalidByOrderSource(0));
        dto.setDelayPushes(factorySettingService.listFactorySettingDelayPushes());
        dto.setOrderInvalid(factorySettingService.listFactorySettingOrderInvalid());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminFactorySetting, value = CommonLogTypeConstantDTO.AdminFactorySettingUpdateFactory)
    @PutMapping("")
    @ApiOperation(value = "更新工厂生产推送时效设置")
    public Response updateFactorySettingDelayPushes(@RequestBody @Valid FactorySettingUpdateCmd cmd) {
        factorySettingService.updateFactorySetting(cmd);
        return Response.buildSuccess();
    }

}
