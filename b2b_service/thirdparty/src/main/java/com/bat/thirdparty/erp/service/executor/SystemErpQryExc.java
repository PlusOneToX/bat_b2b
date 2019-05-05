package com.bat.thirdparty.erp.service.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.erp.k3cloudwebapiclient.K3CloudApiClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.thirdparty.erp.dto.system.DepartmentErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.UserErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.DepartmentErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.system.data.UserErpRpcDTO;
import com.bat.thirdparty.erp.api.request.BillQueryRequest;
import com.bat.thirdparty.erp.manager.ErpDataManager;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 11:01
 */
@Component
public class SystemErpQryExc {
    @Resource
    private ErpDataManager erpDataManager;

    public List<DepartmentErpRpcDTO> lisDepartment(DepartmentErpRpcQry qry) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        BillQueryRequest request = new BillQueryRequest();
        request.setFormId("BD_Department");
        // 名称 编码 上级部门名称 上级部门编码
        request.setFieldKeys("FName,FNumber,FParentID.FName,FParentID.FNumber,FUseOrgId.FName,FUseOrgId.FNumber");
        StringBuilder filter = new StringBuilder();
        filter.append("FForbidStatus = 'A'");
        if (qry.getOrgId() != null) {
            filter.append(" and FUseOrgId.FNumber in ( ").append(qry.getOrgId()).append(")");
        }
        request.setFilterString(filter.toString());
        String json = JSONObject.toJSONString(request);
        List<List<Object>> result = client.executeBillQuery(json);
        List<DepartmentErpRpcDTO> list = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            for (List<Object> objects : result) {
                DepartmentErpRpcDTO departmentErpDTO = new DepartmentErpRpcDTO();
                departmentErpDTO.setDepartmentName(objects.get(0) != null ? objects.get(0).toString() : null);
                departmentErpDTO.setErpDepartmentId(objects.get(1) != null ? objects.get(1).toString() : null);
                departmentErpDTO.setParentName(objects.get(2) != null ? objects.get(2).toString() : null);
                departmentErpDTO.setParentErpId(objects.get(3) != null ? objects.get(3).toString() : null);
                departmentErpDTO.setOrgName(objects.get(4) != null ? objects.get(4).toString() : null);
                departmentErpDTO.setOrgId(objects.get(5) != null ? objects.get(5).toString() : null);
                list.add(departmentErpDTO);
            }
            return list;
        }
        return list;
    }

    public List<UserErpRpcDTO> listUser(UserErpRpcQry qry) throws Exception {
        K3CloudApiClient client = erpDataManager.login();
        BillQueryRequest request = new BillQueryRequest();
        request.setFormId("BD_Empinfo");
        request.setFieldKeys("FName,FNumber,FPostDept.FName,FPostDept.FNumber,FUseOrgId.FName,FUseOrgId.FNumber");
        StringBuilder filter = new StringBuilder();
        filter.append("FForbidStatus = 'A'");
        if (qry.getErpUserNo() != null) {
            filter.append(" and FNumber in (").append("'").append(qry.getErpUserNo()).append("'").append(")");
        }
        request.setFilterString(filter.toString());
        String json = JSONObject.toJSONString(request);
        List<List<Object>> result = client.executeBillQuery(json);
        List<UserErpRpcDTO> list = new ArrayList<>();
        if (result != null && !result.isEmpty()) {
            for (List<Object> objects : result) {
                UserErpRpcDTO userErpRpcDTO = new UserErpRpcDTO();
                userErpRpcDTO.setName(objects.get(0) != null ? objects.get(0).toString() : null);
                userErpRpcDTO.setErpUserNo(objects.get(1) != null ? objects.get(1).toString() : null);
                userErpRpcDTO.setDepartmentName(objects.get(2) != null ? objects.get(2).toString() : null);
                userErpRpcDTO.setErpDepartmentNo(objects.get(3) != null ? objects.get(3).toString() : null);
                userErpRpcDTO.setOrganizationName(objects.get(4) != null ? objects.get(4).toString() : null);
                userErpRpcDTO.setErpOrganizationNo(objects.get(5) != null ? objects.get(5).toString() : null);
                list.add(userErpRpcDTO);
            }
            return list;
        }
        return list;
    }
}
