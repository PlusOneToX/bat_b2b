package com.bat.distributor.service.distributor.convertor;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.data.DistributorCustomPriceDTO;
import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.Constant;
import com.bat.dubboapi.flexible.material.api.MaterialServiceRpc;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DistributorCustomPriceConvertor {

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private MaterialServiceRpc materialServiceRpc;

    /**
     * 转换
     * @param priceDOList
     * @return
     */
    public List<DistributorCustomPriceDTO> toDistributorCustomPriceDTOList(List<DistributorCustomPriceDO> priceDOList){
        List<Integer> itemIdList = priceDOList.stream().map(DistributorCustomPriceDO::getItemId).collect(Collectors.toList());
        //根据itemIdList查询货品
        Response<List<GoodsItemRpcDTO>> goodsItemResponse = goodsServiceRpc.listGoodsItemByIds(itemIdList);
        if(goodsItemResponse ==null || !goodsItemResponse.isSuccess()){
            throw DistributorException.buildException(CommonErrorCode.C_GOODS_SERVICE_EXCEPTION);
        }
        List<GoodsItemRpcDTO> itemRpcDTOList = goodsItemResponse.getData();
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap = itemRpcDTOList.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));


        //根据itemIdList查询其绑定的材质列表
        com.bat.dubboapi.flexible.common.Response<List<MaterialDTORpcQry>> diyResponse = materialServiceRpc.listByItemIdList(itemIdList);
        if(diyResponse ==null || !diyResponse.isSuccess()){
            throw DistributorException.buildException(CommonErrorCode.C_FLEXIBLE_CUSTOM_SERVICE_EXCEPTION);
        }
        List<MaterialDTORpcQry> materialDTORpcQryList = diyResponse.getData();
        Map<Integer, MaterialDTORpcQry> materialDTORpcQryMap = materialDTORpcQryList.stream().collect(Collectors.toMap(MaterialDTORpcQry::getItemId, materialDTORpcQry -> materialDTORpcQry));

        List<DistributorCustomPriceDTO> list = new ArrayList<>();
        priceDOList.stream().forEach(distributorCustomPriceDO -> {
            DistributorCustomPriceDTO priceDTO = new DistributorCustomPriceDTO();
            priceDTO.setId(distributorCustomPriceDO.getId());
            priceDTO.setPrice(distributorCustomPriceDO.getPrice());
            GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(distributorCustomPriceDO.getItemId());
            priceDTO.setItemId(goodsItemRpcDTO.getId());
            priceDTO.setItemCode(goodsItemRpcDTO.getItemCode());
            priceDTO.setItemName(goodsItemRpcDTO.getItemName());
            priceDTO.setGoodsNo(goodsItemRpcDTO.getGoodsNo());
            priceDTO.setGoodsName(goodsItemRpcDTO.getGoodsName());
            MaterialDTORpcQry materialDTORpcQry = materialDTORpcQryMap.get(distributorCustomPriceDO.getItemId());
            priceDTO.setMaterialName(materialDTORpcQry.getName());
            list.add(priceDTO);
        });
        return list;
    }

    /**
     * 创建一个新的分销商定制C端价格对象
     * @return
     */
    public static DistributorCustomPriceDO toCreateDistributorCustomPriceDO(Integer distributorId, BigDecimal price,Integer itemId,Integer userId,String userName){
        DistributorCustomPriceDO customPriceDO = new DistributorCustomPriceDO();
        customPriceDO.setDistributorId(distributorId);
        customPriceDO.setPrice(price);
        customPriceDO.setItemId(itemId);
        customPriceDO.setCreateTime(new Date());
        customPriceDO.setCreateUserId(userId);
        customPriceDO.setCreateUserName(userName);
        customPriceDO.setUpdateUserId(userId);
        customPriceDO.setUpdateUserName(userName);
        customPriceDO.setUpdateTime(new Date());
        // 处理操作人信息
        customPriceDO.setDelFlag(Constant.COMMON_DEL_FLAG_NO);

        return customPriceDO;
    }
}
