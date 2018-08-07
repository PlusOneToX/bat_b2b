package com.bat.system.web.globalsetting;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.AgreementSettingService;
import com.bat.system.api.globalsetting.dto.*;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.globalsetting.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-协议设置后台接口", value = "AdminAgreementSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/agreementSetting")
public class AdminAgreementSettingController extends BaseController {

    @Resource
    private AgreementSettingService agreementSettingService;

    @GetMapping()
    @ApiOperation(value = "查询设置ID")
    public Response<AgreementSettingDTO> getAgreementSettingById(@Valid AgreementSettingId id) {
        return Response.of(agreementSettingService.getAgreementSettingById(id.getId()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询设置(分页)")
    public Response<PageInfo<AgreementSettingDTO>> listAgreementSetting(@Valid AgreementSettingQry qry) {
        return Response.of(agreementSettingService.listAgreementSetting(qry));
    }

    @GetMapping("/checkBrand")
    @ApiOperation(value = "查询设置(分页)")
    public Response<List<Integer>> checkBrandAgreementSetting(@Valid CheckBrandAgreementQry qry) {
        return Response.of(agreementSettingService.checkBrandAgreementSetting(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAgreementSetting, value = CommonLogTypeConstantDTO.AdminAgreementSettingAdd)
    @PostMapping()
    @ApiOperation(value = "添加协议设置")
    public Response createAgreementSetting(@RequestBody @Valid AgreementSettingCreateCmd cmd) {
        agreementSettingService.createAgreementSetting(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAgreementSetting, value = CommonLogTypeConstantDTO.AdminAgreementSettingUpdate)
    @PutMapping()
    @ApiOperation(value = "更新协议设置")
    public Response updateAgreementSetting(@RequestBody @Valid AgreementSettingUpdateCmd cmd) {
        agreementSettingService.updateAgreementSetting(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAgreementSetting, value = CommonLogTypeConstantDTO.AdminAgreementSettingDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除协议设置")
    public Response deleteAgreementSetting(@RequestBody @Valid AgreementSettingId id) {
        agreementSettingService.deleteAgreementSettingById(id.getId());
        return Response.buildSuccess();
    }
}
