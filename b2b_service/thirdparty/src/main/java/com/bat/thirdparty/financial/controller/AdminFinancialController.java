package com.bat.thirdparty.financial.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.dto.ThirdIdDTO;
import com.bat.thirdparty.common.dto.ThirdIdsDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.refund.dto.data.RefundBillSyncDTO;
import com.bat.thirdparty.financial.api.AdminFinancialServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/17 19:52
 */
@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/financial")
@Api(tags = "财务触发第三方调用后台接口")
public class AdminFinancialController extends BaseController {

    @Resource
    private AdminFinancialServiceI adminFinancialServiceI;

    @PutMapping(value = "/syncVouchersToERP")
    @ApiOperation(value = "同步收款单给ERP")
    public Response syncVouchersToERP(@Valid @RequestBody ThirdIdsDTO thirdIdDTO) {
        return adminFinancialServiceI.syncVouchersToERP(thirdIdDTO.getIds());
    }

    @PutMapping(value = "/syncRefundBillToERP")
    @ApiOperation(value = "同步退款单给ERP")
    public Response syncRefundBillToERP(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        RefundBillSyncDTO dto = new RefundBillSyncDTO();
        dto.setId(thirdIdDTO.getId());
        return adminFinancialServiceI.syncRefundBillToErp(dto);
    }

}
