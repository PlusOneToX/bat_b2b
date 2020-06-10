package com.bat.flexible.api.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.dao.exchange.co.ExchangeCardPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.api.exchange.dto.*;

import java.util.List;

public interface ExchangeCardServiceI {

    /**
     * 新增兑换码
     * @param exchangeCardRequest
     * @param currentAdmin
     * @return
     */
    Response add(ExchangeCardCmd exchangeCardRequest, AdminResponse currentAdmin);

    Response update(ExchangeCardCmd exchangeCardRequest, AdminResponse currentAdmin);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    Response<ExchangeCardDetailDTO> detailById(Integer id);

    /**
     * 修改状态
     * @param exchangeCardStatusRequest
     * @param currentAdmin
     * @return
     */
    Response updateStatus(ExchangeCardStatusCmd exchangeCardStatusRequest, AdminResponse currentAdmin);

    /**
     * 兑换卡活动分页查询
     * @param exchangeCardPageQry
     * @return
     */
    PageInfo<List<ExchangeCardPageCO>> page(ExchangeCardPageQry exchangeCardPageQry);

    Response qrCode(Integer id, AdminResponse currentAdmin, ExchangeCardDO exchangeCardDO);

    /**
     * 同步至工厂
     * @param id
     * @param currentAdmin
     * @return
     */
   // Response syncFactory(ExchangeSyncFactoryRequest id, AdminResponse currentAdmin) throws IOException;



    /**
     * 同步兑换码到ERP
     * @param exchangeId
     * @return
     */
    Response syncExchangeToErp(Integer exchangeId);

    Response getDefaultExchangeIdNew(Integer exchangeId);

    ExchangeCardDO getById(Integer exchangeId);

    void updateDO(ExchangeCardDO exchangeCardDO);

    /**
     * 根据兑换卡活动id查询材质和型号参数
     * @param exchangeId
     * @return
     */
    ExchangeCardParamDTO listModelAndMaterial(Integer exchangeId);

    /**
     * 根据货品id和分销商id查询是否是兑换码
     * @param itemId
     * @param distributorId
     * @return
     */
    ExchangeCardDO checkIsExchangeByItemIdAndDistributorId(Integer itemId, Integer distributorId);


    /**
     * 根据id列表查询
     * @param exchangeIdList
     * @return
     */
    List<ExchangeCardDO> listByIdList(List<Integer> exchangeIdList);

    /**
     * 根据材质以及分销商查找权益活动
     * @param distributorId
     * @param materialId
     * @return
     */
    ExchangeCardDO findQuanyiByDistributorIdAndMaterialId(Integer distributorId, Integer materialId);
}
