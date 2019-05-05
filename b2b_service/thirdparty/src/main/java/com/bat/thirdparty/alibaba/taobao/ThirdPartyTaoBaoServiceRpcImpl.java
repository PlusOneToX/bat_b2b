package com.bat.thirdparty.alibaba.taobao;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.alibaba.taobao.api.ThirdPartyTaoBaoServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.taobao.dto.TaoBaoResp;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemSellerGetRequest;
import com.taobao.api.response.ItemSellerGetResponse;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@DubboService
public class ThirdPartyTaoBaoServiceRpcImpl implements ThirdPartyTaoBaoServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyTaoBaoServiceRpcImpl.class);

    @Override
    public Response<TaoBaoResp> getItemSeller(String appkey, String secret, String sessionKey, String url,Long numId) {
        TaobaoClient taobaoClient = new DefaultTaobaoClient(url, appkey, secret);
        ItemSellerGetRequest req = new ItemSellerGetRequest();
        req.setFields("num_iid,title,nick,price,approve_status,sku,outer_id");
        req.setNumIid(numId);
        try {
            ItemSellerGetResponse itemSellerGetResponse = taobaoClient.execute(req, sessionKey);
            LOGGER.info("淘宝查询返回{}", JSON.toJSONString(itemSellerGetResponse));
            TaoBaoResp taoBaoResp = new TaoBaoResp();
            BeanUtils.copyProperties(itemSellerGetResponse,taoBaoResp);

            return Response.of(taoBaoResp);
        } catch (ApiException e) {
            e.printStackTrace();
            return Response.buildFailure("获取淘宝货品信息异常","获取淘宝信息异常");
        }
    }
}
