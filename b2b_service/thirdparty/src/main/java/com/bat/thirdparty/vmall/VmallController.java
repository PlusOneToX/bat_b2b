/*
package com.bat.thirdparty.vmall;

import ReceiveService;
import OrderBaseOnIdCmd;
import VmallOrderServiceI;
import BopOrderStatusUpdateRequest;
import BopQueryShipmentRequest;
import VmallExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/thirdparty/lks")
public class VmallController {
    @Autowired
    private VmallOrderServiceI vmallOrderServiceI;

    @Autowired
    private VmallExe vmallExe;

    @Autowired
    private ReceiveService receiveService;

    @GetMapping("get")
    @ResponseBody
    public List<OrderBaseOnIdCmd> response() throws IOException {
       return vmallOrderServiceI.vmallOrderPull();
    }

    @GetMapping
    @ResponseBody
    public List<OrderBaseOnIdCmd> response1() throws IOException {
        BopOrderStatusUpdateRequest request=new BopOrderStatusUpdateRequest();
        BopOrderStatusUpdateRequest.shipmentUpdate obj=new BopOrderStatusUpdateRequest.shipmentUpdate();
        BopQueryShipmentRequest requestss=new BopQueryShipmentRequest();
        requestss.setOrderNo("55410004057");
        vmallExe.queryShipmentLis(requestss);
        obj.setExpressNo("632589663609");
        obj.setOrderCode("55240009428");
        obj.setLogisticsCompanyCode("ZTO");
        obj.setStatus("20");
        //obj.setShipmentNo("125454545445");
        List<BopOrderStatusUpdateRequest.shipmentUpdate> osd=new ArrayList<>();
        osd.add(obj);
        request.setShipmentList(osd);
        vmallExe.orderStatusUpdate(request);
        return null;
    }

    @GetMapping("get2")
    @ResponseBody
    public void respons2e() throws IOException {
        receiveService.orderToSumsung(174414);
    }

}
*/
