package com.bat.financial.web.platformaccount;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.platformaccount.AccountKuaiQianService;
import com.bat.financial.api.platformaccount.dto.AccountId;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianCreateCmd;
import com.bat.financial.api.platformaccount.dto.AccountKuaiQianUpdateCmd;
import com.bat.financial.api.platformaccount.dto.AccountQry;
import com.bat.financial.api.platformaccount.dto.data.AccountKuaiQianDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "平台账户-快钱支付账户后台接口", value = "AdminAccountKuaiQianController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/platform/KuaiQian")
public class AdminAccountKuaiQianController extends BaseController {

    @Resource
    private AccountKuaiQianService accountKuaiQianService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountKuaiQian, value =
    // CommonLogTypeConstantDTO.AdminAccountKuaiQianGet)
    @GetMapping()
    @ApiOperation(value = "查询收款账户(快钱)")
    public Response<AccountKuaiQianDTO> getAccount(@Valid AccountId id) {
        return Response.of(accountKuaiQianService.getAccount(id.getId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountKuaiQian, value =
    // CommonLogTypeConstantDTO.AdminAccountKuaiQianList)
    @GetMapping("/list")
    @ApiOperation(value = "查询收款账户(快钱)(分页)")
    public Response<PageInfo<AccountKuaiQianDTO>> listAccountKuaiQian(@Valid AccountQry qry) {
        return Response.of(accountKuaiQianService.listAccount(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountKuaiQian,
        value = CommonLogTypeConstantDTO.AdminAccountKuaiQianAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(快钱)")
    public Response createAccount(@RequestBody @Valid AccountKuaiQianCreateCmd cmd) {
        accountKuaiQianService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountKuaiQian,
        value = CommonLogTypeConstantDTO.AdminAccountKuaiQianUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(快钱)")
    public Response updateAccount(@RequestBody @Valid AccountKuaiQianUpdateCmd cmd) {
        accountKuaiQianService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountKuaiQian,
        value = CommonLogTypeConstantDTO.AdminAccountKuaiQianDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(快钱)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountKuaiQianService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

}
