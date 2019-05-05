package com.bat.thirdparty.oss.service;

import static com.bat.thirdparty.oss.service.ErrorCode.B_OSS_ASSUME_ROLE_ERROR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.oss.service.executor.OssConfigExe;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.config.annotation.DubboService;

import com.alicp.jetcache.anno.Cached;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.oss.dto.AssumeRoleRpcDTO;
import com.bat.thirdparty.oss.config.OssConfig;

import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 21:34
 */
@DubboService
public class SystemServiceOssRpcImpl implements ThirdPartySystemOssServiceRpc {

    @Resource
    private OssConfigExe ossConfigExe;

    /**
     * 获取STS 临时授权 有效期30分钟
     *
     * @param userId
     *            调用者用户id
     * @return
     */
    @Override
    @Cached(name = "thirdparty:system:OssService.getAssumeRole", key = "#userId", expire = 1800)
    public Response<AssumeRoleRpcDTO> getAssumeRole(String userId) {
        OssConfig ossConfig=ossConfigExe.getOssConfig();
        userId = userId == null ? "0000" : userId;
        String AccessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String roleArn = ossConfig.getRoleArn();
        String roleSessionName = "U-" + userId;
        String policy = ossConfig.getPolicy();
        try {
            IClientProfile profile = DefaultProfile.getProfile(ossConfig.getRegionId(), AccessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            // 若policy为空，则用户将获得该角色下所有权限
            request.setPolicy(policy);
            // 设置凭证有效时间(30分钟)
            request.setDurationSeconds(3600L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleRpcDTO role = new AssumeRoleRpcDTO();
            role.setAccessKeyId(response.getCredentials().getAccessKeyId());
            role.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            role.setBucketName(ossConfig.getBucket());
            role.setEndpoint(ossConfig.getEndpoint());
            role.setExpiration(response.getCredentials().getExpiration());
            role.setRegion(ossConfig.getRegion());
            role.setSecurityToken(response.getCredentials().getSecurityToken());
            role.setHostname(ossConfig.getBaseHttp());
            role.setPathName(userId + "");
            return Response.of(role);
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
            return Response.buildFailure(B_OSS_ASSUME_ROLE_ERROR, MessageUtils.get(B_OSS_ASSUME_ROLE_ERROR));
        }
    }

    /**
     * 输入流数据上传到OSS
     *
     * @param fileName
     * @param folder
     * @param in
     * @return
     */
    @SneakyThrows
    @Override
    public String uploadExtendStream(String fileName, String folder, InputStream in) {
        OssConfig ossConfig=ossConfigExe.getOssConfig();
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

    /**
     * 旧版文件下载api
     * 
     * 新功能不建议使用
     * 
     * @param ossKey
     * @param fileName
     * @throws IOException
     * @return
     */
    @Override
    public InputStream getExcelTemp(String ossKey, String fileName) throws IOException {
        OssConfig ossConfig=ossConfigExe.getOssConfig();
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
            ossConfig.getAccessKeySecret());
        ossKey = new String(ossKey.getBytes(), "utf-8");
        // 从阿里云进行下载
        // bucketName需要自己设置
        // 直接返回流 就不能关闭client，可能存在连接不能回收 导致阻塞的问题
        OSSObject ossObject = ossClient.getObject(ossConfig.getBucket(), ossKey);
        InputStream inputStream = ossObject.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        ByteArrayInputStream stream = new ByteArrayInputStream(outputStream.toByteArray());
        ossClient.shutdown();
        return stream;
    }

    /**
     * 输入流数据上传到OSS
     *
     * @param fileName
     * @param folder
     * @return
     */
    @SneakyThrows
    @Override
    public String uploadExtendStream(String fileName, String folder, byte[] bytes) {
        OssConfig ossConfig=ossConfigExe.getOssConfig();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
            ossConfig.getAccessKeySecret());
        String ossKey = folder + fileName;
        ossKey = new String(ossKey.getBytes(), "utf-8");
        InputStream in = new ByteArrayInputStream(bytes);
        ossClient.putObject(new PutObjectRequest(ossConfig.getBucket(), ossKey, in));
        // 关闭OSSClient。
        ossClient.shutdown();
        return ossConfig.getBaseHttp() + folder + fileName;
    }

    /**
     * 旧版文件下载api
     *
     * 新功能不建议使用
     *
     * @param ossKey
     * @param fileName
     * @throws IOException
     * @return
     */
    @Override
    public byte[] getExcelTempByteArr(String ossKey, String fileName) throws IOException {
        OssConfig ossConfig=ossConfigExe.getOssConfig();
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(),
            ossConfig.getAccessKeySecret());
        ossKey = new String(ossKey.getBytes(), "utf-8");
        // 从阿里云进行下载
        // bucketName需要自己设置
        // 直接返回流 就不能关闭client，可能存在连接不能回收 导致阻塞的问题
        OSSObject ossObject = ossClient.getObject(ossConfig.getBucket(), ossKey);
        InputStream inputStream = ossObject.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        ossClient.shutdown();
        return outputStream.toByteArray();
    }
}