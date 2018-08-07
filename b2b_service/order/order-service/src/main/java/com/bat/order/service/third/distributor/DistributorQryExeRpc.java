package com.bat.order.service.third.distributor;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorExtendDataServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountRatioServiceRpc;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountSalemanServiceRpc;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountUserConfigServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountRatioRpcDTOQry;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountSalemanRpcDTOQry;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountUserConfigRpcDTOQry;
import com.bat.order.api.common.exception.OrderException;

@Component
public class DistributorQryExeRpc {

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private DistributorExtendDataServiceRpc distributorExtendDataServiceRpc;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private DistributorSubAccountRatioServiceRpc distributorSubAccountRatioServiceRpc;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private DistributorSubAccountSalemanServiceRpc distributorSubAccountSalemanServiceRpc;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private DistributorSubAccountUserConfigServiceRpc distributorSubAccountUserConfigServiceRpc;

    /**
     * 根据分销商id获取分销商扩展数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorExtendDataRpcDTO getByDistributorId(Integer distributorId) {

        Response<DistributorExtendDataRpcDTO> distributorExtendDataRpcDTOResponse =
            distributorExtendDataServiceRpc.getByDistributorId(distributorId);
        if (!distributorExtendDataRpcDTOResponse.isSuccess()) {
            throw OrderException.buildException(distributorExtendDataRpcDTOResponse.getErrCode(),
                distributorExtendDataRpcDTOResponse.getErrMessage());
        }
        DistributorExtendDataRpcDTO distributorExtendDataRpcDTO = distributorExtendDataRpcDTOResponse.getData();
        return distributorExtendDataRpcDTO;
    }

    /**
     * 根据分销商分账配置id查询分账等级比例列表
     * 
     * @param subAccountConfigId
     * @return
     */
    public List<DistributorSubAccountRatioRpcDTOQry>
        listBySubAccountConfigIdOrderByLevelSequenceAsc(Integer subAccountConfigId) {
        Response<List<DistributorSubAccountRatioRpcDTOQry>> response =
            distributorSubAccountRatioServiceRpc.listBySubAccountConfigIdOrderByLevelSequenceAsc(subAccountConfigId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    /**
     * 根据分账业务员id查询分账业务员
     * 
     * @param salemanId
     * @return
     */
    public DistributorSubAccountSalemanRpcDTOQry getSubAccountSalemanById(Integer salemanId) {
        Response<DistributorSubAccountSalemanRpcDTOQry> response =
            distributorSubAccountSalemanServiceRpc.getSubAccountSalemanById(salemanId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    /**
     * 根据id获取分销商侧分账配置
     * 
     * @param userConfigId
     * @return
     */
    public DistributorSubAccountUserConfigRpcDTOQry getUserSubAccountConfigById(Integer userConfigId) {
        Response<DistributorSubAccountUserConfigRpcDTOQry> response =
            distributorSubAccountUserConfigServiceRpc.getUserSubAccountConfigById(userConfigId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    public List<DistributorSubAccountSalemanRpcDTOQry> listAllSalemanByDistributorId(Integer distributorId) {
        Response<List<DistributorSubAccountSalemanRpcDTOQry>> response =
            distributorSubAccountSalemanServiceRpc.listAllSalemanByDistributorId(distributorId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }
}
