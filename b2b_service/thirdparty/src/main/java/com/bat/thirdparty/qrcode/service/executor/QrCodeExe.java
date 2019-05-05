package com.bat.thirdparty.qrcode.service.executor;

import static com.bat.thirdparty.qrcode.service.executor.ErrorCode.B_QR_CODE_OSS_ERROR;
import static com.bat.thirdparty.qrcode.service.executor.ErrorCode.B_QR_CODE_WRITER_ERROR;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.zxing.WriterException;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.qrcode.dto.ShopQrCodeRpcCmd;
import com.bat.dubboapi.thirdparty.qrcode.dto.UserQrCodeRpcCmd;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.common.util.QrCodeUtil;
import com.bat.thirdparty.oss.service.OssService;
import com.bat.thirdparty.qrcode.config.QrCodeConfig;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/26 11:30
 */
@Component
public class QrCodeExe {

    @Resource
    private QrCodeConfigExe qrCodeConfigExe;

    @Resource
    private OssService ossService;

    @Value("${font.path}")
    public String fontPath;

    /**
     * 生成分销商分销二维码
     *
     * @param cmd
     * @return
     */
    public Response<String> distributionQrCode(UserQrCodeRpcCmd cmd) {
        QrCodeConfig qrCodeConfig = qrCodeConfigExe.getQrCodeConfig();
        String distributionUrl =
            qrCodeConfig.getDistributionUrl() + "?distributorId=" + String.valueOf(cmd.getDistributorId());
        try {
            ByteArrayInputStream inputStream = QrCodeUtil.createQrCodeExtendStream(distributionUrl);
            String fileName = "qrcode_" + cmd.getDistributorId() + ".jpg";
            distributionUrl =
                ossService.uploadExtendStream(fileName, qrCodeConfig.getDistributionOssFolder(), inputStream);
            return Response.of(distributionUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_OSS_ERROR, MessageUtils.get("B_QR_CODE_OSS_ERROR"));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_WRITER_ERROR, MessageUtils.get("B_QR_CODE_WRITER_ERROR"));
        }
    }

    public Response shopQrCode(ShopQrCodeRpcCmd cmd) {
        String url = cmd.getUrl();
        String shopCode = cmd.getShopCode();
        try {
        ByteArrayOutputStream byteArrayOut = QrCodeUtil.createQrCodeExtendStream(url, shopCode,fontPath);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOut.toByteArray());
        String fileName = "qrcode_" + cmd.getShopCode() + ".jpg";
            String qrUrl = ossService.uploadExtendStream(fileName, "shop/qr/", inputStream);
            return Response.of(qrUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_OSS_ERROR, MessageUtils.get("B_QR_CODE_OSS_ERROR"));
        } catch (IOException | FontFormatException | WriterException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_WRITER_ERROR, MessageUtils.get("B_QR_CODE_WRITER_ERROR"));
        }
    }

    public Response<String> createQrCode(String fileName, String ossFolder, String text) {
        try {
            ByteArrayInputStream inputStream = QrCodeUtil.createQrCodeExtendStream(text);
            String qrUrl = ossService.uploadExtendStream(fileName, ossFolder, inputStream);
            return Response.of(qrUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_OSS_ERROR, MessageUtils.get("B_QR_CODE_OSS_ERROR"));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return Response.buildFailure(B_QR_CODE_WRITER_ERROR, MessageUtils.get("B_QR_CODE_WRITER_ERROR"));
        }
    }
}
