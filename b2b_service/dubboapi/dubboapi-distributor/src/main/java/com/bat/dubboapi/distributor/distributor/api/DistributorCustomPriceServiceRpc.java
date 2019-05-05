package com.bat.dubboapi.distributor.distributor.api;

import java.util.List;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.distributor.common.Response;

public interface DistributorCustomPriceServiceRpc {

    /**
     * 根据分销商id和货品id查询C端客户价格
     *
     * @param distributorId
     * @param itemId
     * @return
     */
    public Response<DistributorCustomerPriceDTO> getByDistributorIdAndItemId(Integer distributorId, Integer itemId);

    /**
     * 根据分销商id和货品ids查询C端客价格
     *
     * @param distributorId
     * @param itemIds
     * @return
     */
    public Response<List<DistributorCustomerPriceDTO>> getByDistributorIdAndItemIds(Integer distributorId,
        List<Integer> itemIds);

    /**
     * 修改材质关联的货品id、同时处理分销商C端价格（原来的货品iD和现在的不为空、且不匹配、直接修改为新的货品id、原来的货品ID不为空、新的为空、删除分销商C端价格）
     * 
     * @param oldItemId
     *            原来的货品id
     * @param newItemId
     *            现在的货品id
     */
    Response updateItemIdByMaterialRelevanceItemIdChange(Integer oldItemId, Integer newItemId, Integer updateUserId,
        String updateUserName);

    /**
     * 数据迁移
     * 
     * @param toJSONString
     */
    void importData(String toJSONString);
}
