package com.bat.financial.web.depositdetail;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.api.deposit.dto.*;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.DepositBalanceService;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.RechargeService;
import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDetailSummaryDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.api.pay.dto.data.CreateTradeRespDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 14:20
 */
@Api(tags = "预存款前台接口", value = "UserDepositController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/user/deposit")
public class UserDepositController extends BaseController {

    @Resource
    private DepositBalanceService depositService;

    @Resource
    private DepositDetailService depositDetailService;

    @Resource
    private RechargeService reChargeService;

    @GetMapping("")
    @ApiOperation(value = "根据分销商id查询余额")
    public Response<DepositDistributorDTO> getDepositById(@Valid DepositAvailableId id) {
        return Response.of(depositService.getDepositBalanceByDistributorId(id.getId()));
    }

    @GetMapping("/set")
    @ApiOperation(value = "预存款设置查询")
    public Response<DepositDTO> getDepositSet() {
        return Response.of(depositService.getDepositSet());
    }

    @GetMapping("/detail/list")
    @ApiOperation(value = "资产明细")
    public Response<PageInfo<DepositDistributorSubsidiaryBookDTO>>
        listDepositDetailByUserId(@Valid DepositDetailUserQry qry) {
        return Response.of(depositDetailService.listDepositDetailByDistributorId(qry));
    }

    @GetMapping("/detail/summary")
    @ApiOperation(value = "按时间范围查询资产明细汇总")
    public Response<DepositDetailSummaryDTO> listDepositDetailSummary(@Valid DepositDetailSummaryUserQry qry) {
        return Response.of(depositDetailService.listDepositDetailSummary(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserDeposit,
        value = CommonLogTypeConstantDTO.UserDepositRecharge)
    @PostMapping("/recharge")
    @ApiOperation(value = "预存款充值")
    public Response<CreateTradeRespDTO> recharge(@Valid @RequestBody DepositReChargeCreateCmd cmd) {
        return Response.of(reChargeService.createRecharge(cmd));
    }

    @GetMapping("/rechargeStatus")
    @ApiOperation(value = "预存款充值状态")
    public Response queryRechargeStatus(@Valid DepositReChargeQry qry) {
        return Response.of(reChargeService.queryRechargeStatus(qry));
    }

    // @PostMapping("/withdraw")
    // @ApiOperation(value = "预存款提现")
    // public Response withdraw(@Valid @RequestBody DepositWithdrawCreateCmd cmd) {
    // depositService.createWithdraw(cmd);
    // return Response.buildSuccess();
    // }
    //

}
