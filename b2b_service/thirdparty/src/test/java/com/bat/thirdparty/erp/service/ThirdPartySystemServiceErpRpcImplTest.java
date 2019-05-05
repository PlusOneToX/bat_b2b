package com.bat.thirdparty.erp.service;

import com.bat.dubboapi.thirdparty.erp.dto.system.DepartmentErpRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.system.UserErpRpcQry;
import com.bat.thirdparty.ThirdPartyApplicationTests;
import org.springframework.beans.factory.annotation.Autowired;

public class ThirdPartySystemServiceErpRpcImplTest extends ThirdPartyApplicationTests {

    @Autowired
    private ThirdPartySystemServiceErpRpcImpl systemServiceErpRpc;

    public void listDepartment() throws Exception {
        DepartmentErpRpcQry qry = new DepartmentErpRpcQry();
        qry.setOrgId("100,201,209");
        systemServiceErpRpc.lisDepartment(qry);
    }

    public void listUser() throws Exception {
        UserErpRpcQry qry = new UserErpRpcQry();
        qry.setErpUserNo("'RQ000395'");
        systemServiceErpRpc.listUser(qry);
    }
}