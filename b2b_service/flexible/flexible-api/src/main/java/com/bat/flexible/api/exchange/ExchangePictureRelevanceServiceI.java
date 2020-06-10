package com.bat.flexible.api.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;

import java.util.List;

public interface ExchangePictureRelevanceServiceI {

    void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> pictureIdList, AdminResponse adminResponse);

    List<PictureRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId);

    List<ExchangePictureRelevanceDO> listByExchangeId(Integer exchangeId);

    void create(ExchangePictureRelevanceDO rela);

    void deleteById(Integer id);

    ExchangePictureRelevanceDO findOneByExchangeId(Integer exchangeId);

    ExchangePictureRelevanceDO findByExchangeIdAndPictureId(Integer exchangeId, Integer pictureId);
}
