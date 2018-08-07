package com.bat.system.service.logistics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.bat.system.api.base.MessageUtils;
import com.bat.system.api.logistics.dto.LogisticsCalculationQry;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.api.logistics.dto.data.LogisticsQryDTO;
import com.bat.system.api.logistics.dto.data.LogisticsThirdMappingDTO;
import com.bat.system.service.logistics.executor.ErrorCode;
import com.bat.system.service.logistics.executor.LogisticsQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsThirdMappingRpcDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:54
 */
@DubboService
@Slf4j
public class LogisticsServiceRpcImpl implements SystemLogisticsServiceRpc {

    @Resource
    private LogisticsQryExc logisticsQryExc;

    @Override
    public Response<LogisticsCalculationRpcDTO> getLogisticsCalculationById(LogisticsCalculationRpcQry qry) {
        log.info("getLogisticsCalculationById:params{}", JSON.toJSONString(qry));
        LogisticsCalculationQry qry1 = new LogisticsCalculationQry();
        BeanUtils.copyProperties(qry, qry1);
        LogisticsQryDTO logisticsQryDTOS = logisticsQryExc.getLogisticsCalculationById(qry1);
        if (logisticsQryDTOS != null) {
            LogisticsCalculationRpcDTO logisticsCalculationRpcDTO = new LogisticsCalculationRpcDTO();
            BeanUtils.copyProperties(logisticsQryDTOS, logisticsCalculationRpcDTO);
            return Response.of(logisticsCalculationRpcDTO);
        }
        return Response.buildFailure(ErrorCode.B_LOGISTICS_NOT_EXISTS,
            MessageUtils.get(ErrorCode.B_LOGISTICS_NOT_EXISTS));
    }

    @Override
    public Response<LogisticsThirdMappingRpcDTO> getThirdLogisticsByThirdTypeAndLogisticsId(Short thirdType,
        Integer logisticsId) {
        LogisticsThirdMappingDTO thirdLogisticsByThirdTypeAndDistributorId =
            logisticsQryExc.getThirdLogisticsByThirdTypeAndLogisticsId(thirdType, logisticsId);
        if (thirdLogisticsByThirdTypeAndDistributorId != null) {
            LogisticsThirdMappingRpcDTO dto = new LogisticsThirdMappingRpcDTO();
            BeanUtils.copyProperties(thirdLogisticsByThirdTypeAndDistributorId, dto);
            return Response.of(dto);
        }
        return Response.buildFailure(ErrorCode.B_THIRD_LOGISTICS_NOT_EXISTS,
            MessageUtils.get(ErrorCode.B_LOGISTICS_NOT_EXISTS));
    }

    @Override
    public Response<List<LogisticsRpcDTO>> listLogisticsFromRpcByParams(LogisticsRpcQry qry) {
        List<LogisticsDTO> logisticsDTOS = logisticsQryExc.listLogisticsFromRpcByParams(qry);
        List<LogisticsRpcDTO> collect = new ArrayList<>();
        if(!CollectionUtils.isEmpty(logisticsDTOS)){
            collect = logisticsDTOS.stream().map(logisticsDTO -> {
                LogisticsRpcDTO dto = new LogisticsRpcDTO();
                BeanUtils.copyProperties(logisticsDTO, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return Response.of(collect);
    }

    @Override
    public Response<LogisticsRpcDTO> getLogisticsById(Integer id) {
        LogisticsRpcDTO rpcDTO = logisticsQryExc.getLogisticsRpcDTOById(id);
        return Response.of(rpcDTO);
    }
}
