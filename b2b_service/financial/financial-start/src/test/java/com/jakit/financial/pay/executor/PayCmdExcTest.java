package com.bat.financial.pay.executor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bat.financial.dao.pay.dataobject.PayBillsDO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayCmdExcTest {

    @Resource
    private PayCmdExc payCmdExc;

    @Test
    public void testUpdatePayBills() throws ParseException {
        PayBillsDO aDo = new PayBillsDO();
        aDo.setOutTradeNo("kqc0ce1432587472425492480");
        aDo.setBusinessId("null");
        aDo.setPayStatus((short)1);
        aDo.setOrderTitle("null");
        aDo.setOrderDescribe("null");
        aDo.setProductId("null");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        aDo.setPayTime(format.parse("20180731141404"));
        BigDecimal decimal = new BigDecimal("330000");
        BigDecimal divide = decimal.divide(BigDecimal.valueOf(100L), 2, BigDecimal.ROUND_HALF_UP);
        aDo.setTotalFee(divide);
        aDo.setPayMethod("null");
        aDo.setCurrencyCode("CNY");
        payCmdExc.updatePayBills((short)0, aDo);
    }
}