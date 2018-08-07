package com.bat.warehouse.manager.vmi.executor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.GoodsVmiStockErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemErpDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.vmi.GoodsVmiStockDOMapper;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

@Component
public class GoodsVmiStockCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsVmiStockCmdExe.class);

    @Autowired
    private GoodsVmiStockDOMapper goodsVmiStockDOMapper;

    @CreateCache(name = WarehouseKeyConstant.GOODS_VMI_STOCK_DO_PRE)
    private Cache<String, GoodsVmiStockDO> cache;

    public void create(GoodsVmiStockDO goodsVmiStock) {
        GoodsVmiStockDO stockDO = goodsVmiStockDOMapper.findByItemId(goodsVmiStock.getItemId());
        if (stockDO != null) {
            throw new WarehouseException(GoodsVmiStockErrorCode.W_VMI_ITEM_STOCK_ONLY);
        }
        goodsVmiStockDOMapper.insert(goodsVmiStock);
        cache.put(TenantContext.getTenantNo() + ":" + goodsVmiStock.getItemId(), goodsVmiStock);
    }

    @CacheUpdate(name = WarehouseKeyConstant.GOODS_VMI_STOCK_DO_PRE, key = "#goodsVmiStockDO.itemId",
        value = "#goodsVmiStockDO")
    public void update(GoodsVmiStockDO goodsVmiStockDO) {
        goodsVmiStockDOMapper.updateByPrimaryKey(goodsVmiStockDO);
    }

    public void syncStock(List<InquiryInfo> vmiWarehouseNoList) {
        if (vmiWarehouseNoList == null || vmiWarehouseNoList.size() == 0) {
            return;
        }
        Map<String, InquiryInfo> map = vmiWarehouseNoList.stream()
            .collect(Collectors.toMap(InquiryInfo::getFMATERIALID, inquiryInfo -> inquiryInfo));
        List<GoodsVmiStockDO> goodsVmiStockDOList = goodsVmiStockDOMapper.listAll();
        if (goodsVmiStockDOList == null || goodsVmiStockDOList.size() == 0) {
            return;
        }
        goodsVmiStockDOList.stream().forEach(goodsVmiStockDO -> {
            if (map.containsKey(String.valueOf(goodsVmiStockDO.getItemErpId()))) {
                // 查询回来了
                InquiryInfo inquiryInfo = map.get(String.valueOf(goodsVmiStockDO.getItemErpId()));
                goodsVmiStockDO.setNumLock(inquiryInfo.getLockedQuantity().intValue());
                goodsVmiStockDO.setNumVmi(inquiryInfo.getFBASEQTY().intValue());
            } else {
                // VMI置0
                LOGGER.info("无法查询VMI库存、需要置0，货品id{}", goodsVmiStockDO.getItemId());
                goodsVmiStockDO.setNumVmi(0);
            }
            goodsVmiStockDO.setUpdateTime(new Date());
            update(goodsVmiStockDO);
        });

    }

    public void initGoodsStock(List<InquiryInfo> vmiList, GoodsMsgRpcDTO goodsMsgRpcDTO) {
        List<GoodsItemErpDTO> itemErpDTOList = goodsMsgRpcDTO.getItemErpDTOList();
        Map<String, InquiryInfo> map =
            vmiList.stream().collect(Collectors.toMap(InquiryInfo::getFMATERIALID, inquiryInfo -> inquiryInfo));
        itemErpDTOList.stream().forEach(goodsItemErpDTO -> {
            GoodsVmiStockDO goodsVmiStockDO = new GoodsVmiStockDO();
            goodsVmiStockDO.setCreateTime(new Date());
            goodsVmiStockDO.setUpdateTime(new Date());
            InquiryInfo inquiryInfo = map.get(String.valueOf(goodsItemErpDTO.getItemErpId()));
            if (inquiryInfo != null) {
                goodsVmiStockDO.setNumVmi(inquiryInfo.getPREDICTQTY().intValue());
                goodsVmiStockDO.setNumLock(inquiryInfo.getLockedQuantity().intValue());
            } else {
                goodsVmiStockDO.setNumVmi(0);
                goodsVmiStockDO.setNumLock(0);
            }
            goodsVmiStockDO.setGoodsId(goodsMsgRpcDTO.getGoodsId());
            goodsVmiStockDO.setItemId(goodsItemErpDTO.getItemId());
            goodsVmiStockDO.setItemErpId(goodsItemErpDTO.getItemErpId());
            goodsVmiStockDO.setItemName(goodsItemErpDTO.getItemName());
            goodsVmiStockDO.setItemCode(goodsItemErpDTO.getItemCode());
            try {
                create(goodsVmiStockDO);
            } catch (DuplicateKeyException e) {
                return;
            }
        });
    }

    public void deleteByItemId(Integer itemId) {
        goodsVmiStockDOMapper.deleteByItemId(itemId);
        // 移除缓存
        GoodsVmiStockDO vmiStockDO = cache.get(TenantContext.getTenantNo() + ":" + itemId);
        if (vmiStockDO != null) {
            cache.remove(TenantContext.getTenantNo() + ":" + itemId);
        }
    }

    /**
     * 批量删除VMI库存数据（包括缓存数据）
     *
     * @param itemIds
     */
    public void deleteList(List<Integer> itemIds) {
        Set<String> redisKeys = new HashSet<>();
        itemIds.forEach(itemId -> {
            redisKeys.add(TenantContext.getTenantNo() + ":" + itemId);
        });
        cache.removeAll(redisKeys);
        goodsVmiStockDOMapper.deleteByIds(itemIds);
    }

    // 只保存redis缓存、不保存mysql
    public void updateRedisCacheOnly(GoodsVmiStockDO goodsVmiStockDO) {
        cache.put(TenantContext.getTenantNo() + ":" + goodsVmiStockDO.getItemId(), goodsVmiStockDO);
    }

    /**
     * 批量修改mysql数据、不修改redis
     * 
     * @param goodsVmiStockDOList
     */
    public void updateList(List<GoodsVmiStockDO> goodsVmiStockDOList) {
        goodsVmiStockDOMapper.updateList(goodsVmiStockDOList);
    }
}
