package com.bat.goods.service.goods;

// package by domain, not by duty

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.service.goods.executor.GoodsQryExe;
import com.bat.goods.service.scaleprice.executor.ScalePriceQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.goods.GoodsServiceI;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.dataobject.GoodsItemDO;
import com.bat.goods.service.goods.executor.GoodsCmdExe;
import com.bat.goods.service.goods.executor.GoodsRpcQryExe;

@Service
public class GoodsServiceImpl implements GoodsServiceI {

    @Resource
    private GoodsCmdExe cmdExe;

    @Resource
    private GoodsQryExe qryExe;

    @Resource
    private GoodsRpcQryExe rpcQryExe;

    @Resource
    private ScalePriceQryExe scalePriceQryExe;

    @Override
    public void createGoods(GoodsCmd cmd) {
        cmdExe.createGoods(cmd);
    }

    @Override
    public void updateGoods(GoodsCmd cmd) {
        cmdExe.updateGoods(cmd);
    }

    @Override
    public PageInfo<GoodsListDTO> listGoods(GoodsListQry qry) {
        return qryExe.executeGoodsList(qry);
    }

    @Override
    public PageInfo<GoodsItemListDTO> listGoodsItem(GoodsItemListQry qry) {
        return qryExe.executeGoodsItemList(qry);
    }

    @Override
    public void deleteGoods(GoodsId cmd) {
        cmdExe.deleteGoods(cmd);
    }

    @Override
    public GoodsDTO getGoods(GoodsId qry) {
        return qryExe.executeGoods(qry);
    }

    @Override
    public void saleStatusGoods(GoodsSaleStatusCmd cmd) {
        cmdExe.saleStatusGoods(cmd);
    }

    @Override
    public void saleStatusGoodsItem(GoodsItemSaleStatusCmd cmd) {
        cmdExe.saleStatusGoodsItem(cmd);
    }

    @Override
    public void freezeStatusGoods(GoodsFreezeStatusCmd cmd) {
        cmdExe.freezeStatusGoods(cmd);
    }

    @Override
    public PageInfo<GoodsItemErpDTO> listGoodsItemErp(GoodsItemListErpQry qry, String userId) {
        return rpcQryExe.listGoodsItemErp(qry, userId);
    }

    @Override
    public void updateGoodsItemBoxs(List<GoodsItemBoxCmd> cmds) {
        cmdExe.updateGoodsItemBoxs(cmds);
    }

    @Override
    public List<GoodsItemBoxDTO> listGoodsItemBox(GoodsItemId qry) {
        return qryExe.listGoodsItemBox(qry);
    }

    @Override
    public PageInfo<GoodsListStoreDTO> columnGoodsList(GoodsListColumnQry qry) {
        return qryExe.columnGoodsList(qry);
    }

    @Override
    public PageInfo<GoodsListStoreDTO> sectionGoodsList(GoodsListSectionQry qry) {
        return qryExe.sectionGoodsList(qry);
    }

    @Override
    public PageInfo<GoodsListStoreDTO> mobileGoodsList(GoodsListMobileQry qry) {
        return qryExe.mobileGoodsList(qry);
    }

    @Override
    public List<GoodsListStoresDTO> mobileGoodsList(GoodsListMobilesQry qry) {
        List<GoodsListStoresDTO> list = new ArrayList<>();
        for (Integer mobileId : qry.getMobileIds()) {
            GoodsListMobileQry goodsListMobileQry = new GoodsListMobileQry();
            goodsListMobileQry.setMobileId(mobileId);
            goodsListMobileQry.setPage(qry.getPage());
            goodsListMobileQry.setSize(qry.getSize());
            goodsListMobileQry.setModuleType(qry.getModuleType());
            PageInfo<GoodsListStoreDTO> pageInfo = qryExe.mobileGoodsList(goodsListMobileQry);
            GoodsListStoresDTO goodsListStoresDTO = new GoodsListStoresDTO();
            goodsListStoresDTO.setMobileId(mobileId);
            goodsListStoresDTO.setPageInfo(pageInfo);
            list.add(goodsListStoresDTO);
        }
        return list;
    }

    @Override
    public List<GoodsListDTO> goodsListByClassifyIds(BaseIds qry) {
        return qryExe.goodsListByClassifyIds(qry);
    }

    @Override
    public List<GoodsListDTO> goodsListByIds(BaseIds qry) {
        return qryExe.goodsListByIds(qry);
    }

    @Override
    public List<GoodsItemDO> listAllItem() {
        return qryExe.listAllItem();
    }

    @Override
    public PageInfo<GoodsItemSimpleDTO> pageAssignByDistributorId(DistributorGoodsQuery goodsQuery) {
        return qryExe.pageAssignByDistributorId(goodsQuery);
    }

    @Override
    public PageInfo<GoodsItemSimpleDTO> pageByGoodsTypeAndDiyType(DiyGoodsPageQry diyGoodsPageQry) {
        return qryExe.pageByGoodsTypeAndDiyType(diyGoodsPageQry);
    }

    @Override
    public void syncBatchGoodsPrice(BaseIds cmd) {
        cmdExe.syncBatchGoodsPrice(cmd);
    }

    @Override
    public void syncAllGoodsPrice() {
        cmdExe.syncAllGoodsPriceMessage();
    }

    @Override
    public void syncBatchGoodsItem(BaseIds cmd) {
        cmdExe.syncBatchGoodsItem(cmd);
    }

    @Override
    public void syncAllGoodsItem() {
        cmdExe.syncAllGoodsItemMessage();
    }

    @Override
    public void syncBatchGoodsItemBox(BaseIds cmd) {
        cmdExe.syncBatchGoodsItemBox(cmd);
    }

    @Override
    public void syncAllGoodsItemBox() {
        cmdExe.syncAllGoodsItemBoxMessage();
    }

    @Override
    public void allGoodsPrice() {
        cmdExe.allGoodsPrice();
    }

    @Override
    public void allGoodsScope() {
        cmdExe.syncAllGoodsScopeMessage();
    }
}