package com.bat.thirdparty.qrcode.service;

import javax.annotation.Resource;

import com.bat.thirdparty.qrcode.service.executor.QrCodeExe;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.qrcode.api.ThirdPartyQrCodeServiceRpc;
import com.bat.dubboapi.thirdparty.qrcode.dto.ShopQrCodeRpcCmd;
import com.bat.dubboapi.thirdparty.qrcode.dto.UserQrCodeRpcCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/25 13:52
 */
@DubboService
public class ThirdPartyQrCodeServiceRpcImpl implements ThirdPartyQrCodeServiceRpc {

    @Resource
    private QrCodeExe qrCodeExe;

    /**
     * 生成分销商分销二维码
     *
     * @param cmd
     * @return
     */
    @Override
    public Response<String> distributionQrCode(UserQrCodeRpcCmd cmd) {
        return qrCodeExe.distributionQrCode(cmd);
    }

    /**
     * 生成带编号的二维码
     * 
     * @param cmd
     * @return
     */
    @Override
    public Response<String> shopQrCode(ShopQrCodeRpcCmd cmd) {
        return qrCodeExe.shopQrCode(cmd);
    }

    /**
     * 生成二维码通用
     * @param text
     * @return
     */
    @Override
    public Response<String> createQrCode(String fileName, String ossFolder, String text) {
        return qrCodeExe.createQrCode(fileName,ossFolder,text);
    }
}
