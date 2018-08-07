package com.bat.financial.voucher;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoucherServiceRpcImplTest {

    @Autowired
    private VoucherServiceRpcImpl voucherServiceRpc;

    @Test
    public void testListErpVoucherById() {
        voucherServiceRpc.listErpVoucherById(Arrays.asList(33800));
    }
}