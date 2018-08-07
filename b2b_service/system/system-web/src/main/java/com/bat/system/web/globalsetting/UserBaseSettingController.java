package com.bat.system.web.globalsetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.BaseSettingService;
import com.bat.system.api.globalsetting.dto.data.BaseSettingDTO;
import com.bat.system.api.globalsetting.dto.data.BaseSettingLoginSettingDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-基本设置前台接口", value = "UserBaseSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/baseSetting")
public class UserBaseSettingController extends BaseController {

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

    @GetMapping("/loginSetting")
    @ApiOperation(value = "查询登录设置")
    public Response<BaseSettingLoginSettingDTO> getBaseSettingLoginSetting() {
        return Response.of(baseSettingService.getBaseSettingLoginSetting());
    }

}