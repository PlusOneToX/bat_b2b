package com.bat.dubboapi.distributor.subaccount;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountRatioRpcDTOQry;

import java.util.List;

public interface DistributorSubAccountRatioServiceRpc {


    /**
     * 根据分销商分账配置ID查询分账比例列表、按照等级顺序
     * @param subAccountConfigId
     * @return
     */
    Response<List<DistributorSubAccountRatioRpcDTOQry>> listBySubAccountConfigIdOrderByLevelSequenceAsc(Integer subAccountConfigId);

}
