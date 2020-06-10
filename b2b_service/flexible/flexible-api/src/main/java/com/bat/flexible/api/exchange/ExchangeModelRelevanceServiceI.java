package com.bat.flexible.api.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;

import java.util.List;

public interface ExchangeModelRelevanceServiceI {

    void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> modelIdList, AdminResponse adminResponse);
    
    List<ModelRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId);

    List<ExchangeModelRelevanceDO> listByExchangeId(Integer exchangeId);

    void create(ExchangeModelRelevanceDO rela);

    void deleteById(Integer id);

    ExchangeModelRelevanceDO findOneByExchangeIdAndModelId(Integer exchangeId, Integer modelId);
}
