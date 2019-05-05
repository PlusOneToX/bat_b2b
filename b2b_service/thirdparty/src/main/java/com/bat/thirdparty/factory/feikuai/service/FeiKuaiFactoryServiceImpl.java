package com.bat.thirdparty.factory.feikuai.service;

import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.factory.api.FactoryExpandService;
import com.bat.thirdparty.factory.api.FactoryService;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;

import com.bat.thirdparty.factory.feikuai.executor.FeiKuaiCmdExe;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:08
 */
@Service("FeiKuaiFactoryServiceImpl")
public class FeiKuaiFactoryServiceImpl implements FactoryService, FactoryExpandService {

    @Resource
    private FeiKuaiCmdExe cmdExe;


    @SneakyThrows
    @Override
    public String syncOrder(FactoryOrderAddCmd addCmd) {
        return cmdExe.syncOrder(addCmd);
    }

    @Override
    public void deliverOrder(FactoryDeliverOrderReq resp) {

    }

    @Override
    public void diyOrderSyncToFactory(String startTime) {

    }


    @Override
    public Response synchronizedLogisticsByOrderID(FactoryOrderQueCmd data) {
        cmdExe.synchronizedLogisticsByOrderID(data.getFactoryEnum().name(),data.getOrderNo());
        return Response.buildSuccess();
    }

    @Override
    public Response factoryTrackingNumber(FactoryOrderQueCmd data, String expressNo) {
        return null;
    }
}
