package com.bat.dubboapi.thirdparty.oss;

import java.io.IOException;
import java.io.InputStream;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.oss.dto.AssumeRoleRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 21:32
 */
public interface ThirdPartySystemOssServiceRpc {
    /**
     * 获取sts授权
     * 
     * @param id
     * @return
     */
    Response<AssumeRoleRpcDTO> getAssumeRole(String id);

    /**
     * 上传文件 InputStream dubbo协议不支持
     * 
     * @param fileName
     * @param folder
     * @param in
     * @return
     */
    String uploadExtendStream(String fileName, String folder, InputStream in);

    /**
     * 以 byte[] 格式上传文件
     * 
     * @param fileName
     * @param folder
     * @param bytes
     * @return
     */
    String uploadExtendStream(String fileName, String folder, byte[] bytes);

    /**
     * 下载文件模板 dubbo协议不支持
     * 
     * @param ossKey
     * @param fileName
     * @return
     * @throws IOException
     */
    InputStream getExcelTemp(String ossKey, String fileName) throws IOException;

    /**
     * 下载文件模板 通过字节数组
     * 
     * @param ossKey
     * @param fileName
     * @return
     * @throws IOException
     */
    byte[] getExcelTempByteArr(String ossKey, String fileName) throws IOException;
}
