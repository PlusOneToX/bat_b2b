package com.bat.financial.web.voucher;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.voucher.VoucherService;
import com.bat.financial.api.voucher.dto.VoucherQry;
import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
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
@Api(tags = "销售往来单据-收款单列表后台接口", value = "AdminVoucherController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/voucher")
public class AdminVoucherController extends BaseController {
    @Resource
    private VoucherService voucherService;

    @GetMapping("/list")
    @ApiOperation(value = "收款单列表")
    @SearchMethod
    public Response<PageInfo<VoucherDistributorDTO>> listVoucher(@Valid VoucherQry qry) {
        return Response.of(voucherService.listVoucher(qry));
    }

    @GetMapping()
    @ApiOperation(value = "收款单详情")
    public Response<VoucherDistributorDTO> getVoucher(VoucherQry qry) {
        return Response.of(voucherService.getVoucher(qry).get(0));
    }

}
