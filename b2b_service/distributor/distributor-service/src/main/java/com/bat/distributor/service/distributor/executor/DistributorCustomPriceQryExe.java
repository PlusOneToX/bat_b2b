package com.bat.distributor.service.distributor.executor;

import com.bat.distributor.dao.distributor.DistributorCustomPriceDOMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import com.bat.distributor.service.distributor.convertor.DistributorConvertor;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DistributorCustomPriceQryExe {

    @Autowired
    private DistributorCustomPriceDOMapper distributorCustomPriceDOMapper;

    @Resource
    private DistributorRpcQryExe rpcQryExe;


    public List<DistributorCustomPriceDO> listByCondition(Integer distributorId, Integer itemId) {
        return distributorCustomPriceDOMapper.listByDistributorIdAndItemId(distributorId, itemId);
    }

    public DistributorCustomerPriceDTO listByDistributorIdAndItemId(Integer distributorId, Integer itemId) {
        List<DistributorCustomPriceDO> priceDOList =
            distributorCustomPriceDOMapper.listByDistributorIdAndItemId(distributorId, itemId);
        GoodsItemPriceRpcDTO priceRpcDTO = null;
        DistributorCustomPriceDO customPriceDO = null;
        if (!CollectionUtils.isEmpty(priceDOList)) {
            customPriceDO = priceDOList.get(0);
        } else {
            // 没有配置价格、要调用goods服务获取
            List<Integer> itemIds = new ArrayList<>();
            itemIds.add(itemId);
            List<GoodsItemPriceRpcDTO> rpcDTOS = rpcQryExe.listGoodsItemRetailPrice(itemIds);
            if (!CollectionUtils.isEmpty(rpcDTOS)) {
                priceRpcDTO = rpcDTOS.get(0);
            }
        }
        DistributorCustomerPriceDTO dto =
            DistributorConvertor.toDistributorCustomerPriceDTO(itemId, customPriceDO, priceRpcDTO);
        return dto;
    }

    public List<DistributorCustomerPriceDTO> listByDistributorIdAndItemIds(Integer distributorId,
        List<Integer> itemIds) {
        List<DistributorCustomPriceDO> priceDOList =
            distributorCustomPriceDOMapper.listByDistributorIdAndItemIds(distributorId, itemIds);
        List<GoodsItemPriceRpcDTO> rpcDTOS = new ArrayList<>();
        List<Integer> noItemIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(priceDOList)) {
            List<Integer> exitItemIds =
                priceDOList.stream().map(DistributorCustomPriceDO::getItemId).collect(Collectors.toList());
            noItemIds = itemIds.stream().filter(itemId -> !exitItemIds.contains(itemId)).collect(Collectors.toList());
        } else {
            noItemIds.addAll(itemIds);
        }
        if (!CollectionUtils.isEmpty(noItemIds)) {
            rpcDTOS = rpcQryExe.listGoodsItemRetailPrice(noItemIds);
        }
        List<DistributorCustomerPriceDTO> dtos =
            DistributorConvertor.toDistributorCustomerPriceDTOList(itemIds, priceDOList, rpcDTOS);
        return dtos;
    }
}
