package com.bat.thirdparty.distributor.controller;

import com.bat.thirdparty.common.base.BaseId;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.erp.api.ErpServiceI;
import com.bat.thirdparty.log.annotation.SysLog;
import com.bat.thirdparty.log.constants.CommonLogTypeConstantDTO;
import com.bat.thirdparty.log.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/distributor")
@Api(tags = "第三方触发的分销商模块后台接口")
public class ThirdDistributorController {

    @Autowired
    private ErpServiceI service;

    @SysLog(businessFunction= CommonLogTypeTitleConstantDTO.Erp,value = CommonLogTypeConstantDTO.ErpSyncDistributor)
    @ApiOperation(value = "ERP分销商同步")
    @PostMapping(value = "/sync/distributor")
    public Response distributorSync(@RequestBody BaseId cmd) throws Exception {
        service.distributorSync(cmd);
        return Response.buildSuccess();
    }
}
