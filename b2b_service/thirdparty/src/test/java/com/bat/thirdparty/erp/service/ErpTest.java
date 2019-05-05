package com.bat.thirdparty.erp.service;

import com.bat.thirdparty.ThirdPartyApplicationTests;
import com.bat.thirdparty.erp.manager.ErpDataManager;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/5/19 11:39
 */

public class ErpTest extends ThirdPartyApplicationTests {
    @Resource
    private ErpDataManager manager;

    public void ErpLogin() throws Exception {
        String token = manager.getToken("AXI-token");
        manager.deleteCachedToken("AXI-token");
        System.out.print(token);
    }
}
