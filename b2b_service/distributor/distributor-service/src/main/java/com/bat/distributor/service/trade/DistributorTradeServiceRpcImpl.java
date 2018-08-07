package com.bat.distributor.service.trade;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.trade.dto.TradeId;
import com.bat.distributor.api.trade.dto.data.TradeDTO;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.distributor.service.trade.convertor.TradeConvertor;
import com.bat.distributor.service.trade.executor.TradeQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.trade.api.DistributorTradeServiceRpc;
import com.bat.dubboapi.distributor.trade.dto.DistributorTradeRpcQryDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class DistributorTradeServiceRpcImpl implements DistributorTradeServiceRpc {

    @Autowired
    private TradeQryExe tradeQryExe;

    @Override
    public Response<DistributorTradeRpcQryDTO> getById(Integer id) {
        TradeId qry = new TradeId();
        qry.setId(id);
        try {
            TradeDTO tradeDTO = tradeQryExe.execute(qry);
            DistributorTradeRpcQryDTO tradeRpcQryDTO = TradeConvertor.toDistributorTradeRpcQryDTO(tradeDTO);
            return Response.of(tradeRpcQryDTO);
        } catch (DistributorException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),e.getMsg());
        }
    }

    @Override
    public Response<DistributorTradeRpcQryDTO> getByDistributorId(Integer distributorId) {
        TradeDO tradeDO = tradeQryExe.getByDistributorId(distributorId);
        DistributorTradeRpcQryDTO tradeRpcQryDTO = TradeConvertor.toDistributorTradeRpcQryDTOByDO(tradeDO);
        return Response.of(tradeRpcQryDTO);
    }
}
