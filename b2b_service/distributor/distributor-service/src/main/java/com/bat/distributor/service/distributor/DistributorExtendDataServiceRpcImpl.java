package com.bat.distributor.service.distributor;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.dao.distributor.dataobject.DistributorExtendDataDO;
import com.bat.distributor.service.distributor.executor.DistributorExtendDataQryExe;
import com.bat.distributor.service.distributor.executor.DistributorQryExe;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.MessageUtils;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorExtendDataServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorExtendDataRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorMsgNeedDataDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class DistributorExtendDataServiceRpcImpl implements DistributorExtendDataServiceRpc {

    @Autowired
    private DistributorQryExe distributorQryExe;

    @Autowired
    private DistributorExtendDataQryExe distributorExtendDataQryExe;

    @Override
    public Response<List<DistributorExtendDataRpcDTO>> listByCondition() {
        List<DistributorExtendDataDO> distributorExtendDataDOList = distributorExtendDataQryExe.listByCondition();
        List<DistributorExtendDataRpcDTO> distributorExtendDataRpcDTOS =
            BeanUtils.copyList(distributorExtendDataDOList, DistributorExtendDataRpcDTO.class);
        return Response.of(distributorExtendDataRpcDTOS);
    }

    @Override
    public Response<DistributorExtendDataRpcDTO> getByDistributorId(Integer distributorId) {
        if (distributorId == null) {
            return Response.buildFailure(CommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL,
                MessageUtils.get(CommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL));
        }
        DistributorExtendDataDO distributorExtendDataDO = distributorExtendDataQryExe.getByDistributorId(distributorId);
        return Response.of(BeanUtils.copy(distributorExtendDataDO, DistributorExtendDataRpcDTO.class));
    }

    @Override
    public Response<List<DistributorMsgNeedDataDTO>> getByDistributorIds(List<Integer> distributorIds) {
        List<DistributorMsgNeedDataDTO> distributorMsgNeedDataDTOS=new ArrayList<>();
        List<DistributorExtendDataDO> distributorExtendDataDOS=distributorExtendDataQryExe.getByDistributorIds(distributorIds);
        for(DistributorExtendDataDO distributorExtendDataDO:distributorExtendDataDOS){
            DistributorMsgNeedDataDTO distributorMsgNeedDataDTO=new DistributorMsgNeedDataDTO();
            org.springframework.beans.BeanUtils.copyProperties(distributorExtendDataDO,distributorMsgNeedDataDTO);
            distributorMsgNeedDataDTOS.add(distributorMsgNeedDataDTO);
        }
        return Response.of(distributorMsgNeedDataDTOS);
    }

    @Override
    public Response<List<DistributorExtendDataRpcDTO>> listAvailableByErpNos(List<String> erpNos) {
        List<DistributorExtendDataDO> distributorExtendDataDTOS =
            distributorExtendDataQryExe.listAvailableByErpNos(erpNos);
        if (!CollectionUtils.isEmpty(distributorExtendDataDTOS)) {
            List<DistributorExtendDataRpcDTO> rpcDTOS =
                distributorExtendDataDTOS.stream().map(distributorExtendDataDTO -> {
                    DistributorExtendDataRpcDTO rpcDTO = new DistributorExtendDataRpcDTO();
                    org.springframework.beans.BeanUtils.copyProperties(distributorExtendDataDTO, rpcDTO);
                    return rpcDTO;
                }).collect(Collectors.toList());
            return Response.of(rpcDTOS);
        }
        return Response.buildFailure(ErrorCode.B_DISTRIBUTOR_EXTEND_DATA_IS_NOT_EXISTS,
            MessageUtils.get(ErrorCode.B_DISTRIBUTOR_EXTEND_DATA_IS_NOT_EXISTS));
    }
}
