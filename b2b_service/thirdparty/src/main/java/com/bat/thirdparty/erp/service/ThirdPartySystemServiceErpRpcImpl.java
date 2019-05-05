package com.bat.thirdparty.erp.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import com.bat.thirdparty.erp.service.executor.SystemErpQryExc;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartySystemServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.system.DepartmentErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.UserErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.UserErpRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 10:46
 */
@DubboService
class ThirdPartySystemServiceErpRpcImpl implements ThirdPartySystemServiceErpRpc {

    @Resource
    private SystemErpQryExc systemErpQryExc;

    @Override
    public Response<List<DepartmentErpRpcDTO>> lisDepartment(DepartmentErpRpcQry qry) {
        try {
            return Response.of(systemErpQryExc.lisDepartment(qry));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_DEPARTMENT_ERROR, MessageUtils.get(ErrorCode.B_ERP_DEPARTMENT_ERROR));
        }
    }

    @Override
    public Response<List<UserErpRpcDTO>> listUser(UserErpRpcQry qry) {
        try {
            return Response.of(systemErpQryExc.listUser(qry));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_USER_ERROR, MessageUtils.get(ErrorCode.B_ERP_USER_ERROR));
        }
    }
}
