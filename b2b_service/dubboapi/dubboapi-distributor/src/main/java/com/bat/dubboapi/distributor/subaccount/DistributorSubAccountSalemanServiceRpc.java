package com.bat.dubboapi.distributor.subaccount;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountSalemanRpcDTOQry;

import java.util.List;

public interface DistributorSubAccountSalemanServiceRpc {

    /**
     * 根据分账业务员id查询分账业务员对象
      * @param id
     * @return
     */
  Response<DistributorSubAccountSalemanRpcDTOQry> getSubAccountSalemanById(Integer id);

    /**
     * 根据分销商id查询所有的业务员
     * @param distributorId
     * @return
     */
    Response<List<DistributorSubAccountSalemanRpcDTOQry>> listAllSalemanByDistributorId(Integer distributorId);
}
