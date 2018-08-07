package com.bat.system.web.globalsetting;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.BaseSettingService;
import com.bat.system.api.globalsetting.dto.BaseSettingKeyUpdateCmd;
import com.bat.system.api.globalsetting.dto.BaseSettingLoginSettingUpdateCmd;
import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.api.globalsetting.dto.data.BaseSettingLoginSettingDTO;
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

@Api(tags = "商店配置模块-基本设置后台接口", value = "AdminBaseSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/baseSetting")
public class AdminBaseSettingController extends BaseController {

    @Resource
    private BaseSettingService baseSettingService;

    @GetMapping("")
    @ApiOperation(value = "查询设置通过KEY")
    public Response<List<BaseSettingDTO>> getBaseSettingByKey(@Valid @RequestParam(value = "key") List<String> qry) {
        List<BaseSettingDTO> collect =
            qry.stream().map(baseSettingKeyQry -> baseSettingService.getBaseSettingByKey(baseSettingKeyQry))
                .collect(Collectors.toList());
        return Response.of(collect);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBaseSetting,
        value = CommonLogTypeConstantDTO.AdminBaseSettingUpdate)
    @PutMapping()
    @ApiOperation(value = "更新设置通过KEY")
    public Response updateBaseSettingByKey(@RequestBody @Valid List<BaseSettingKeyUpdateCmd> cmd) {
        cmd.forEach(baseSettingKeyUpdateCmd -> baseSettingService.updateBaseSettingByKey(baseSettingKeyUpdateCmd));
        return Response.buildSuccess();
    }

    @GetMapping("/loginSetting")
    @ApiOperation(value = "查询登录设置")
    public Response<BaseSettingLoginSettingDTO> getBaseSettingLoginSetting() {
        return Response.of(baseSettingService.getBaseSettingLoginSetting());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBaseSetting,
        value = CommonLogTypeConstantDTO.AdminBaseSettingLogin)
    @PutMapping("/loginSetting")
    @ApiOperation(value = "更新登录设置")
    public Response updateBaseSettingLoginSetting(@RequestBody @Valid BaseSettingLoginSettingUpdateCmd cmd) {
        baseSettingService.updateBaseSettingLoginSetting(cmd);
        return Response.buildSuccess();
    }
}
