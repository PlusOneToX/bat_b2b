package com.bat.financial.web.distributoraccount;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.api.distributoraccount.dto.*;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.api.distributoraccount.AccountWxDistributorService;
import com.bat.financial.api.distributoraccount.dto.data.AccountWxDistributorDTO;
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
@Api(tags = "分销商账户-微信支付账户列表后台接口", value = "AdminAccountWxDistributorController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/distributor/accountWx")
public class AdminAccountWxDistributorController extends BaseController {

    @Resource
    private AccountWxDistributorService accountWxDistributorService;

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountWxDistributorGetById)
    @GetMapping()
    @ApiOperation(value = "查询收款账户")
    public Response<AccountWxDistributorDTO> getAccountByAppId(@Valid AccountId id) {
        return Response.of(accountWxDistributorService.getAccountById(id.getId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountWxDistributorGetByAppId)
    @GetMapping("/appId")
    @ApiOperation(value = "查询收款账户")
    public Response<AccountWxDistributorDTO> getAccountByAppId(@Valid AccountAppId id) {
        return Response.of(accountWxDistributorService.getAccountByAppIdAndSubMchid(id));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountWxDistributorGetByDistributorId)
    @GetMapping("/distributorId")
    @ApiOperation(value = "查询收款账户")
    public Response<AccountWxDistributorDTO> getAccountByDistributorId(@Valid AccountDistributorId id) {
        return Response.of(accountWxDistributorService.getAccountByDistributorId(id.getDistributorId()));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountWxDistributorList)
    @GetMapping("/list")
    @ApiOperation(value = "查询收款账户(微信)(分页)")
    @SearchMethod
    public Response<PageInfo<AccountWxDistributorDTO>> listAccountWx(@Valid AccountQry qry) {
        return Response.of(accountWxDistributorService.listAccount(qry));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor, value =
    // CommonLogTypeConstantDTO.AdminAccountWxDistributorCheck)
    @GetMapping("/checkDistributor")
    @ApiOperation(value = "检查分销商关联情况")
    @SearchMethod
    public Response checkDistributor(@Valid DistributorIds ids) {
        return Response.of(accountWxDistributorService.checkDistributor(ids));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountWxDistributorAdd)
    @PostMapping()
    @ApiOperation(value = "添加收款账户(微信)")
    public Response createAccount(@RequestBody @Valid AccountWxDistributorCreateCmd cmd) {
        accountWxDistributorService.createAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountWxDistributorUpdate)
    @PutMapping()
    @ApiOperation(value = "更新收款账户(微信)")
    public Response updateAccount(@RequestBody @Valid AccountWxDistributorUpdateCmd cmd) {
        accountWxDistributorService.updateAccount(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAccountWxDistributor,
        value = CommonLogTypeConstantDTO.AdminAccountWxDistributorDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除收款账户(微信)")
    public Response deleteAccount(@RequestBody @Valid AccountId id) {
        accountWxDistributorService.deleteAccountById(id.getId());
        return Response.buildSuccess();
    }

    @GetMapping("/getSubAccountRatioByDistributor")
    @ApiOperation(value = "查询分销商服务商的分账比例")
    public Response<BigDecimal> getSubAccountRatioByDistributor(@Valid AccountDistributorId id) {
        BigDecimal ratio =
            accountWxDistributorService.getSubAccountRatioByDistributor(Integer.parseInt(id.getDistributorId()));
        return Response.of(ratio);
    }

}