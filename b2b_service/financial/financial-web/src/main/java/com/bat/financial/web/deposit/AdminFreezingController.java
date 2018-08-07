package com.bat.financial.web.deposit;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.FreezingService;
import com.bat.financial.api.deposit.dto.FreezableQry;
import com.bat.financial.api.deposit.dto.FreezingCreateCmd;
import com.bat.financial.api.deposit.dto.FreezingDeleteCmd;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorFreezingDTO;
import com.bat.financial.api.distributoraccount.dto.DistributorIds;
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
@Api(tags = "预存款-冻结列表后台接口", value = "AdminFreezingController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/deposit")
public class AdminFreezingController extends BaseController {
    @Resource
    private FreezingService freezingService;

    @GetMapping("/freezing/list")
    @ApiOperation(value = "冻结列表")
    @SearchMethod
    public Response<PageInfo<DepositDistributorFreezingDTO>> listFreezing(@Valid FreezableQry qry) {
        PageInfo<DepositDistributorFreezingDTO> pageInfo = freezingService.listFreezing(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/freezing/balance")
    @ApiOperation(value = "冻结列表获取分销商余额信息")
    @SearchMethod
    public Response<List<DepositDistributorDTO>> listBalance(@Valid DistributorIds id) {
        return Response.of(freezingService.listDepositAvailableByDistributorIds(id.getIds()));
    }

    /**
     * 后台添加冻结 其他类型
     * 
     * @param cmd
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminFreezing,
        value = CommonLogTypeConstantDTO.AdminFreezingAdd)
    @PostMapping("/freezing")
    @ApiOperation(value = "添加冻结")
    public Response createFreezing(@Valid @RequestBody FreezingCreateCmd cmd) {
        freezingService.createFreezing(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminFreezing,
        value = CommonLogTypeConstantDTO.AdminFreezingAdds)
    @PutMapping("/freezings")
    @ApiOperation(value = "添加冻结(批量)")
    public Response createFreezings(@Valid @RequestBody List<FreezingCreateCmd> cmds) {
        freezingService.createFreezings(cmds);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminFreezing,
        value = CommonLogTypeConstantDTO.AdminFreezingThaw)
    @PutMapping("/freezing/thaw")
    @ApiOperation(value = "解除冻结")
    public Response updateFreezing(@Valid @RequestBody FreezingDeleteCmd cmd) {
        freezingService.updateFreezing(cmd);
        return Response.buildSuccess();
    }

    /**
     * 分销商服务提供
     */
    // @GetMapping("/freezing")
    // @ApiOperation(value = "获取可冻结分销商列表")
    // public Response listFreezableDistributor(@Valid DepositFreezableDistributorQry qry) {
    // freezingService.listFreezableDistributor(qry);
    // return Response.buildSuccess();
    // }
}
