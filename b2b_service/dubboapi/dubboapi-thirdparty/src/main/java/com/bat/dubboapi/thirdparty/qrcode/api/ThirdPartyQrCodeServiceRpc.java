package com.bat.dubboapi.thirdparty.qrcode.api;

import com.bat.dubboapi.thirdparty.qrcode.dto.ShopQrCodeRpcCmd;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.qrcode.dto.UserQrCodeRpcCmd;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/25 13:52
 */
public interface ThirdPartyQrCodeServiceRpc {

    /**
     * 分销商获取分销二维码
     *
     * @param cmd
     * @return
     */
    Response<String> distributionQrCode(UserQrCodeRpcCmd cmd);

    /**
     * 店铺二维码
     * 
     * @param cmd
     * @return
     */
    Response<String> shopQrCode(ShopQrCodeRpcCmd cmd);


    /**
     * 生成二维码 通用
     * @param text
     * @return
     */
    Response<String> createQrCode(String fileName, String ossFolder, String text);
}
