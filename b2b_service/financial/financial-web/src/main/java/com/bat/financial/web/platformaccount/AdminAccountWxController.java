package com.bat.financial.web.platformaccount;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.api.platformaccount.AccountWxService;
import com.bat.financial.api.platformaccount.dto.AccountId;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.AccountWxCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountWxUpdateCmd;
import com.bat.financial.api.platformaccount.dto.data.AccountWxDTO;
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

@Api(tags = "平台账户-微信支付账户后台接口", value = "AdminAccountWxController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/platform/accountWx")
public class AdminAccountWxController extends BaseController {

    @Resource
    private AccountWxService accountWxService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWx, value =
    // CommonLogTypeConstantDTO.AdminAccountWxGet)
    @GetMapping("")
    @ApiOperation(value = "查询收款账户")
    public Response<AccountWxDTO> getAccount(@Valid AccountId id) {
        return Response.of(accountWxService.getAccount(id.getId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWx, value =
    // CommonLogTypeConstantDTO.AdminAccountWxList)
    @GetMapping("/list")
    @ApiOperation(value = "查询收款账户(微信)(分页)")
    @SearchMethod
    public Response<PageInfo<AccountWxDTO>> listAccountWx(@Valid AccountQry qry) {
        return Response.of(accountWxService.listAccount(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWx,
        value = CommonLogTypeConstantDTO.AdminAccountWxAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(微信)")
    public Response createAccount(@RequestBody @Valid AccountWxCreateCmd cmd) {
        accountWxService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWx,
        value = CommonLogTypeConstantDTO.AdminAccountWxUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(微信)")
    public Response updateAccount(@RequestBody @Valid AccountWxUpdateCmd cmd) {
        accountWxService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWx,
        value = CommonLogTypeConstantDTO.AdminAccountWxDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(微信)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountWxService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

}
