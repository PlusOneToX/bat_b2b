package com.bat.financial.web.depositdetail;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.BalanceInfoSyncQry;
import com.bat.financial.api.deposit.dto.DepositAvailableQry;
import com.bat.financial.api.deposit.dto.DepositDetailUserQry;
import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 代码拆开方便生产权限数据
 * @date: 2018/5/27 14:20
 */
@Api(tags = "预存款明细-余额表后台接口", value = "AdminDepositController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/deposit")
public class AdminDepositBalanceController extends BaseController {

    @Resource
    private DepositBalanceService depositService;

    @Resource
    private DepositDetailService depositDetailService;

    @GetMapping()
    @ApiOperation(value = "预存款设置查询")
    public Response<DepositDTO> getDeposit() {
        return Response.of(depositService.getDepositSet());
    }

    @GetMapping("/available/list")
    @ApiOperation(value = "预存款明细-余额列表")
    @SearchMethod
    public Response<PageInfo<DepositDistributorDTO>> listDepositAvailableByUserId(@Valid DepositAvailableQry qry) {
        return Response.of(depositService.listDepositAvailableByUserId(qry));
    }

    @GetMapping("/available/detail")
    @ApiOperation(value = "预存款明细-余额详情")
    public Response<PageInfo<DepositDistributorSubsidiaryBookDTO>>
        listDepositDetailByDistributorId(@Valid DepositDetailUserQry qry) {
        return Response.of(depositDetailService.listDepositDetailByDistributorId(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDepositBalance,
        value = CommonLogTypeConstantDTO.AdminDepositBalanceSync)
    @GetMapping("/sync")
    @ApiOperation(value = "预存款余额同步")
    public Response syncDepositBalanceInfo(@Valid BalanceInfoSyncQry syncCmd) {
        depositService.syncDepositBalanceInfo(syncCmd);
        return Response.buildSuccess();
    }
}
