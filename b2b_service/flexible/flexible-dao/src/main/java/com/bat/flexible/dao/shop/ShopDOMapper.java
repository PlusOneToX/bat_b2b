package com.bat.flexible.dao.shop;

import com.bat.flexible.dao.shop.co.ShopPageCO;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopDO record);

    int insertSelective(ShopDO record);

    ShopDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopDO record);

    int updateByPrimaryKey(ShopDO record);

    List<ShopPageCO> listCOByCondition(@Param("openFlag") Short openFlag, @Param("content") String content,@Param("distributorId") Integer distributorId,
                                       @Param("appName") String appName,@Param("shopCode") String shopCode,
                                       @Param("shopName") String shopName);

    ShopDO getByDistributorIdAndShopCode(@Param("distributorId") Integer distributorId,@Param("shopCode") String shopCode);

    List<Integer> searchUserConfigIdByShopName(@Param("shopName") String shopName);

    List<ShopDO> listByCondition(@Param("userConfigId") Integer userConfigId);

    void restoreUserConfigIdNull(@Param("userConfigId") Integer userConfigId);
}