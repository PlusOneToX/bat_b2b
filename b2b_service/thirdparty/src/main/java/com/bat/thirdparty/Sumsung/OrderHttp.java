package com.bat.thirdparty.Sumsung;

import com.bat.thirdparty.Sumsung.request.OrderRequest;
import com.bat.thirdparty.Sumsung.response.BaseResponse;
import com.bat.thirdparty.common.http.HttpUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderHttp {

    @Resource
    private HttpUtil httpUtil;

    /**
     * 订单同步
     * @param orderRequest
     * @param url
     * @return
     */
    public BaseResponse sysOrder(OrderRequest orderRequest, String url) {
        return httpUtil.requestFor(url, HttpMethod.POST, orderRequest, BaseResponse.class);
    }

}
