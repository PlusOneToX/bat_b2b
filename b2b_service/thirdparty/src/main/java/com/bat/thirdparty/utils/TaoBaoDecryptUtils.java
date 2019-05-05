package com.bat.thirdparty.utils;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.alibaba.taobao.api.dto.DataDecryptDTO;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.taobao.api.response.TopOaidDecryptResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class TaoBaoDecryptUtils {

    private static final Logger logger = LoggerFactory.getLogger(TaoBaoDecryptUtils.class);

    @Autowired
    private HttpUtil httpUtil;


    public Map<String, TopOaidDecryptResponse.Receiver> dataDecrypt(DataDecryptDTO dataDecryptDTO) {
        String url = "http://jst.bat.com/data/decrypt";
        logger.info("taobao解密数据传参:{}",JSONObject.toJSONString(dataDecryptDTO));
        ResponseBaseBean responseBaseBean = httpUtil.requestFor(url, HttpMethod.POST, dataDecryptDTO, ResponseBaseBean.class);
        logger.info("taobao解密数据返回:{}",JSONObject.toJSONString(responseBaseBean));
        if (responseBaseBean != null && responseBaseBean.getCode() == 0) {
            List<TopOaidDecryptResponse.Receiver> receivers = JSONObject.parseArray(JSONObject.toJSONString(responseBaseBean.getData()), TopOaidDecryptResponse.Receiver.class);
            Map<String, TopOaidDecryptResponse.Receiver> receiverMap = receivers.stream().collect(Collectors.toMap(TopOaidDecryptResponse.Receiver::getTid, a -> a, (k1, k2) -> k1));
            return receiverMap;
        } else {
            logger.error("解密出现异常:{}", responseBaseBean.getMsg());
            return new HashMap<>();
        }
    }
}
