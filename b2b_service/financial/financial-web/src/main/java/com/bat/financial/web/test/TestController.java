package com.bat.financial.web.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bat.financial.api.export.ExportService;
import com.bat.financial.api.message.MessageSendServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/16 20:34
 */
@RestController
@Slf4j
@RequestMapping("/financial/v1/web/user/")
public class TestController {

    @Autowired
    private MessageSendServiceI messageSendServiceIl;

    @Resource
    private ExportService exportService;

    @RequestMapping("/test")
    public void test() {
        MessageHeaderAccessor accessor = new MessageHeaderAccessor();
        accessor.setHeader(RocketMQHeaders.TAGS, "Test");
        long l = System.currentTimeMillis();
        accessor.setHeader(RocketMQHeaders.KEYS, l);
        accessor.setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 5);
        boolean test = messageSendServiceIl.test(String.valueOf(l), accessor);
        log.info("消息发送状态：{},KEY:{}", test, l);
    }

    @GetMapping("reconciliation/export")
    public void export(MultipartFile file, HttpServletResponse response) throws IOException {
        byte[] byteArr = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        exportService.reconciliationExport(inputStream, outputStream);
    }
}
