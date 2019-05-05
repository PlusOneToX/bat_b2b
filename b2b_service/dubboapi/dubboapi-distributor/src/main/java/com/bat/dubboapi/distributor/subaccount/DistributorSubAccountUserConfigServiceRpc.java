package com.bat.dubboapi.distributor.subaccount;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountUserConfigRpcDTOQry;

public interface DistributorSubAccountUserConfigServiceRpc {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Response<DistributorSubAccountUserConfigRpcDTOQry> getUserSubAccountConfigById(Integer id);
}
