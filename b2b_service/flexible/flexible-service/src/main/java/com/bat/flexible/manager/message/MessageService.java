package com.bat.flexible.manager.message;

import com.bat.flexible.mq.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    @Autowired
    private SenderService senderService;


    public void test() {
        CreateReceiveBillEntryRequest createReceiveBillEntryRequest = new CreateReceiveBillEntryRequest();
        createReceiveBillEntryRequest.setFB2B_BILLNO("2222");
        createReceiveBillEntryRequest.setFBillTypeID("typeid");
        senderService.sendObject(createReceiveBillEntryRequest,"syncErpFlexbileOrderDelivery","syncErpFlexbileOrderDelivery-"+"FX");
    }
}
