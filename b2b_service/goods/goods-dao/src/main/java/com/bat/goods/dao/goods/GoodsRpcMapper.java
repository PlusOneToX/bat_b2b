package com.bat.goods.dao.goods;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.goods.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.goods.dao.goods.dataobject.*;

@Mapper
public interface GoodsRpcMapper {

    List<UserGoodsNameDO> listGoodsNameByDistributorId(Map map);

    List<GoodsItemListDO> listGoodsItemByIds(@Param("ids") List<Integer> ids);

    List<GoodsItemListDO> listGoodsItemByCodes(@Param("codes") List<String> codes);

    GoodsItemListDO listGoodsItemByCode(@Param("code") String code);

    List<GoodsListDO> listGoodsByIds(@Param("ids") List<Integer> ids);

    List<GoodsItemOnwaySaleFlagDO> listGoodsItemOnwaySaleFlagByIds(@Param("ids") List<Integer> ids);

    List<UserGoodsNameDO> listGoodsNameByItemCodes(@Param("itemCodes") List<String> itemCodes);

    List<GoodsItemListDO> listDistributorGoodsItem(Map map);

    List<UserGoodsRpcDO> listDistributorGoods(Map map);

    List<Integer> listGoodsIdByDistributorId(@Param("distributorId") Integer distributorId,
        @Param("brandIds") List<Integer> brandIds);

    List<Integer> listGoodsIdByBrandScale(@Param("scalePriceId") Integer scalePriceId,
        @Param("brandId") Integer brandId, @Param("brandIds") List<Integer> brandIds);

    List<Integer> listGoodsIdByAdminId(@Param("adminId") Integer adminId, @Param("brandIds") List<Integer> brandIds);

    List<Integer> listGoodsIdByDepartmentId(@Param("departmentId") Integer departmentId,
        @Param("brandIds") List<Integer> brandIds);

    List<Integer> listGoodsIdByDistributorGroupId(@Param("distributorGroupIds") List<Integer> distributorGroupIds,
        @Param("brandIds") List<Integer> brandIds);

}
