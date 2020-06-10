package com.bat.flexible.api.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;

import java.util.List;

public interface ExchangeMaterialRelevanceServiceI {

    /**
     * 保存关联关系
     * @param isAdd 是否新增
     * @param exchangeId 兑换卡活动id
     * @param materialIdList 材质id列表、空表示全部适用
     * @param adminResponse 后台操作人
     */
    void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> materialIdList, AdminResponse adminResponse);


    List<MaterialRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId);


    List<ExchangeMaterialRelevanceDO> listByExchangeId(Integer exchangeId);

    void create(ExchangeMaterialRelevanceDO rela);

    void deleteById(Integer id);

    /**
     * 根据兑换卡活动id和材质id查询关联关系
     * @param exchangeId
     * @param materialId
     * @return
     */
    ExchangeMaterialRelevanceDO findByExchangeIdAndMaterialId(Integer exchangeId, Integer materialId);
}
