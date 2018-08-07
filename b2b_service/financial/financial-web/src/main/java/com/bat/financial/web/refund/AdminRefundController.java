package com.bat.financial.web.refund;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.refund.RefundDistributorService;
import com.bat.financial.api.refund.dto.RefundId;
import com.bat.financial.api.refund.dto.RefundQry;
import com.bat.financial.api.refund.dto.data.RefundDistributorDTO;
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
@Api(tags = "销售往来单据-退款单列表后台接口", value = "AdminRefundController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/refund")
public class AdminRefundController extends BaseController {
    @Resource
    private RefundDistributorService refundService;

    @GetMapping()
    @ApiOperation(value = "通过id查询退款单详情")
    @SearchMethod
    public Response<RefundDistributorDTO> getRefundBy(@Valid RefundId id) {
        return Response.of(refundService.getRefundBy(id.getId()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "退款单列表")
    @SearchMethod
    public Response<PageInfo<RefundDistributorDTO>> listRefund(@Valid RefundQry qry) {
        return Response.of(refundService.listRefund(qry));
    }

    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefund,
    // value = CommonLogTypeConstantDTO.AdminRefundAdd)
    // @PostMapping()
    // @ApiOperation(value = "退款单新增")
    // @SearchMethod
    // public Response createRefund(@Valid @RequestBody RefundCreateCmd cmd) {
    // refundService.createRefund(cmd);
    // return Response.buildSuccess();
    // }
    //
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefund,
    // value = CommonLogTypeConstantDTO.AdminRefundUpdate)
    // @PutMapping()
    // @ApiOperation(value = "退款单更新")
    // @SearchMethod
    // public Response updateRefund(@Valid @RequestBody RefundUpdateCmd cmd) {
    // refundService.updateRefund(cmd);
    // return Response.buildSuccess();
    // }
    //
    // @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminRefund,
    // value = CommonLogTypeConstantDTO.AdminRefundDeleteById)
    // @DeleteMapping()
    // @ApiOperation(value = "退款单删除")
    // @SearchMethod
    // public Response deleteRefund(@Valid @RequestBody RefundId id) {
    // refundService.deleteRefund(id.getId());
    // return Response.buildSuccess();
    // }
}
