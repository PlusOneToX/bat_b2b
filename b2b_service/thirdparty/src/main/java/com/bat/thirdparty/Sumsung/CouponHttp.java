package com.bat.thirdparty.Sumsung;


import com.bat.thirdparty.Sumsung.request.CouponIsQualifiedRequest;
import com.bat.thirdparty.Sumsung.request.CouponRequest;
import com.bat.thirdparty.Sumsung.response.BaseResponse;
import com.bat.thirdparty.Sumsung.response.CouponIsQualifiedResponse;
import com.bat.thirdparty.common.http.HttpUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CouponHttp {

    @Resource
    private HttpUtil httpUtil;

    /**
     * 优惠券同步
     * @param couponRequest
     * @param url
     * @return
     */
    public BaseResponse sysCoupon(CouponRequest couponRequest, String url) {
        return httpUtil.requestFor(url, HttpMethod.POST,couponRequest, BaseResponse.class);
    }

    public CouponIsQualifiedResponse sysCouponIsQualified(CouponIsQualifiedRequest couponRequest, String url) {
        return httpUtil.requestFor(url,HttpMethod.POST,couponRequest, CouponIsQualifiedResponse.class);
    }
}
