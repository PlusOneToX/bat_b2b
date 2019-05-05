package com.bat.thirdparty.distributor.executor;

import com.bat.thirdparty.common.base.ThirdPartyException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.distributor.trade.api.DistributorTradeServiceRpc;
import com.bat.dubboapi.distributor.trade.dto.DistributorTradeRpcQryDTO;

@Component
public class DistributorServiceQryExe {

    @DubboReference(check = false, timeout = 10000)
    private DistributorTradeServiceRpc distributorTradeServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private DistributorServiceRpc distributorServiceRpc;

    /**
     * 根据分销商id获取结算方式
     * 
     * @param distributorId
     *            分销商id
     * @return
     */
    public DistributorTradeRpcQryDTO getTradeByDistributorId(Integer distributorId) {
        Response<DistributorTradeRpcQryDTO> tradeResponse =
            distributorTradeServiceRpc.getByDistributorId(distributorId);
        if (tradeResponse == null) {
            throw ThirdPartyException.buildException("获取分销商结算方式异常");
        }
        if (!tradeResponse.isSuccess()) {
            throw ThirdPartyException.buildException(tradeResponse.getErrMessage());
        }
        if (tradeResponse.getData() == null) {
            throw ThirdPartyException.buildException("该分销商尚未设置结算方式");
        }
        return tradeResponse.getData();
    }

    /**
     * 根据分销商id查询分销商信息
     * 
     * @param distributorId
     */
    public DistributorRpcDTO getDistributorById(Integer distributorId) {
        Response<DistributorRpcDTO> distributorRpcDTOResponse = distributorServiceRpc.distributorById(distributorId);
        if (!distributorRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(distributorRpcDTOResponse.getErrCode(),
                distributorRpcDTOResponse.getErrMessage());
        }
        return distributorRpcDTOResponse.getData();
    }
}
