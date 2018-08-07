package com.bat.financial.web.subaccount;

import com.bat.financial.api.base.FinancialIdDTO;
import com.bat.financial.api.subaccount.OrderSubAccountBillServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountBillDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "订单分账流水后台接口")
@RestController
@RequestMapping("/financial/v1/web/admin/orderSubAccountBill")
public class AdminOrderSubAccountBillController extends BaseController {

    @Autowired
    private OrderSubAccountBillServiceI orderSubAccountBillServiceI;




    @GetMapping(value = "/listByOrderSubAccountId")
    @ApiOperation(value = "查询分账详情列表")
    public Response<List<OrderSubAccountBillDTO>> listByOrderSubAccountId(@Valid FinancialIdDTO financialIdDTO){
        List<OrderSubAccountBillDTO> billDTOS = orderSubAccountBillServiceI.listDTOByOrderSubAccountId(financialIdDTO.getId());

        return Response.of(billDTOS);
    }

}
