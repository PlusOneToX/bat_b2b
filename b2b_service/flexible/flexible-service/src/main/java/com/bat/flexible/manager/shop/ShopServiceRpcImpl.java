package com.bat.flexible.manager.shop;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.shop.executor.ShopCmdExe;
import com.bat.flexible.manager.shop.executor.ShopQryExe;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.shop.ShopServiceRpc;
import com.bat.dubboapi.flexible.shop.dto.ShopDTORpcQry;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ShopServiceRpcImpl implements ShopServiceRpc {

    @Autowired
    private ShopQryExe shopQryExe;

    @Autowired
    private ShopCmdExe shopCmdExe;

    /**
     * 根据店铺名称模糊查询分销商分账配置id列表
     * @param shopName
     * @return
     */
    @Override
    public Response<List<Integer>> searchUserConfigIdByShopName(String shopName) {
        if(StringUtils.isBlank(shopName)){
            return Response.of(null);
        }
        List<Integer> list =  shopQryExe.searchUserConfigIdByShopName(shopName);
        return Response.of(list);
    }

    /**
     * 根据分销商id和门店编码查询门店
     * @param distributorId
     * @param shopCode
     * @return
     */
    @Override
    public Response<ShopDTORpcQry> getByDistributorIdAndShopCode(Integer distributorId, String shopCode) {
        ShopDO shopDO = shopQryExe.getByDistributorIdAndShopCode(distributorId, shopCode);
        return Response.of(BeanUtils.copy(shopDO,ShopDTORpcQry.class));
    }

    @Override
    public Response restoreUserConfigIdNull(Integer userConfigId) {
        shopCmdExe.restoreUserConfigIdNull(userConfigId);
        return Response.buildSuccess();
    }
}
