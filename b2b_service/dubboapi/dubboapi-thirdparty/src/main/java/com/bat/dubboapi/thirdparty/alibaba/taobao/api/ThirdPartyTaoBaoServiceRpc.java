package com.bat.dubboapi.thirdparty.alibaba.taobao.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.alibaba.taobao.dto.TaoBaoResp;

public interface ThirdPartyTaoBaoServiceRpc {
    Response<TaoBaoResp> getItemSeller(String appkey, String secret, String sessionKey, String url,Long numId);
}
