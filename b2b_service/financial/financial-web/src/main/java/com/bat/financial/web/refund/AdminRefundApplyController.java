package com.bat.financial.web.refund;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import com.bat.financial.web.annotation.SysLog;
import com.bat.financial.web.constants.CommonLogTypeConstantDTO;
import com.bat.financial.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.refund.RefundApplyService;
import com.bat.financial.api.refund.dto.RefundApplyManualConfirmCmd;
import com.bat.financial.api.refund.dto.RefundApplyQry;
import com.bat.financial.api.refund.dto.data.RefundApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundCustomerApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundDistributorApplyDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/1 10:36
 */
@Api(tags = "销售往来单据-退款单申请列表后台接口", value = "AdminRefundApplyController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/refundApply")
public class AdminRefundApplyController extends BaseController {
    @Resource
    private RefundApplyService refundApplyService;

    @GetMapping("/distributor/list")
    @ApiOperation(value = "分销商退款单申请列表")
    @SearchMethod
    public Response<PageInfo<RefundDistributorApplyDTO>> listDistributorRefundApply(@Valid RefundApplyQry qry) {
        return Response.of(refundApplyService.listDistributorRefundApply(qry));
    }

    @GetMapping("/customer/list")
    @ApiOperation(value = "柔性退款单申请列表")
    @SearchMethod
    public Response<PageInfo<RefundCustomerApplyDTO>> listCustomerRefundApply(@Valid RefundApplyQry qry) {
        return Response.of(refundApplyService.listCustomerRefundApply(qry));
    }

    @GetMapping("/list")
    @ApiOperation(value = "退款单申请列表 汇总(不分页)")
    @SearchMethod
    public Response<RefundApplyDTO> listRefundApply(@Valid RefundApplyQry qry) {
        return Response.of(refundApplyService.listRefundApply(qry));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefundApply,
    // value = CommonLogTypeConstantDTO.AdminRefundApplyAdd)
    // @PostMapping()
    // @ApiOperation(value = "退款单申请新增")
    // @SearchMethod
    // public Response createRefundApply(@Valid @RequestBody RefundApplyCreateCmd cmd) {
    // refundApplyService.createRefundApply(cmd);
    // return Response.buildSuccess();
    // }
    //
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefundApply,
        value = CommonLogTypeConstantDTO.AdminRefundApplyUpdate)
    @PutMapping("/manualConfirmRefundApply")
    @ApiOperation(value = "退款单申请更新 /退回账户余额/其他操作/拒绝操作")
    @SearchMethod
    public Response manualConfirmRefundApply(@Valid @RequestBody RefundApplyManualConfirmCmd cmd) {
        refundApplyService.manualConfirmRefundApply(cmd);
        return Response.buildSuccess();
    }
    //
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefundApply,
    // value = CommonLogTypeConstantDTO.AdminRefundApplyDeleteById)
    // @DeleteMapping()
    // @ApiOperation(value = "退款单申请删除")
    // @SearchMethod
    // public Response deleteRefundApply(@Valid @RequestBody RefundApplyId id) {
    // refundApplyService.deleteRefundApply(id.getId());
    // return Response.buildSuccess();
    // }

}
