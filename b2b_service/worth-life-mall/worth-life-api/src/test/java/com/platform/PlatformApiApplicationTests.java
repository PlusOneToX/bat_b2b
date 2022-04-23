package com.platform;

import com.github.binarywang.wxpay.bean.entpay.EntPayBankRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallDistService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.sys.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PlatformApiApplicationTests {
    @Autowired
    private MailService mailService;
    @Autowired
    private WxPayService payService;
    @Autowired
    private MallDistService mallDistServic;

    @Autowired
    private MallUserService mallUserService;
    @Test
    public void testDist() {
       /* List<MallDistEntity> entities = mallDistServic.queryAll(Collections.singletonMap("code", "46031018566"));
        log.info("entities.size {}", entities.size());*/
        mallUserService.lambdaUpdate().set(MallUserEntity::getUserName,"asdjkhasdjhk").eq(MallUserEntity::getId,6).update();
        System.out.println("1");

    }

    @Test
    public void testHtmlMail() {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件1111111!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendMail("939961241@qq.com", "test html mail", content);
    }

    /**
     * 企业向个人付款
     *
     * @throws WxPayException
     */
    @Test
    public void testPay() throws WxPayException {
        EntPayRequest request = new EntPayRequest();
        request.setPartnerTradeNo("APP20200717171910474277266");
        request.setOpenid("ouby15OpFirvjeut_CjDhWYAcm0s");
        request.setCheckName("NO_CHECK");// 不校验真实姓名
        request.setReUserName("李鹏军");
        request.setDescription("佣金提现");
        request.setSpbillCreateIp("127.0.0.1");
        request.setAmount(30);// amount是以分为单位
        System.out.println(request);
        payService.getEntPayService().entPay(request);
    }

    /**
     * 企业向个人银行卡付款
     *
     * @throws WxPayException
     */
    @Test
    public void testPayBank() throws WxPayException {
        EntPayBankRequest request = new EntPayBankRequest();
        request.setPartnerTradeNo("APP20200717171910474277266");
        request.setEncBankNo("6214991630490547");
        request.setEncTrueName("李鹏军");
        //建设银行
        request.setBankCode("1003");
        request.setAmount(1);// amount是以分为单位
        request.setDescription("佣金提现");
        payService.getEntPayService().payBank(request);
    }

    /**
     * 申请退款
     *
     * @throws WxPayException
     */
    @Test
    public void testEntPay() throws WxPayException {
        WxPayRefundRequest request = new WxPayRefundRequest();
        request.setOutTradeNo("MA20200718115214780645086");
        request.setTotalFee(1);
        request.setRefundFee(1);
        request.setOutRefundNo("MA20200718115214780645086");
        request.setRefundDesc("测试退款");
        System.out.println(request);
        payService.refund(request);
    }
}
