package com.bat.dubboapi.goods.goods.api;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.goods.dto.*;
import com.bat.dubboapi.goods.goods.dto.data.*;
import com.bat.dubboapi.goods.common.Response;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface GoodsServiceRpc {
    /**
     * 根据分销商id查询商品列表
     *
     * @param qry
     * @return
     */
    Response<PageInfo<UserGoodsRpcDTO>> listGoods(UserGoodsListRpcQry qry);

    /**
     * 新增更新栏目和商品关联关系
     *
     * @param cmds
     * @return
     */
    Response insertUpdateGoodsStoreColumn(List<GoodsStoreColumnRpcCmd> cmds);

    /**
     * 一键导入清仓商品（栏目）
     *
     * @param columnId
     * @return
     */
    Response clearanceGoodsStoreColumn(Integer columnId);

    /**
     * 一键清楚商品（栏目）
     *
     * @param columnId
     * @return
     */
    Response clearGoodsStoreColumn(Integer columnId);

    /**
     * 新增更新板块和商品关联关系
     *
     * @param cmds
     * @return
     */
    Response insertUpdateGoodsStoreSection(List<GoodsStoreSectionRpcCmd> cmds);

    /**
     * 新增更新移动端配置和商品关联关系
     *
     * @param cmds
     * @return
     */
    Response insertUpdateGoodsStoreMobile(List<GoodsStoreMobileRpcCmd> cmds);

    /**
     * 根据栏目id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    Response<PageInfo<GoodsStoreColumnRpcDTO>> listGoodsStoreColumn(GoodsStoreListRpcQry qry);

    /**
     * 根据板块id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    Response<PageInfo<GoodsStoreSectionRpcDTO>> listGoodsStoreSection(GoodsStoreListRpcQry qry);

    /**
     * 根据移动端配置id获取商品关系列表（分页）
     *
     * @param qry
     * @return
     */
    Response<PageInfo<GoodsStoreMobileRpcDTO>> listGoodsStoreMobile(GoodsStoreListRpcQry qry);

    /**
     * 根据栏目id删除所有商品关联关系
     *
     * @param columnId
     * @return
     */
    Response deleteGoodsStoreColumnByColumnId(Integer columnId);

    /**
     * 根据板块id删除所有商品关联关系
     *
     * @param sectionId
     * @return
     */
    Response deleteGoodsStoreColumnBySectionId(Integer sectionId);

    /**
     * 根据移动端配置id删除所有商品关联关系
     *
     * @param mobileId
     * @return
     */
    Response deleteGoodsStoreMobileByMobileId(Integer mobileId);

    /**
     * 根据货品ids获取货品信息
     *
     * @param goodsItemIds
     * @return
     */
    Response<List<GoodsItemRpcDTO>> listGoodsItemByIds(List<Integer> goodsItemIds);

    /**
     * 根据货品codes获取货品信息
     *
     * @param itemCodes
     * @return
     */
    Response<List<GoodsItemRpcDTO>> listGoodsItemByCodes(List<String> itemCodes);

    /**
     * 根据货品code获取货品信息
     *
     * @param itemCode
     * @return
     */
    Response<GoodsItemRpcDTO> goodsItemByCode(String itemCode);

    /**
     * 根据商品品ids获取商品信息
     *
     * @param goodsIds
     * @return
     */
    Response<List<GoodsRpcDTO>> listGoods(List<Integer> goodsIds);

    /**
     * 根据货品ids获取直发客户是否支持在途
     *
     * @param goodsItemIds
     * @return
     */
    Response<List<GoodsItemOnwaySaleFlagRpcDTO>> listGoodsItemOnwaySaleFlag(List<Integer> goodsItemIds);

    /**
     * 根据促销活动计算商品最佳价格
     *
     * @param cmd
     * @return
     */
    Response goodsPromotionPriceByPromotion(GoodsPromotionRpcCmd cmd);

    /**
     * 根据拼团秒杀活动计算商品最佳价格
     *
     * @param cmd
     * @return
     */
    Response goodsPromotionPriceByGroupSeckill(GoodsGroupSeckillRpcCmd cmd);

    /**
     * 根据促销活动id删除最佳价格
     *
     * @param promotionId
     * @return
     */
    Response deletePromotionPriceByPromotionId(Integer promotionId);

    /**
     * 根据拼团秒杀活动id删除最佳价格
     *
     * @param groupSeckillId
     * @return
     */
    Response deletePromotionPriceByGroupSeckillId(Integer groupSeckillId);

    /**
     * 根据商品ids或货品ids获取分销商可视货品列表（分页）
     *
     * @param qry
     * @return
     */
    Response<PageInfo<GoodsItemRpcDTO>> listDistributorGoodsItem(UserGoodsItemListRpcQry qry);

    /**
     * 根据分销商id和商品ids查询商品列表（分页，有序（顺序按商品ids顺序））
     *
     * @param qry
     * @return
     */
    Response<PageInfo<UserGoodsRpcDTO>> listDistributorGoodsSort(UserGoodsSortListRpcQry qry);

    /**
     * 根据分销商获取货品价格列表
     *
     * @return
     */
    Response<List<GoodsItemPriceRpcDTO>> listDistributorGoodsItemPrice(GoodsItemPriceRpcQry qry);

    /**
     * 根据货品建立零售价
     *
     * @param itemIds
     * @return
     */
    Response<List<GoodsItemPriceRpcDTO>> listGoodsItemRetailPrice(List<Integer> itemIds);

    /**
     * 根据货品ids获取装箱规格
     *
     * @param itemIds
     * @return
     */
    Response<List<GoodsItemBoxRpcDTO>> listGoodsItemBoxList(List<Integer> itemIds);

    /**
     * 根据货品id查询货品上架状态 1、未上架 2、审判中 3、已上架 null表示已被删除
     *
     * @param itemId
     * @return
     */
    Response<Short> getSaleStatusByItemId(Integer itemId);

    /**
     * 根据分销商刷新分销商商品可视范围
     *
     * @param distributorId
     * @param brandScaleRpcs
     * @param salesId
     * @param departmentId
     * @return
     */
    Response<List<Integer>> getVisibleGoodsIdsByDistributor(Integer distributorId, List<BrandScaleRpc> brandScaleRpcs,
        Integer salesId, Integer departmentId, List<Integer> brandIds, String distributorGroupIds);

    /**
     * 根据商品编码获取商品id
     *
     * @param goodsNo
     * @return
     */
    Response<Integer> getGoodsIdByGoodsNo(String goodsNo);

    /**
     * 根据货品编码或条形码获取货品ids
     *
     * @param itemCodeOrBarCode
     * @return
     */
    Response<List<GoodsItemRpc>> getGoodsItemIdsByItemCodeOrBarCode(String itemCodeOrBarCode);

    Response<List<StopGoodsItemRpcDTO>> listLifeCycleStopGoodsItem(Short saleStatus);

    /**
     * 根据ids修改上下架状态
     *
     * @param ids
     * @param saleStatus
     * @return
     */
    Response updateGoodsItemSaleStatusByIds(List<Integer> ids, Short saleStatus);

    /**
     * 获取所有货品ids
     *
     * @return
     */
    Response<List<Integer>> getAllItemIds();

    /**
     * 获取所有货品基本信息
     *
     * @return
     */
    Response<List<GoodsItemRpc>> getAllGoodsItem();

    /**
     * <h2>全部可视商品</h2>
     */
    Response<List<Integer>> allVisibleProducts();

    /**
     * <h2>全部可视品牌</h2>
     */
    Response<List<Integer>> allVisibleBrands();

    /**
     * <h2>查询商品可视与品牌</h2>
     */
    Response<List<GoodsAreVisibleRpcDTO>> goodsAreVisible(List<Integer> orderGoodsList);

    /**
     * <h2>根据货品ID查询零售价</h2>
     */
    Response<BigDecimal> queryRetailPriceByItemId(Integer itemId);

    /**
     * <h2>根据部门id查询部门所有可视的商品</h2>
     * 
     * @param departmentID
     * @return
     */
    Response<List<Integer>> goodsAreVisibleByDepId(Integer departmentID);

}
