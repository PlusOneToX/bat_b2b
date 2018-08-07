package com.bat.system.service.organization.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.dao.organization.OrganizationMapper;
import com.bat.system.dao.organization.dataobject.OrganizationDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartySystemServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.system.DepartmentErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
public class DepartmentRpcQryExc {

    @Resource
    OrganizationMapper organizationMapper;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartySystemServiceErpRpc systemServiceErpRpc;

    public List<DepartmentErpRpcDTO> listDepartment() {
        Map<String, Object> map = new HashMap<>();
        List<OrganizationDO> organizationDOS = organizationMapper.listByParams(map);
        StringBuilder orgId = new StringBuilder();
        for (OrganizationDO organizationDO : organizationDOS) {
            orgId.append(",").append(organizationDO.getErpOrganizationId());
        }
        String substring = orgId.substring(1);
        DepartmentErpRpcQry qry = new DepartmentErpRpcQry();
        qry.setOrgId(substring);
        Response<List<DepartmentErpRpcDTO>> listResponse = systemServiceErpRpc.lisDepartment(qry);
        if (listResponse.isSuccess()) {
            return listResponse.getData();
        } else {
            throw SystemException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }
}
