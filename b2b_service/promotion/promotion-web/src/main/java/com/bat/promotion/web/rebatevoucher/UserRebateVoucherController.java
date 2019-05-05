package com.bat.promotion.web.rebatevoucher;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.web.base.BaseController;
import com.bat.promotion.web.base.Response;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherUsageRecordListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.rebatevoucher.RebateVoucherService;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.web.annotation.SearchMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:30
 */
@Api(tags = "返利代金券前台接口", value = "UserRebateVoucherController")
@RestController
@RequestMapping("/promotion/v1/web/user/rebateVoucher")
public class UserRebateVoucherController extends BaseController {
    @Resource
    private RebateVoucherService rebateVoucherService;

    @ApiOperation(value = "根据搜索条件查找返利代金券列表(分页)")
    @GetMapping(value = "/list")
    @SearchMethod
    public Response<PageInfo<RebateVoucherDTO>> listRebateVoucher(@Valid RebateVoucherListQry qry) {
        PageInfo<RebateVoucherDTO> pageInfo = rebateVoucherService.listRebateVoucher(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "查询分销商可用代金券总额")
    @GetMapping(value = "/getAvailableRebateVoucherBalanceSum")
    public Response<BigDecimal> getAvailableRebateVoucherBalanceSum(Integer distributorId) {
        BigDecimal balanceSum = rebateVoucherService.getAvailableRebateVoucherBalanceSum(distributorId);
        return Response.of(balanceSum);
    }

    @ApiOperation(value = "代金券使用记录列表(分页)")
    @GetMapping("/listUsageRecord")
    public Response<PageInfo<RebateVoucherUsageRecordDTO>>
    listRebateVoucherUsageRecord(@Valid RebateVoucherUsageRecordListQry qry) {
        PageInfo<RebateVoucherUsageRecordDTO> pageInfo = rebateVoucherService.listRebateVoucherUsageRecord(qry);
        return Response.of(pageInfo);
    }
}
