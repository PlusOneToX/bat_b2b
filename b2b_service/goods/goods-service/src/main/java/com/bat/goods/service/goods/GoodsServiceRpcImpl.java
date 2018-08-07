package com.bat.goods.service.goods;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.goods.executor.GoodsRpcCmdExe;
import org.apache.dubbo.config.annotation.DubboService;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.*;
import com.bat.dubboapi.goods.goods.dto.data.*;
import com.bat.goods.service.goods.executor.GoodsRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/28 10:24
 */
@DubboService
public class GoodsServiceRpcImpl implements GoodsServiceRpc {

    @Resource
    private GoodsRpcQryExe goodsRpcQryExe;

    @Resource
    private GoodsRpcCmdExe goodsRpcCmdExe;

    @Override
    public Response<PageInfo<UserGoodsRpcDTO>> listGoods(UserGoodsListRpcQry qry) {
        return Response.of(goodsRpcQryExe.listGoods(qry));
    }

    @Override
    public Response insertUpdateGoodsStoreColumn(List<GoodsStoreColumnRpcCmd> list) {
        goodsRpcCmdExe.insertUpdateGoodsStoreColumn(list);
        return Response.buildSuccess();
    }

    @Override
    public Response clearanceGoodsStoreColumn(Integer columnId) {
        goodsRpcCmdExe.clearanceGoodsStoreColumn(columnId);
        return Response.buildSuccess();
    }

    @Override
    public Response clearGoodsStoreColumn(Integer columnId) {
        goodsRpcCmdExe.clearGoodsStoreColumn(columnId);
        return Response.buildSuccess();
    }

    @Override
    public Response insertUpdateGoodsStoreSection(List<GoodsStoreSectionRpcCmd> list) {
        goodsRpcCmdExe.insertUpdateGoodsStoreSection(list);
        return Response.buildSuccess();
    }

    @Override
    public Response insertUpdateGoodsStoreMobile(List<GoodsStoreMobileRpcCmd> list) {
        goodsRpcCmdExe.insertUpdateGoodsStoreMobile(list);
        return Response.buildSuccess();
    }

    @Override
    public Response<PageInfo<GoodsStoreColumnRpcDTO>> listGoodsStoreColumn(GoodsStoreListRpcQry qry) {
        PageInfo<GoodsStoreColumnRpcDTO> pageInfo = goodsRpcQryExe.listGoodsStoreColumn(qry);
        return Response.of(pageInfo);
    }

    @Override
    public Response<PageInfo<GoodsStoreSectionRpcDTO>> listGoodsStoreSection(GoodsStoreListRpcQry qry) {
        PageInfo<GoodsStoreSectionRpcDTO> pageInfo = goodsRpcQryExe.listGoodsStoreSection(qry);
        return Response.of(pageInfo);
    }

    @Override
    public Response<PageInfo<GoodsStoreMobileRpcDTO>> listGoodsStoreMobile(GoodsStoreListRpcQry qry) {
        PageInfo<GoodsStoreMobileRpcDTO> pageInfo = goodsRpcQryExe.listGoodsStoreMobile(qry);
        return Response.of(pageInfo);
    }

