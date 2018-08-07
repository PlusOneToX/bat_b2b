package com.bat.financial.web.subaccount;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bat.financial.api.base.FinancialIdDTO;
import com.bat.financial.api.base.Response;
import com.bat.financial.api.subaccount.OrderSubAccountBillServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillBatchCmd;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillDTO;
import com.bat.financial.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "订单分账流水前台接口")
@RestController
@RequestMapping("/financial/v1/web/user/orderSubAccountBill")
public class UserOrderSubAccountBillController extends BaseController {

    @Autowired
    private OrderSubAccountBillServiceI orderSubAccountBillServiceI;

    @GetMapping(value = "/listByOrderSubAccountId")
    @ApiOperation(value = "查询分账详情列表")
    public Response<List<OrderSubAccountBillDTO>> listByOrderSubAccountId(@Valid FinancialIdDTO financialIdDTO) {
        List<OrderSubAccountBillDTO> billDTOS =
            orderSubAccountBillServiceI.listDTOByOrderSubAccountId(financialIdDTO.getId());
        return Response.of(billDTOS);
    }

    @PutMapping(value = "/updateActualSubAccountAmount")
    @ApiOperation(value = "修改分账金额、实时分账")
    public Response updateActualSubAccountAmount(@Valid @RequestBody OrderSubAccountBillBatchCmd billBatchCmd) {
        return orderSubAccountBillServiceI.updateActualSubAccountAmount(billBatchCmd);

    }

    @PutMapping(value = "/subAccountAgain")
    @ApiOperation(value = "重新分账")
    public Response subAccountAgain(@Valid @RequestBody FinancialIdDTO financialIdDTO) {
        return orderSubAccountBillServiceI.subAccountAgain(financialIdDTO.getId());
    }

}
