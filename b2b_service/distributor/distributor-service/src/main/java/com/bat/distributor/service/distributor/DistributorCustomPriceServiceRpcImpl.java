package com.bat.distributor.service.distributor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceCmdExe;
import com.bat.distributor.service.distributor.executor.DistributorCustomPriceQryExe;
import com.bat.distributor.service.common.Constant;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorCustomPriceServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@DubboService
public class DistributorCustomPriceServiceRpcImpl implements DistributorCustomPriceServiceRpc {

    @Autowired
    private DistributorCustomPriceQryExe distributorCustomPriceQryExe;

    @Autowired
    private DistributorCustomPriceCmdExe distributorCustomPriceCmdExe;

    @Override
    public Response<DistributorCustomerPriceDTO> getByDistributorIdAndItemId(Integer distributorId, Integer itemId) {
        DistributorCustomerPriceDTO dto =
            distributorCustomPriceQryExe.listByDistributorIdAndItemId(distributorId, itemId);
        return Response.of(dto);
    }

    @Override
    public Response<List<DistributorCustomerPriceDTO>> getByDistributorIdAndItemIds(Integer distributorId,
        List<Integer> itemIds) {
        List<DistributorCustomerPriceDTO> dtos =
            distributorCustomPriceQryExe.listByDistributorIdAndItemIds(distributorId, itemIds);
        return Response.of(dtos);
    }

    //修改材质关联的货品id、同时处理分销商C端价格（原来的货品iD和现在的不为空、且不匹配、直接修改为新的货品id、原来的货品ID不为空、新的为空、删除分销商C端价格）
    @Transactional
    @Override
    public Response updateItemIdByMaterialRelevanceItemIdChange(Integer oldItemId, Integer newItemId,Integer updateUserId,String updateUserName) {
        //原来关联的价格列表
        List<DistributorCustomPriceDO> priceDOList = distributorCustomPriceQryExe.listByCondition(null, oldItemId);
        if(priceDOList ==null || priceDOList.size()==0){
            return Response.buildSuccess();
        }
        if(newItemId ==null){
            //原来关联了货品id、后面没有关联（变成非最终材质）、删除
            priceDOList.stream().forEach(distributorCustomPriceDO -> {
                distributorCustomPriceDO.setDelFlag(Constant.COMMON_DEL_FLAG_YES);
                distributorCustomPriceDO.setUpdateUserId(updateUserId);
                distributorCustomPriceDO.setUpdateUserName(updateUserName);
                distributorCustomPriceDO.setUpdateTime(new Date());
                distributorCustomPriceCmdExe.update(distributorCustomPriceDO);
            });
        }else{
            priceDOList.stream().forEach(distributorCustomPriceDO -> {
                distributorCustomPriceDO.setItemId(newItemId);
                distributorCustomPriceDO.setUpdateUserId(updateUserId);
                distributorCustomPriceDO.setUpdateUserName(updateUserName);
                distributorCustomPriceDO.setUpdateTime(new Date());
                distributorCustomPriceCmdExe.update(distributorCustomPriceDO);
            });
        }
        return Response.buildSuccess();
    }

    @Override
    public void importData(String jsonArrayString) {
        JSONArray jsonArray = JSONArray.parseArray(jsonArrayString);
        for(int x=0;x<jsonArray.size();x++){
            JSONObject jsonObject = jsonArray.getJSONObject(x);
            DistributorCustomPriceDO customPriceDO = JSONObject.parseObject(jsonObject.toJSONString(), DistributorCustomPriceDO.class);
            customPriceDO.setDelFlag(Constant.COMMON_DEL_FLAG_NO);
            customPriceDO.setCreateTime(new Date());
            customPriceDO.setCreateUserId(-1);
            customPriceDO.setCreateUserName("数据迁移");
            customPriceDO.setUpdateTime(new Date());
            customPriceDO.setUpdateUserId(-1);
            customPriceDO.setUpdateUserName("数据迁移");
            customPriceDO.setPrice(jsonObject.getBigDecimal("price"));
            distributorCustomPriceCmdExe.create(customPriceDO);
        }
    }
}
