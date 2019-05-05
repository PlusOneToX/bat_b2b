package com.bat.dubboapi.distributor.subaccount;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountAdminConfigRpcDTOQry;

public interface DistributorSubAccountAdminConfigServiceRpc {

    /**
     * 根据分销商ID查询
     * @param distributorId
     * @return
     */
    Response<DistributorSubAccountAdminConfigRpcDTOQry> getAdminSubAccountConfigByDistributorId(Integer distributorId);
}
