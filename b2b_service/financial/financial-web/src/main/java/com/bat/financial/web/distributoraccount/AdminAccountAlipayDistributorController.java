package com.bat.financial.web.distributoraccount;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.api.distributoraccount.dto.*;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.api.distributoraccount.AccountAlipayDistributorService;
import com.bat.financial.api.distributoraccount.dto.data.AccountAlipayDistributorDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:50
 */
@Api(tags = "分销商账户-支付宝账户列表后台接口", value = "AdminAccountAlipayDistributorController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/distributor/accountAlipay")
public class AdminAccountAlipayDistributorController extends BaseController {

    @Resource
    private AccountAlipayDistributorService accountAlipayDistributorService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountAlipayDistributorGet)
    @GetMapping("")
    @ApiOperation(value = "查询收款账户")
    public Response<AccountAlipayDistributorDTO> getAccount(@Valid AccountAppId id) {
        return Response.of(accountAlipayDistributorService.getAccount(id.getAppId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountAlipayDistributorList)
    @GetMapping("/list")
    @ApiOperation(value = "通过当前用户查询收款账户(支付宝)(分页)")
    @SearchMethod
    public Response<PageInfo<AccountAlipayDistributorDTO>> listAccountAlipayDistributor(@Valid AccountQry qry) {
        return Response.of(accountAlipayDistributorService.listAccount(qry));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountAlipayDistributorCheckDistributor)
    @GetMapping("/checkDistributor")
    @ApiOperation(value = "检查分销商关联情况")
    @SearchMethod
    public Response checkDistributor(@Valid DistributorIds ids) {
        return Response.of(accountAlipayDistributorService.checkDistributor(ids.getIds()));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayDistributorAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(支付宝)")
    public Response createAccount(@RequestBody @Valid AccountAlipayDistributorCreateCmd cmd) {
        accountAlipayDistributorService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayDistributorUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(支付宝)")
    public Response updateAccount(@RequestBody @Valid AccountAlipayDistributorUpdateCmd cmd) {
        accountAlipayDistributorService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountAlipayDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountAlipayDistributorDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(支付宝)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountAlipayDistributorService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

}