    @Override
    public Response deleteGoodsStoreColumnByColumnId(Integer columnId) {
        goodsRpcCmdExe.deleteGoodsStoreColumnByColumnId(columnId);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteGoodsStoreColumnBySectionId(Integer sectionId) {
        goodsRpcCmdExe.deleteGoodsStoreColumnBySectionId(sectionId);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteGoodsStoreMobileByMobileId(Integer mobileId) {
        goodsRpcCmdExe.deleteGoodsStoreMobileByMobileId(mobileId);
        return Response.buildSuccess();
    }

    @Override
    public Response<List<GoodsItemRpcDTO>> listGoodsItemByIds(List<Integer> ids) {
        List<GoodsItemRpcDTO> dtos = goodsRpcQryExe.listGoodsItem(ids);
        return Response.of(dtos);
    }

    @Override
    public Response<List<GoodsItemRpcDTO>> listGoodsItemByCodes(List<String> codes) {
        List<GoodsItemRpcDTO> dtos = goodsRpcQryExe.listGoodsItemByCodes(codes);
        return Response.of(dtos);
    }

    @Override
    public Response<GoodsItemRpcDTO> goodsItemByCode(String itemCode) {
        GoodsItemRpcDTO dto = goodsRpcQryExe.goodsItemByCode(itemCode);
        return Response.of(dto);
    }

    @Override
    public Response<List<GoodsRpcDTO>> listGoods(List<Integer> list) {
        List<GoodsRpcDTO> dtos = goodsRpcQryExe.listGoods(list);
        return Response.of(dtos);
    }

    @Override
    public Response<List<GoodsItemOnwaySaleFlagRpcDTO>> listGoodsItemOnwaySaleFlag(List<Integer> list) {
        List<GoodsItemOnwaySaleFlagRpcDTO> dtos = goodsRpcQryExe.listGoodsItemOnwaySaleFlag(list);
        return Response.of(dtos);
    }

    @Override
    public Response goodsPromotionPriceByPromotion(GoodsPromotionRpcCmd goodsPromotionRpcCmd) {
        goodsRpcCmdExe.goodsPromotionPriceByPromotion(goodsPromotionRpcCmd);
        return Response.buildSuccess();
    }

    @Override
    public Response goodsPromotionPriceByGroupSeckill(GoodsGroupSeckillRpcCmd groupSeckillRpcCmd) {
        goodsRpcCmdExe.goodsPromotionPriceByGroupSeckill(groupSeckillRpcCmd);
        return Response.buildSuccess();
    }

    @Override
    public Response deletePromotionPriceByPromotionId(Integer promotionId) {
        goodsRpcCmdExe.deletePromotionPriceByPromotionId(promotionId);
        return Response.buildSuccess();
    }

    @Override
    public Response deletePromotionPriceByGroupSeckillId(Integer groupSeckillId) {
        goodsRpcCmdExe.deletePromotionPriceByGroupSeckillId(groupSeckillId);
        return Response.buildSuccess();
    }

    @Override
    public Response<PageInfo<GoodsItemRpcDTO>> listDistributorGoodsItem(UserGoodsItemListRpcQry qry) {
        PageInfo<GoodsItemRpcDTO> pageInfo = goodsRpcQryExe.listGoodsItem(qry);
        return Response.of(pageInfo);
    }

    @Override
    public Response<PageInfo<UserGoodsRpcDTO>> listDistributorGoodsSort(UserGoodsSortListRpcQry qry) {
        PageInfo<UserGoodsRpcDTO> pageInfo = goodsRpcQryExe.listDistributorGoods(qry);
        return Response.of(pageInfo);
    }

    @Override
    public Response<List<GoodsItemPriceRpcDTO>> listDistributorGoodsItemPrice(GoodsItemPriceRpcQry qry) {
        List<GoodsItemPriceRpcDTO> rpcDTOS = goodsRpcQryExe.listDistributorGoodsItemPrice(qry);
        return Response.of(rpcDTOS);
    }

    @Override
    public Response<List<GoodsItemPriceRpcDTO>> listGoodsItemRetailPrice(List<Integer> itemIds) {
        List<GoodsItemPriceRpcDTO> rpcDTOS = goodsRpcQryExe.listGoodsItemRetailPrice(itemIds);
        return Response.of(rpcDTOS);
    }

    @Override
    public Response<List<GoodsItemBoxRpcDTO>> listGoodsItemBoxList(List<Integer> itemIds) {
        List<GoodsItemBoxRpcDTO> rpcDTOS = goodsRpcQryExe.listGoodsItemBoxList(itemIds);
        return Response.of(rpcDTOS);
    }

    @Override
    public Response<Short> getSaleStatusByItemId(Integer itemId) {
        GoodsItemRpcDTO goodsItemRpcDTO = goodsRpcQryExe.goodsItemByItemId(itemId);
        return Response.of(goodsItemRpcDTO == null ? null : goodsItemRpcDTO.getSaleStatus());
    }

    @Override
    public Response<List<Integer>> getVisibleGoodsIdsByDistributor(Integer distributorId, List<BrandScaleRpc> scaleRpcs,
        Integer saleId, Integer departmentId, List<Integer> brandIds, String distributorGroupIds) {
        List<Integer> goodsIds = goodsRpcQryExe.getVisibleGoodsIdsByDistributor(distributorId, scaleRpcs, saleId,
            departmentId, brandIds, distributorGroupIds);
        return Response.of(goodsIds);
    }

    @Override
    public Response<Integer> getGoodsIdByGoodsNo(String goodsNo) {
        Integer goodsId = goodsRpcQryExe.getGoodsIdByGoodsNo(goodsNo);
        return Response.of(goodsId);
    }

    @Override
    public Response<List<GoodsItemRpc>> getGoodsItemIdsByItemCodeOrBarCode(String itemCodeOrBarCode) {
        List<GoodsItemRpc> goodsItemRpcs = goodsRpcQryExe.getGoodsItemIdsByItemCodeOrBarCode(itemCodeOrBarCode);
        return Response.of(goodsItemRpcs);
    }

    @Override
    public Response<List<StopGoodsItemRpcDTO>> listLifeCycleStopGoodsItem(Short saleStatus) {
        List<StopGoodsItemRpcDTO> list = goodsRpcQryExe.listLifeCycleStopGoodsItem(saleStatus);
        return Response.of(list);
    }

    @Override
    public Response updateGoodsItemSaleStatusByIds(List<Integer> ids, Short saleStatus) {
        goodsRpcCmdExe.updateGoodsItemSaleStatusByIds(ids, saleStatus);
        return Response.buildSuccess();
    }

    /**
     * 获取所有货品ids
     * 
     * @return
     */
    @Override
    public Response<List<Integer>> getAllItemIds() {
        List<Integer> allItemIds = goodsRpcQryExe.getAllItemIds();
        return Response.of(allItemIds);
    }

    @Override
    public Response<List<GoodsItemRpc>> getAllGoodsItem() {
        List<GoodsItemRpc> allGoodsItem = goodsRpcQryExe.getAllGoodsItem();
        return Response.of(allGoodsItem);
    }

    @Override
    public Response<List<Integer>> allVisibleProducts() {
        List<Integer> allVisibleProducts = goodsRpcQryExe.allVisibleProducts();
        return Response.of(allVisibleProducts);
    }

    @Override
    public Response<List<Integer>> allVisibleBrands() {
        List<Integer> allVisibleBrands = goodsRpcQryExe.allVisibleBrands();
        return Response.of(allVisibleBrands);
    }

    @Override
    public Response<List<GoodsAreVisibleRpcDTO>> goodsAreVisible(List<Integer> orderGoodsList) {
        List<GoodsAreVisibleRpcDTO> goodsAreVisibleList = goodsRpcQryExe.goodsAreVisible(orderGoodsList);
        return Response.of(goodsAreVisibleList);
    }

    /**
     * <h2>根据货品id查询货品零售价</h2>
     * 
     * @param itemId
     * @return
     */
    @Override
    public Response<BigDecimal> queryRetailPriceByItemId(Integer itemId) {
        BigDecimal retailPrice = goodsRpcQryExe.queryRetailPriceByItemId(itemId);
        return Response.of(retailPrice);
    }

    /**
     * <h2>根据部门id查询部门所有可视的商品</h2>
     * 
     * @param departmentID
     * @return
     */
    @Override
    public Response<List<Integer>> goodsAreVisibleByDepId(Integer departmentID) {
        List<Integer> list = goodsRpcQryExe.goodsAreVisibleByDepId(departmentID);
        return Response.of(list);
    }
}
