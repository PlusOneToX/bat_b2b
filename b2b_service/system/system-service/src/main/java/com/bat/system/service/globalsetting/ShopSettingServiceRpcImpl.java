package com.bat.system.service.globalsetting;

import javax.annotation.Resource;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.api.globalsetting.dto.data.ShopSettingDTO;
import com.bat.system.service.globalsetting.executor.ErrorCode;
import com.bat.system.service.globalsetting.executor.ShopSettingQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.storesetting.api.SystemGlobeShopSettingServiceRpc;
import com.bat.dubboapi.system.storesetting.dto.ShopSettingRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/11 14:29
 */
@DubboService
public class ShopSettingServiceRpcImpl implements SystemGlobeShopSettingServiceRpc {

    @Resource
    private ShopSettingQryExc shopSettingQryExc;

    @Override
    public Response<Integer> getNewproductTime() {
        return Response.of(shopSettingQryExc.getShopSettingByKey("newproduct_time"));
    }

    @Override
    public Response<ShopSettingRpcDTO> getSystemConfig() {
        ShopSettingDTO shopSetting = shopSettingQryExc.getShopSetting();
        if (shopSetting != null) {
            ShopSettingRpcDTO rpcDTO = new ShopSettingRpcDTO();
            BeanUtils.copyProperties(shopSetting, rpcDTO);
            return Response.of(rpcDTO);
        }
        return Response.buildFailure(ErrorCode.B_SHOP_SETTING_NULL, MessageUtils.get(ErrorCode.B_SHOP_SETTING_NULL));
    }
}
