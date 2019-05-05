package com.bat.thirdparty.factory.maike.service;

import com.bat.thirdparty.factory.maike.service.dto.MaikeDeliverOrderResp;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.factory.api.FactoryService;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:06
 */
@Component("MaikeFactoryServiceImpl")
public class MaikeFactoryServiceImpl implements FactoryService {

    @Resource
    private MaikeServiceImpl maikeService;

    @Override
    public String syncOrder(FactoryOrderAddCmd data) {
        MaikeOrderAddCmd maikeOrderAddCmd = data.getMaikeOrderAddCmd();
        return maikeService.syncOrder(maikeOrderAddCmd);
    }

    @Override
    public void deliverOrder(FactoryDeliverOrderReq resp) {
        MaikeDeliverOrderResp maikeResp = resp.getMaikeResp();
        ResponseBaseBean responseBaseBean =
            maikeService.deliverOrder(maikeResp.getRequest(), maikeResp.getSignature(), maikeResp.getTimestamp());
        if (!responseBaseBean.getCode().equals(0)) {
            throw ThirdPartyException.buildException(responseBaseBean.getCode() + "", responseBaseBean.getMsg());
        }
    }

    @Override
    public void diyOrderSyncToFactory(String startTime) {

    }
}
