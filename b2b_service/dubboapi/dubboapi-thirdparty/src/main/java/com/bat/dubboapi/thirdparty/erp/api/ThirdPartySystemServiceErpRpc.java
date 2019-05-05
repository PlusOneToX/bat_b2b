package com.bat.dubboapi.thirdparty.erp.api;

import java.util.List;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.system.DepartmentErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.UserErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.UserErpRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 10:45
 */
public interface ThirdPartySystemServiceErpRpc {

    Response<List<DepartmentErpRpcDTO>> lisDepartment(DepartmentErpRpcQry qry);

    Response<List<UserErpRpcDTO>> listUser(UserErpRpcQry qry);
}
