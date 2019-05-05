package com.bat.thirdparty.factory.maike.executor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.factory.maike.common.MaikeConfigQry;
import com.bat.thirdparty.factory.maike.common.MaikeFactoryConfig;
import com.bat.thirdparty.utils.FTPUtils;

@Component
public class MaikeCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaikeCmdExe.class);

    @Resource
    MaikeConfigQry maikeConfigQry;

    /**
     * 上传文件去FTP、返回工厂FTP路径
     * 
     * @param jpgFileStr
     * @param tryCount
     *            重试次数
     * @return
     */
    public String uploadFileToFTP(String factoryCode, String jpgFileStr, Integer tryCount) {
        MaikeFactoryConfig maikeFactoryConfig= maikeConfigQry.getTenantFactoryConfig(factoryCode);
        LOGGER.info("上传图片到FTP：" + jpgFileStr);
        if (!jpgFileStr.contains("com/")) {
            LOGGER.info("图片非法：" + jpgFileStr);
            throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_ORDER_PICTURE_URL_ILLEGAL);
        }
        String[] arr = jpgFileStr.split("com/");
        arr = arr[1].split("/");
        String fileName = arr[arr.length - 1];
        String filePath = "";
        for (int x = 0; x < arr.length - 1; x++) {
            filePath += arr[x] + "/";
        }
        filePath = filePath.substring(0, filePath.length() - 1);
        // 拼上文件夹
        filePath = maikeFactoryConfig.getFtpLocalFile() + "/" + filePath;
        // 处理url含有 customize//
        if (filePath.indexOf("customize//") > -1) {
            filePath = filePath.replaceFirst("customize//", "customize/");
        }
        InputStream inputStream = null;
        try {
            URL url = new URL(jpgFileStr);
            inputStream = url.openStream();
        } catch (Exception e) {
            e.printStackTrace();
            // 读取网络图片URL异常
            LOGGER.error(
                MessageUtils.get(ThirdOrderErrorCode.T_ORDER_PICTURE_INTERNET_URL_READ_EXCEPTION) + jpgFileStr + "{}",
                e);
            throw new ThirdPartyOpenApiException(
                MessageUtils.get(ThirdOrderErrorCode.T_ORDER_PICTURE_INTERNET_URL_READ_EXCEPTION) + jpgFileStr);
        }

        try {
            Boolean isUpload = FTPUtils.storeFile(maikeFactoryConfig.getFtpIp(), maikeFactoryConfig.getFtpPort(),
                maikeFactoryConfig.getFtpUserName(), maikeFactoryConfig.getFtpPassword(), filePath, fileName,
                inputStream);
            if (!isUpload) {
                isUpload = FTPUtils.storeFile(maikeFactoryConfig.getFtpIp(), maikeFactoryConfig.getFtpPort(),
                    maikeFactoryConfig.getFtpUserName(), maikeFactoryConfig.getFtpPassword(), filePath, fileName,
                    inputStream);
                if (!isUpload) {
                    LOGGER.error(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_FTP_UPLOAD_FAIL) + jpgFileStr + "重新上传");
                    throw new ThirdPartyOpenApiException(
                        MessageUtils.get(ThirdOrderErrorCode.T_ORDER_FTP_UPLOAD_FAIL) + jpgFileStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_FTP_UPLOAD_FAIL) + jpgFileStr + "--{}", e);
            tryCount++;
            if (tryCount < 6) {
                LOGGER.info("FTP上传失败、继续重试：" + tryCount + ",图片：" + jpgFileStr);
                uploadFileToFTP(factoryCode, jpgFileStr, tryCount);
            }
            LOGGER.info("FTP上传失败超过5次、不再重试：" + jpgFileStr);
            throw new ThirdPartyOpenApiException(
                MessageUtils.get(ThirdOrderErrorCode.T_ORDER_FTP_UPLOAD_FAIL) + jpgFileStr);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("关闭流失败");
            throw new ThirdPartyOpenApiException(
                MessageUtils.get(ThirdOrderErrorCode.T_ORDER_FTP_UPLOAD_FAIL) + jpgFileStr);
        }
        return "/" + filePath + "/" + fileName;
    }
}
