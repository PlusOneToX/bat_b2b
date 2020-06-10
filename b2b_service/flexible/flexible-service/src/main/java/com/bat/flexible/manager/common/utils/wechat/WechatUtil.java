package com.bat.flexible.manager.common.utils.wechat;

import com.alibaba.fastjson.JSONObject;
import com.bat.flexible.manager.sql.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class WechatUtil {

    private static  String appId="" ;

    private static  String SECRET="" ;

    @Value("${wechat.program.appid}")
    public  void setAppId(String appId) {
        WechatUtil.appId = appId;
    }

    @Value("${wechat.program.secret}")
    public  void setSECRET(String SECRET) {
        WechatUtil.SECRET = SECRET;
    }



    /**
     * 获取小程序令牌
     * @return
     */
    public static String getWechatProgramToken() {
        try {
           /* String token = stringRedisTemplate.opsForValue().get(WechatConstant.Wechat_Access_Token);
            if(StringUtils.isNotBlank(token)){
                return token;
            }*/
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("grant_type", "client_credential");
            map.put("appid", appId);// 改成自己的appid
            map.put("secret", SECRET); //改成自己的secret
            String rt = HttpUtil.sendPost("https://api.weixin.qq.com/cgi-bin/token", map);
            JSONObject json = JSONObject.parseObject(rt);
            if (json.getString("access_token") != null || json.getString("access_token") != "") {
                System.out.println("token:" + json.getString("access_token"));
               // stringRedisTemplate.opsForValue().set(WechatConstant.Wechat_Access_Token,json.getString("access_token"),7000, TimeUnit.SECONDS);
                return json.getString("access_token");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    //sceneStr:链接到改小程序界面所要的参数
//accessToken:上一个方法中所生产的token
    public static InputStream getminiqrQr(String sceneStr) {
        String accessToken = getWechatProgramToken();
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", sceneStr);
            //param.put("page", "pages/code/code");
            param.put("width", 430);
            param.put("auto_color", false);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class,
                    new Object[0]);
            System.out.println("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            return inputStream;
        } catch (Exception e) {
            System.out.println("调用异常");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 生成随机数
     *
     * @return
     */
    public static String createNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static void main(String[] args) {
       // String token ="37_KDqiXPv9k0xxYMBoNWhFcvz5XDFL-E9LKUOuC_usxXTuI7ZbHFYFR0_9_6okPjTx4ey9c8YfS7cIYlRQUZaNDg3Ar66rg3Zt2MOZIJewbhU3xn0NCxYH32de87YpxwOBfub6Trc1wr-y63XlIVHaAGAYLX";
        getminiqrQr("aa=1");
    }
}
