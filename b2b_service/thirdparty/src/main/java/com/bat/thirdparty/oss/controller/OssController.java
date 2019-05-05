package com.bat.thirdparty.oss.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bat.thirdparty.oss.service.SystemServiceOssRpcImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: lim
 * @description: 不建议在服务器下载模板，在转流给前端的做法，增加了一次文件传输
 * @date: 2019/6/25 11:38
 */
@Api(tags = "oss文档模板下载接口", value = "OssController")
@RestController
public class OssController {

    @Resource
    private SystemServiceOssRpcImpl systemServiceOssRpc;

    @GetMapping("/getExcelTemp")
    @ApiOperation(value = "获取模板")
    public void getExcelTemp(String ossKey, String fileName, HttpServletResponse resp) throws IOException {
        try (InputStream inputStream = systemServiceOssRpc.getExcelTemp(ossKey, fileName);
            ServletOutputStream outputStream = resp.getOutputStream();) {
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setCharacterEncoding("utf-8");
            String fileName1 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            if (!fileName1.endsWith(".xls") && !fileName1.endsWith(".xlsx")) {
                fileName1 += ossKey.substring(ossKey.lastIndexOf("."));
            }
            resp.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName1);
            byte[] arr = new byte[1024];
            int length;
            while ((length = inputStream.read(arr)) != -1) {
                outputStream.write(arr, 0, length);
            }
        }
    }

}
