package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderExtendDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.flexible.dao.exchange.ExchangeCodeDOMapper;
import com.bat.flexible.dao.exchange.co.ExchangeCodePageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeCodeQryExe {

    @Autowired
    private ExchangeCodeDOMapper exchangeCodeDOMapper;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    /**
     * 根据兑换卡id查询兑换码列表
     * @param exchangeId
     * @return
     */
    public List<ExchangeCodeDO> listByExchangeId(Integer exchangeId) {
        return exchangeCodeDOMapper.listByExchangeId(exchangeId);
    }

    public List<ExchangeCodeDO> listByOutboundNoGroupByBoxCode(String outboundNo) {
        return exchangeCodeDOMapper.listByOutboundNoGroupByBoxCode(outboundNo);
    }

    public List<ExchangeCodeDO> listByBoxCode(String boxCode) {
        return exchangeCodeDOMapper.listByBoxCode(boxCode);
    }

    public ExchangeCodeDO findByPlainCodeAndSecretCode(String plainCode, String secretCode) {
        return exchangeCodeDOMapper.findByPlainCodeAndSecretCode(plainCode,secretCode);
    }

    public List<ExchangeCodeDO> listByBoxCodeAndPlainCodeList(String boxCode, List<String> plainCodeList) {

        return exchangeCodeDOMapper.listByBoxCodeAndPlainCodeList(boxCode,plainCodeList);
    }

    public ExchangeCodeDO getById(Integer id) {
        ExchangeCodeDO exchangeCodeDO = exchangeCodeDOMapper.selectByPrimaryKey(id);
        if(exchangeCodeDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return exchangeCodeDO;
    }

    public ExchangeCodeDO findMaxByRiseValue(String riseValue) {
        return exchangeCodeDOMapper.findMaxByRiseValue(riseValue);
    }

    public List<ExchangeCodeDO> findByUserOrderIdAndStatusOrderByExchangeIdAsc(Integer userOrderId, Short status) {
        return exchangeCodeDOMapper.findByUserOrderIdAndStatusOrderByExchangeIdAsc(userOrderId,status);
    }

    public List<ExchangeCodeDO> countByBoxCode(Integer itemId) {
        return exchangeCodeDOMapper.countByBoxCodeAndItemId(itemId);
    }

    public List<ExchangeCodeDO> countByBoxCodeIsNotNull(Integer itemId) {
        return exchangeCodeDOMapper.countByBoxCodeIsNotNull(itemId);
    }

    public List<ExchangeCodePageCO> listCOByCondition(Integer exchangeFactoryId, Integer exchangeId, Short exchangeWay, Short status, String content,Integer exchangeExportId) {
        return exchangeCodeDOMapper.listCOByCondition(exchangeFactoryId,exchangeId,exchangeWay,status,content,exchangeExportId);
    }

    public List<ExchangeCodeDO> listByExchangeCodeIdListAndMaterialIdAndModelId(Integer materialId, Integer modelId, List<Integer> exchangeCodeIdList) {
        return exchangeCodeDOMapper.listByExchangeCodeIdListAndMaterialIdAndModelId(materialId,modelId,exchangeCodeIdList);
    }

    public List<OrderExtendDataSimpleRpcDTO> listByOutboundNoGroupByDistributorOrderId(String outboundNo) {
        List<ExchangeCodeDO> exchangeCodeDOList = exchangeCodeDOMapper.listByOutboundNoGroupByDistributorOrderId(outboundNo);
        if(exchangeCodeDOList ==null || exchangeCodeDOList.size()==0){
            return null;
        }
        List<Integer> orderIdList = new ArrayList<>();
        exchangeCodeDOList.stream().forEach(exchangeCodeDO -> {
            orderIdList.add(exchangeCodeDO.getDistributorOrderId());
        });
        Response<List<OrderExtendDataSimpleRpcDTO>> response = orderExtendDataServiceRpc.listExtendDataSimpleByOrderIdList(orderIdList);
        if(response ==null || !response.isSuccess()){
            throw FlexibleDubboApiException.buildException("访问订单服务异常");
        }
        return response.getData();
    }

    public List<ExchangeCodeDO> listBySecretCodeList(List<String> secretCodeList) {
        return exchangeCodeDOMapper.listBySecretCodeList(secretCodeList);
    }

    public List<ExchangeCodeDO> listByUserOrderId(Integer orderId) {
        return exchangeCodeDOMapper.listByUserOrderId(orderId);
    }

    public List<ExchangeCodeDO> listAll2() {
        return exchangeCodeDOMapper.listAll2();
    }

    public List<ExchangeCodeDO> listByIds(List<Integer> exchangeCodeIds) {
        return exchangeCodeDOMapper.listByIds(exchangeCodeIds);
    }

    public List<ExchangeCodeDO> listByExportId(Integer exchangeExportId) {
        return exchangeCodeDOMapper.listByExportId(exchangeExportId);
    }
}
