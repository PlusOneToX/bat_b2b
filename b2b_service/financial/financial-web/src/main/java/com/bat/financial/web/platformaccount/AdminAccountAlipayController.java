package com.bat.financial.web.platformaccount;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.api.platformaccount.AccountAlipayService;
import com.bat.financial.api.platformaccount.dto.AccountAlipayCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountAlipayUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountId;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountAlipayDTO;
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

@Api(tags = "平台账户-支付宝支付账户后台接口", value = "AdminAccountAlipayController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/platform/accountAlipay")
public class AdminAccountAlipayController extends BaseController {

    @Resource
    private AccountAlipayService accountAlipayService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipay, value =
    // CommonLogTypeConstantDTO.AdminAccountAlipayGet)
    @GetMapping()
    @ApiOperation(value = "查询收款账户(支付宝)")
    public Response<AccountAlipayDTO> getAccount(@Valid AccountId id) {
        return Response.of(accountAlipayService.getAccount(id.getId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipay, value =
    // CommonLogTypeConstantDTO.AdminAccountAlipayList)
    @GetMapping("/list")
    @ApiOperation(value = "查询收款账户(支付宝)(分页)")
    @SearchMethod
    public Response<PageInfo<AccountAlipayDTO>> listAccountAlipay(@Valid AccountQry qry) {
        return Response.of(accountAlipayService.listAccount(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipay,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(支付宝)")
    public Response createAccount(@RequestBody @Valid AccountAlipayCreateCmd cmd) {
        accountAlipayService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipay,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(支付宝)")
    public Response updateAccount(@RequestBody @Valid AccountAlipayUpdateCmd cmd) {
        accountAlipayService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipay,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(支付宝)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountAlipayService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

}
