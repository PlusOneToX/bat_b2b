package com.bat.system.web.globalsetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.globalsetting.AgreementSettingService;
import com.bat.system.api.globalsetting.dto.AgreementDistributorCmd;
import com.bat.system.api.globalsetting.dto.AgreementSettingBrandQry;
import com.bat.system.api.globalsetting.dto.AgreementSettingId;
import com.bat.system.api.globalsetting.dto.AgreementSettingQry;
import com.bat.system.api.globalsetting.dto.data.AgreementSettingDTO;
import com.bat.system.api.globalsetting.dto.data.AgreementStatusDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-协议设置前台接口", value = "UserAgreementSettingController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/agreementSetting")
public class UserAgreementSettingController extends BaseController {

    @Resource
    private AgreementSettingService agreementSettingService;

    @GetMapping("/id")
    @ApiOperation(value = "查询设置ID")
    public Response<AgreementSettingDTO> getAgreementSettingById(@Valid AgreementSettingId id) {
        if (id.getId() == null) {
            // 默认国内的用户协议
            return Response.of(agreementSettingService.getChinaUserAgreementSetting());
        }
        return Response.of(agreementSettingService.getAgreementSettingById(id.getId()));
    }

    @GetMapping("/brandId")
    @ApiOperation(value = "查询品牌ID")
    public Response<AgreementSettingDTO> getAgreementSettingByBrandId(@Valid AgreementSettingBrandQry qry) {
        return Response.of(agreementSettingService.getAgreementSettingByBrandId(qry));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询设置(分页)")
    public Response<PageInfo<AgreementSettingDTO>> listAgreementSetting(@Valid AgreementSettingQry qry) {
        return Response.of(agreementSettingService.listAgreementSetting(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserAgreementSetting,
        value = CommonLogTypeConstantDTO.UserAgreementSettingSignAgreement)
    @PostMapping("/signAgreement")
    @ApiOperation(value = "签署协议")
    public Response signAgreement(@Valid @RequestBody AgreementDistributorCmd cmd) {
        agreementSettingService.signAgreement(cmd);
        return Response.buildSuccess();
    }

    @GetMapping("/agreementSign/status")
    @ApiOperation(value = "查询协议签署状态")
    public Response<AgreementStatusDTO> getAgreementSignStatus(@Valid AgreementDistributorCmd cmd) {
        if (cmd.getAgreementId() == null) {
            AgreementStatusDTO dto = new AgreementStatusDTO();
            dto.setSign(true);
            return Response.of(dto);
        }
        boolean agreementSignStatus = agreementSettingService.getAgreementSignStatus(cmd);
        AgreementStatusDTO dto = new AgreementStatusDTO();
        dto.setSign(agreementSignStatus);
        return Response.of(dto);
    }

    @GetMapping("/agreementSign/user")
    @ApiOperation(value = "查询用户已经签署的协议")
    public Response<List<AgreementSettingDTO>> getAgreementSettingByDistributorId(@Valid AgreementDistributorCmd cmd) {
        Integer distributorId = cmd.getDistributorId();
        return Response.of(agreementSettingService.getAgreementSettingByDistributorId(distributorId));
    }
}
