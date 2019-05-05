package com.bat.thirdparty.alibaba.taobao.api;

import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;

public interface TaoBaoOrderServiceI {
    ResponseBaseBean sendGoodsCallBack(OrderLogistics logistics, DistributorElectricityRelationMappingRpcDTO mappingRpcDTO,String otherOrderNo);
}
