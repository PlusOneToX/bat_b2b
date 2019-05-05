package com.bat.thirdparty.oss.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import com.bat.thirdparty.oss.service.executor.OssConfigExe;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.bat.thirdparty.oss.config.OssConfig;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/26 11:45
 */
@Component
public class OssService {

    @Resource
    private OssConfigExe ossConfigExe;

    /**
     * 输入流数据上传到OSS
     *
     * @param fileName
     * @param folder
     * @param in
     * @return
     */
    public String uploadExtendStream(String fileName, String folder, InputStream in)
        throws UnsupportedEncodingException {
        //获取配置
        OssConfig ossConfig= ossConfigExe.getOssConfig();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
            ossConfig.getAccessKeySecret());
        String ossKey = folder + fileName;
        ossKey = new String(ossKey.getBytes(), "utf-8");
        ossClient.putObject(new PutObjectRequest(ossConfig.getBucket(), ossKey, in));
        // 关闭OSSClient。
        ossClient.shutdown();
        return ossConfig.getBaseHttp() + folder + fileName;
    }

}
