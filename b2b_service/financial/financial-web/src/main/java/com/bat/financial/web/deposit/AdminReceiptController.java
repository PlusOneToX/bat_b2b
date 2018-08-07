package com.bat.financial.web.deposit;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.financial.common.Response;
import com.bat.financial.api.deposit.ReceiptService;
import com.bat.financial.api.deposit.dto.ReceiptQry;
import com.bat.financial.api.deposit.dto.data.ReceiptDTO;
import com.bat.financial.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 未知不确定还做不做
 * @date: 2018/5/29 10:18
 */
@Api(tags = "预存款-收款列表后台接口", value = "AdminReceiptController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/deposit")
public class AdminReceiptController extends BaseController {
    @Resource
    private ReceiptService receiptService;

    @GetMapping("/receipt/list")
    @ApiOperation(value = "收款列表")
    public Response<PageInfo<ReceiptDTO>> listReceipt(@Valid ReceiptQry qry) {
        receiptService.listReceipt(qry);
        return Response.of(receiptService.listReceipt(qry));
    }

}
