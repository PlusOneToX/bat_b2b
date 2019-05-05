package com.bat.thirdparty.message;

import javax.annotation.Resource;

import com.bat.thirdparty.ThirdPartyApplicationTests;
import com.bat.thirdparty.erp.service.ThirdPartyFinancialServiceErpRpcImpl;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ReceiveServiceTest extends ThirdPartyApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    ThirdPartyFinancialServiceErpRpcImpl thirdPartyFinancialServiceErpRpc;

    @Test
    public void test1() {
        System.out.println(thirdPartyFinancialServiceErpRpc);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}