package com.bat.system.service.organization;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.system.api.base.MessageUtils;
import com.bat.system.api.common.BeanUtils;
import com.bat.system.api.organization.dto.data.DepartmentDTO;
import com.bat.system.dao.organization.dataobject.DepartmentDO;
import com.bat.system.service.organization.executor.DepartmentQryExc;
import com.bat.system.service.organization.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.common.Response;
import com.bat.dubboapi.system.organization.api.SysDepartmentServiceRpc;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;

@DubboService
public class SysDepartmentServiceRpcImpl implements SysDepartmentServiceRpc {

    @Autowired
    private DepartmentQryExc departmentQryExc;

    @Override
    public Response<List<DepartmentRpcDTO>> listByCondition(Short status) {
        List<DepartmentDO> departmentDOList = departmentQryExc.listByCondition(status);
        List<DepartmentRpcDTO> dtoList = BeanUtils.copyList(departmentDOList, DepartmentRpcDTO.class);
        return Response.of(dtoList);
    }

    @Override
    public Response<DepartmentRpcDTO> getDepartmentById(Integer id) {
        DepartmentDTO dto = departmentQryExc.getDepartmentById(id);
        if (dto != null) {
            DepartmentRpcDTO rpcDTO = new DepartmentRpcDTO();
            org.springframework.beans.BeanUtils.copyProperties(dto, rpcDTO);
            return Response.of(rpcDTO);
        }
        return Response.buildFailure(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS, MessageUtils.get(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS));
    }

    @Override
    public Response<List<DepartmentRpcDTO>> getDepartmentByIds(List<Integer> ids) {
        List<DepartmentDTO> dtos = departmentQryExc.getDepartmentByIds(ids);
        if (!CollectionUtils.isEmpty(dtos)) {
            List<DepartmentRpcDTO> collect = dtos.stream().map(departmentDTO -> {
                DepartmentRpcDTO rpcDTO = new DepartmentRpcDTO();
                org.springframework.beans.BeanUtils.copyProperties(departmentDTO, rpcDTO);
                return rpcDTO;
            }).collect(Collectors.toList());
            return Response.of(collect);
        }
        return Response.buildFailure(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS, MessageUtils.get(ErrorCode.B_DEPARTMENT_ID_NOT_EXISTS));
    }
}
