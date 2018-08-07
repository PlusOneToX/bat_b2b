package com.bat.financial.web.deposit;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.WithdrawalService;
import com.bat.financial.api.deposit.dto.WithdrawalCreateCmd;
import com.bat.financial.api.deposit.dto.WithdrawalIds;
import com.bat.financial.api.deposit.dto.WithdrawalQry;
import com.bat.financial.api.deposit.dto.data.WithdrawDepositsDistributorApplyDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 10:18
 */
@Api(tags = "预存款-提现列表后台接口", value = "AdminWithdrawalController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/deposit")
public class AdminWithdrawalController extends BaseController {
    @Resource
    private WithdrawalService withdrawalService;

    @GetMapping("/withdrawal/list")
    @ApiOperation(value = "提现列表")
    @SearchMethod
    public Response<PageInfo<WithdrawDepositsDistributorApplyDTO>> listWithdrawal(@Valid WithdrawalQry qry) {
        return Response.of(withdrawalService.listWithdrawal(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWithdrawal,
        value = CommonLogTypeConstantDTO.AdminWithdrawalWithdrawalAdd)
    @PostMapping("/withdrawal")
    @ApiOperation(value = "新增提现")
    public Response createWithdrawal(@Valid @RequestBody WithdrawalCreateCmd cmd) {
        withdrawalService.createWithdrawal(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWithdrawal,
        value = CommonLogTypeConstantDTO.AdminWithdrawalWithdrawalDelete)
    @DeleteMapping("/withdrawal")
    @ApiOperation(value = "删除提现")
    public Response deleteWithdrawal(@Valid @RequestBody WithdrawalIds ids) {
        withdrawalService.deleteWithdrawalByIds(ids.getIds());
        return Response.buildSuccess();
    }
}
