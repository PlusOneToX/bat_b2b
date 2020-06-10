package com.bat.flexible.manager.shop.executor;

import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.shop.ShopDOMapper;
import com.bat.flexible.dao.shop.co.ShopPageCO;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopQryExe {

    @Autowired
    private ShopDOMapper shopDOMapper;


    @Cached(name = FlexibleKeyConstant.SHOP_DO_PRE,key = "#id")
    public ShopDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        ShopDO shopDO = shopDOMapper.selectByPrimaryKey(id);
        if(shopDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        if(FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(shopDO.getDelFlag())){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_DATA_DEL_ALREADY);
        }
        return shopDO;
    }


    public List<ShopPageCO> listCOByCondition(Short openFlag, String content,Integer distributorId,String appName,String shopCode,
                                              String shopName) {
        return shopDOMapper.listCOByCondition(openFlag,content,distributorId,appName,shopCode,shopName);
    }

    public ShopDO getByDistributorIdAndShopCode(Integer distributorId, String shopCode) {
        return shopDOMapper.getByDistributorIdAndShopCode(distributorId,shopCode);
    }

    public List<Integer> searchUserConfigIdByShopName(String shopName) {
        return shopDOMapper.searchUserConfigIdByShopName(shopName);
    }

    public List<ShopDO> listByCondition(Integer userConfigId) {
        return shopDOMapper.listByCondition(userConfigId);
    }
}
