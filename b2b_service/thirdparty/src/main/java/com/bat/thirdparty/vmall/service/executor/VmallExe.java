package com.bat.thirdparty.vmall.service.executor;
import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.vmall.common.VmallBopConstant;
import com.bat.thirdparty.vmall.request.BopOcidDecryptRequest;
import com.bat.thirdparty.vmall.request.BopOrderFulfillInfoRequest;
import com.bat.thirdparty.vmall.request.BopOrderStatusUpdateRequest;
import com.bat.thirdparty.vmall.request.BopQueryShipmentRequest;
import com.bat.thirdparty.vmall.response.BopAddressDecryptResponse;
import com.bat.thirdparty.vmall.response.BopOrderFulfillInfoResponse;
import com.bat.thirdparty.vmall.response.BopOrderStatusUpdateResponse;
import com.bat.thirdparty.vmall.response.BopShipmentLisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Component
public class VmallExe {

    public static String ALGORITHM_SHAR256 = "SHA-256";
    public static String ALGORITHM_HMAC_SHAR256 = "HmacSHA256";
    public static byte XF = 0x0F;
    public static short XFF = 0xFF;
    public static char ZERO = '0';

    @Resource
    private HttpUtil httpUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(VmallExe.class);

    public BopOrderFulfillInfoResponse orderFulfillInfoList(BopOrderFulfillInfoRequest request) {
        String url = VmallBopConstant.url+VmallBopConstant.orderFulfillListUri; // 订单拉取地址
        String deviceTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).format(System.currentTimeMillis());
        String packPass = "";
        try {
            packPass = packPassword(VmallBopConstant.dataPasswd, VmallBopConstant.priKey, VmallBopConstant.deviceType, deviceTimeStamp);
        } catch (Exception e) {
            throw ThirdPartyException.buildException("密码加密出现异常"+e.getMessage());
        }
        request.setMaxCount(VmallBopConstant.maxCount);
        HttpHeaders headers = new HttpHeaders();
        headers.add("VmallDeviceType",VmallBopConstant.deviceType);
        headers.add("VmallDeviceAccount", VmallBopConstant.deviceAccount);
        headers.add("VmalltimeStamp", deviceTimeStamp);//头参数
        headers.add("VmallDevicePassword", packPass);//头参数
        LOGGER.info("拉取订单请求头参数VmallDeviceType:{},VmallDeviceAccount:{},VmalltimeStamp:{},VmallDevicePassword:{}",
                VmallBopConstant.deviceType,VmallBopConstant.deviceAccount,deviceTimeStamp,packPass);
        LOGGER.debug("拉取订单url:{}", url);
        LOGGER.debug("拉取订单请求参数:{}", JSONObject.toJSONString(request));
        String result = httpUtil.requestFor(url, HttpMethod.POST, headers, request, String.class);
        LOGGER.info("拉取订单请求返回:{}",result);
        BopOrderFulfillInfoResponse response=JSONObject.parseObject(result,BopOrderFulfillInfoResponse.class);
        return response;
    }

    public BopOrderStatusUpdateResponse orderStatusUpdate(BopOrderStatusUpdateRequest request) {
        String url = VmallBopConstant.url + VmallBopConstant.updateOrderStatusUri; // 订单拉取地址
        String deviceTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).format(System.currentTimeMillis());
        String packPass = "";
        try {
            packPass = packPassword(VmallBopConstant.dataPasswd, VmallBopConstant.priKey, VmallBopConstant.deviceType, deviceTimeStamp);
        } catch (Exception e) {
            throw ThirdPartyException.buildException("密码加密出现异常" + e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("VmallDeviceType", VmallBopConstant.deviceType);
        headers.add("VmallDeviceAccount", VmallBopConstant.deviceAccount);
        headers.add("VmalltimeStamp", deviceTimeStamp);//头参数
        headers.add("VmallDevicePassword", packPass);//头参数
        LOGGER.info("改变订单状态请求头参数VmallDeviceType:{},VmallDeviceAccount:{},VmalltimeStamp:{},VmallDevicePassword:{}",
                VmallBopConstant.deviceType,VmallBopConstant.deviceAccount,deviceTimeStamp,packPass);
        LOGGER.debug("改变订单状态url:{}", url);
        LOGGER.debug("改变订单状态请求参数:{}", JSONObject.toJSONString(request));
        String result = httpUtil.requestFor(url, HttpMethod.POST, headers, request, String.class);
        LOGGER.debug("改变订单状态请求返回:{}",result);
        BopOrderStatusUpdateResponse response = JSONObject.parseObject(result, BopOrderStatusUpdateResponse.class);
        return response;
    }


    public BopShipmentLisResponse queryShipmentLis(BopQueryShipmentRequest request) {
        LOGGER.debug("查看发货单请求参数:{}", JSONObject.toJSONString(request));
        String url = VmallBopConstant.url+VmallBopConstant.queryShipmentListUri; // 订单拉取地址
        String deviceTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).format(System.currentTimeMillis());
        String packPass = "";
        try {
            packPass = packPassword(VmallBopConstant.dataPasswd, VmallBopConstant.priKey, VmallBopConstant.deviceType, deviceTimeStamp);
        } catch (Exception e) {
            throw ThirdPartyException.buildException("密码加密出现异常"+e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("VmallDeviceType",VmallBopConstant.deviceType);
        headers.add("VmallDeviceAccount", VmallBopConstant.deviceAccount);
        headers.add("VmalltimeStamp", deviceTimeStamp);//头参数
        headers.add("VmallDevicePassword", packPass);//头参数
        LOGGER.debug("查看发货单请求头参数VmallDeviceType:{},VmallDeviceAccount:{},VmalltimeStamp:{},VmallDevicePassword:{}",
                VmallBopConstant.deviceType,VmallBopConstant.deviceAccount,deviceTimeStamp,packPass);
        LOGGER.debug("查看发货单url:{}", url);
        String result = httpUtil.requestFor(url, HttpMethod.POST, headers, request, String.class);
        LOGGER.info("查看发货单请求返回:{}",result);
        BopShipmentLisResponse response = JSONObject.parseObject(result, BopShipmentLisResponse.class);
        return response;
    }

    public BopAddressDecryptResponse ocidDecrypt(BopOcidDecryptRequest request) {
        LOGGER.info(" 订单收货人信息解密请求参数:{}", JSONObject.toJSONString(request));
        String url = VmallBopConstant.url+VmallBopConstant.ocidDecryptUri; // 订单收货人信息解密
        String deviceTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).format(System.currentTimeMillis());
        String packPass = "";
        try {
            packPass = packPassword(VmallBopConstant.dataPasswd, VmallBopConstant.priKey, VmallBopConstant.deviceType, deviceTimeStamp);
        } catch (Exception e) {
            throw ThirdPartyException.buildException("密码加密出现异常"+e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("VmallDeviceType",VmallBopConstant.deviceType);
        headers.add("VmallDeviceAccount", VmallBopConstant.deviceAccount);
        headers.add("VmalltimeStamp", deviceTimeStamp);//头参数
        headers.add("VmallDevicePassword", packPass);//头参数
        LOGGER.debug("订单收货人信息解密请求头参数VmallDeviceType:{},VmallDeviceAccount:{},VmalltimeStamp:{},VmallDevicePassword:{}",
                VmallBopConstant.deviceType,VmallBopConstant.deviceAccount,deviceTimeStamp,packPass);
        LOGGER.info("订单收货人信息解密url:{}", url);
        String result = httpUtil.requestFor(url, HttpMethod.POST, headers, request, String.class);
        LOGGER.info("订单收货人信息解密返回:{}",result);
        BopAddressDecryptResponse response = JSONObject.parseObject(result, BopAddressDecryptResponse.class);
        return response;
    }


    public static String packPassword(String pass, String priKey, String deviceType, String deviceTimeStamp) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        String dataPasswd = String.format("%s%s%s", pass,
                encoder.encodeToString(deviceType.getBytes(Charset.forName("UTF-8"))), deviceTimeStamp);
        String encodePass = byteToHexString(hmacShar256(dataPasswd, generateKey(priKey)));
        return encodePass;
    }

    public static String byteToHexString(byte[] buf) {
        int len = buf.length;
        return IntStream.range(0, len).mapToObj(i -> {
            int b = buf[i] & 0XFF;
            return (b > XF) ? Integer.toHexString(buf[i] & XFF) : (ZERO + Integer.toHexString(buf[i] & XFF));
        }).collect(Collectors.joining());
    }

    public static byte[] hmacShar256(String message, String secKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(ALGORITHM_HMAC_SHAR256);
        SecretKeySpec secret_key = new SecretKeySpec(secKey.getBytes(), ALGORITHM_HMAC_SHAR256);
        mac.init(secret_key);
        return mac.doFinal(message.getBytes());
    }

    public static String generateKey(String encrypt) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHAR256);
        md.update(encrypt.getBytes(Charset.forName("UTF-8")));
        return byteToHexString(md.digest());
    }
}
