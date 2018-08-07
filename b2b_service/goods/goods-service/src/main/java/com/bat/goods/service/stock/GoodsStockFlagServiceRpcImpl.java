package com.bat.goods.service.stock;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.service.common.Constant;
import com.bat.goods.service.goods.executor.GoodsQryExe;
import com.bat.goods.service.stock.executor.GoodsStockFlagCmdExe;
import com.bat.goods.service.stock.executor.GoodsStockFlagQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.stock.api.GoodsStockFlagServiceRpc;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.goods.api.goods.dto.GoodsItemSaleStatusCmd;
import com.bat.goods.api.utils.BeanUtils;
import com.bat.goods.dao.goods.dataobject.GoodsItemStatusDO;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;
import com.bat.goods.service.goods.convertor.GoodsConvertor;
import com.bat.goods.service.goods.executor.GoodsCmdExe;

@DubboService
public class GoodsStockFlagServiceRpcImpl implements GoodsStockFlagServiceRpc {

    @Resource
    private GoodsStockFlagCmdExe goodsStockFlagCmdExe;

    @Resource
    private GoodsStockFlagQryExe goodsStockFlagQryExe;

    @Resource
    private GoodsQryExe goodsQryExe;

    @Resource
    private GoodsCmdExe goodsCmdExe;

    @Override
    public Response createList(List<GoodsStockFlagDTO> goodsStockFlagDTOList) {
        if (goodsStockFlagDTOList == null || goodsStockFlagDTOList.size() == 0) {
            return Response.buildSuccess();
        }
        goodsStockFlagDTOList =
            goodsStockFlagDTOList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(var -> var.getItemId() + ";" + var.getWarehouseIdArr()))),
                    ArrayList::new));
        List<GoodsStockFlagDO> goodsStockFlagDOList = BeanUtils.copyList(goodsStockFlagDTOList, GoodsStockFlagDO.class);
        Date date = new Date();
        goodsStockFlagDOList.stream().forEach(goodsStockFlagDO -> {
            goodsStockFlagDO.setUpdateTime(date);
        });
        try {
            goodsStockFlagCmdExe.batchCreate(goodsStockFlagDOList);
        } catch (DuplicateKeyException e) {
            updateList(goodsStockFlagDTOList);
        }
        return Response.buildSuccess();
    }

    /**
     * 更新商品是否有货（且自动上下架项目终止的商品）
     * 
     * @param goodsStockFlagDTOList
     * @return
     */
    @Override
    public Response updateList(List<GoodsStockFlagDTO> goodsStockFlagDTOList) {
        if (CollectionUtils.isEmpty(goodsStockFlagDTOList)) {
            return Response.buildSuccess();
        }
        goodsStockFlagDTOList =
            goodsStockFlagDTOList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(Comparator.comparing(var -> var.getItemId() + ";" + var.getWarehouseIdArr()))),
                    ArrayList::new));
        List<GoodsStockFlagDO> updateList = new ArrayList<>();
        List<GoodsStockFlagDO> createList = new ArrayList<>();
        Date date = new Date();
        goodsStockFlagDTOList.stream().forEach(goodsStockFlagDTO -> {
            GoodsStockFlagDO goodsStockFlagDO = goodsStockFlagQryExe
                .getByItemIdAndWarehouseIdArr(goodsStockFlagDTO.getItemId(), goodsStockFlagDTO.getWarehouseIdArr());
            if (goodsStockFlagDO != null
                && !goodsStockFlagDO.getUnderStockFlag().equals(goodsStockFlagDTO.getUnderStockFlag())) {
                // 修改了才放进来
                goodsStockFlagDO.setUpdateTime(date);
                goodsStockFlagDO.setUnderStockFlag(goodsStockFlagDTO.getUnderStockFlag());
                goodsStockFlagDO.setWhetherOutOfStockInTransit(goodsStockFlagDTO.getWhetherOutOfStockInTransit());
                updateList.add(goodsStockFlagDO);
            } else if (goodsStockFlagDO == null) {
                goodsStockFlagDO = BeanUtils.copy(goodsStockFlagDTO, GoodsStockFlagDO.class);
                goodsStockFlagDO.setUpdateTime(date);
                createList.add(goodsStockFlagDO);
            }
        });
        if (!CollectionUtils.isEmpty(updateList)) {
            goodsStockFlagCmdExe.batchUpdate(updateList);
        }
        if (!CollectionUtils.isEmpty(createList)) {
            goodsStockFlagCmdExe.batchCreate(createList);
        }
        // 商品和货品自动上下架(只针对项目终止的商品)
        List<Integer> itemIds =
            goodsStockFlagDTOList.stream().map(GoodsStockFlagDTO::getItemId).collect(Collectors.toList());
        List<GoodsItemStatusDO> goodsItemStatusDOS =
            goodsQryExe.listGoodsItemStatusByItemIdsAndLifeCycle(itemIds, Constant.LIFE_CYCLE_5);
        List<GoodsItemSaleStatusCmd> saleStatusCmds =
            GoodsConvertor.toGoodsItemSaleStatusCmdList(goodsStockFlagDTOList, goodsItemStatusDOS);
        if (!CollectionUtils.isEmpty(saleStatusCmds)) {
            saleStatusCmds.forEach(saleStatusCmd -> {
                goodsCmdExe.saleStatusGoodsItem(saleStatusCmd);
            });
        }
        return Response.buildSuccess();
    }

    /**
     * 删除货品缺货标记
     * 
     * @param itemIdList
     * @return
     */
    @Override
    public Response deleteByItemIdList(List<Integer> itemIdList) {
        if (itemIdList == null || itemIdList.size() == 0) {
            return Response.buildSuccess();
        }
        goodsStockFlagCmdExe.deleteByItemIdList(itemIdList);
        return Response.buildSuccess();
    }
}
