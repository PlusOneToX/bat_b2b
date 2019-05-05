package com.bat.promotion.web.rebatevoucher;

import java.io.IOException;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.web.annotation.SysLog;
import com.bat.promotion.web.base.BaseController;
import com.bat.promotion.web.base.Response;
import com.bat.promotion.web.constants.CommonLogTypeConstantDTO;
import com.bat.promotion.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmds;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.rebatevoucher.RebateVoucherService;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmd;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherListQry;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherUsageRecordListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;
import com.bat.promotion.web.annotation.SearchMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:30
 */
@Api(tags = "返利代金券后台接口", value = "AdminRebateVoucherController")
@RestController
@RequestMapping("/promotion/v1/web/admin/rebateVoucher")
public class AdminRebateVoucherController extends BaseController {
    @Resource
    private RebateVoucherService rebateVoucherService;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.RebateVoucher,
        value = CommonLogTypeConstantDTO.RebateVoucherAdd)
    @ApiOperation(value = "新增返利代金券")
    @PostMapping()
    public Response createRebateVoucher(@RequestBody @Valid RebateVoucherCmd cmd) {
        rebateVoucherService.createRebateVoucher(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.RebateVoucher,
        value = CommonLogTypeConstantDTO.RebateVoucherAdd)
    @ApiOperation(value = "批量新增返利代金券")
    @PostMapping("/batchCreate")
    public Response batchCreateRebateVoucher(@RequestBody @Valid RebateVoucherCmds cmds) {
         rebateVoucherService.batchCreateRebateVoucher(cmds.getRebateVoucherCmdList(), getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据搜索条件查找返利代金券列表(分页)")
    @GetMapping(value = "/list")
    @SearchMethod
    public Response<PageInfo<RebateVoucherDTO>> listRebateVoucher(@Valid RebateVoucherListQry qry) {
        PageInfo<RebateVoucherDTO> pageInfo = rebateVoucherService.listRebateVoucher(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.RebateVoucher,
        value = CommonLogTypeConstantDTO.RebateVoucherUpdate)
    @ApiOperation(value = "更新代金券（冻结/解冻）")
    @PutMapping()
    @SearchMethod
    public Response<PageInfo<RebateVoucherDTO>> updateRebateVoucher(@RequestBody @Valid RebateVoucherCmd cmd) {
        rebateVoucherService.updateRebateVoucher(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "代金券使用记录列表(分页)")
    @GetMapping("/listUsageRecord")
    public Response<PageInfo<RebateVoucherUsageRecordDTO>>
        listRebateVoucherUsageRecord(@Valid RebateVoucherUsageRecordListQry qry) {
        PageInfo<RebateVoucherUsageRecordDTO> pageInfo = rebateVoucherService.listRebateVoucherUsageRecord(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/tempDownLoad")
    @ApiOperation(value = "获取下载模板URL")
    public Response getTempUrl() {
        return Response.of(rebateVoucherService.getTempUrl());
    }

    @PostMapping("/import")
    @ApiOperation(value = "返利金导入接口")
    public Response importRebateVoucher(MultipartFile file) throws IOException {
        rebateVoucherService.importRebateVoucher(file.getInputStream(), getUserId(), getUserName());
        return Response.buildSuccess();
    }
}
