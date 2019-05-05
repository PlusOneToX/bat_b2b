package com.bat.dubboapi.system.logistics.api;

import java.util.List;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsThirdMappingRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/19 19:47
 */
public interface SystemLogisticsServiceRpc {

    /**
     * 通过物流id核查物流费用
     *
     * @param qry
     * @return
     */
    Response<LogisticsCalculationRpcDTO> getLogisticsCalculationById(LogisticsCalculationRpcQry qry);

    /**
     * 根据B2B物流编码 查询对应的第三方快递编码
     *
     * @param thirdType
     * @param logisticsId
     * @return
     */
    Response<LogisticsThirdMappingRpcDTO> getThirdLogisticsByThirdTypeAndLogisticsId(Short thirdType,
                                                                                     Integer logisticsId);

    /**
     * 来自RPC端查询 通过参数查询
     * 
     * @param qry
     * @return
     */
    Response<List<LogisticsRpcDTO>> listLogisticsFromRpcByParams(LogisticsRpcQry qry);

    /**
     * 根据配送方式ID获取配送方式信息
     * @param distributionId
     * @return
     */
    Response<LogisticsRpcDTO> getLogisticsById(Integer distributionId);
}
