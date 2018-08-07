package com.bat.financial.web.platformaccount;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.api.platformaccount.AccountOfflineService;
import com.bat.financial.api.platformaccount.dto.AccountId;
import com.bat.financial.api.platformaccount.dto.AccountOfflineCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountOfflineUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountOfflineDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "平台账户-线下支付账户后台接口", value = "AdminAccountOfflineController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/platform/offline")
public class AdminAccountOfflineController extends BaseController {

    @Resource
    private AccountOfflineService accountOfflineService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountOffline, value =
    // CommonLogTypeConstantDTO.AdminAccountOfflineGet)
    @GetMapping("")
    @ApiOperation(value = "查询收款账户")
    public Response<AccountOfflineDTO> getAccount(@Valid AccountId id) {
        return Response.of(accountOfflineService.getAccount(id.getId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountOffline, value =
    // CommonLogTypeConstantDTO.AdminAccountOfflineList)
    @GetMapping("/list")
    @ApiOperation(value = "查询收款账户(线下)(分页)")
    @SearchMethod
    public Response<PageInfo<AccountOfflineDTO>> listAccountWx(@Valid AccountQry qry) {
        return Response.of(accountOfflineService.listAccount(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountOffline,
        value = CommonLogTypeConstantDTO.AdminAccountOfflineAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(线下)")
    public Response createAccount(@RequestBody @Valid AccountOfflineCreateCmd cmd) {
        accountOfflineService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountOffline,
        value = CommonLogTypeConstantDTO.AdminAccountOfflineUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(线下)")
    public Response updateAccount(@RequestBody @Valid AccountOfflineUpdateCmd cmd) {
        accountOfflineService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountOffline,
        value = CommonLogTypeConstantDTO.AdminAccountOfflineDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(线下)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountOfflineService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

}
