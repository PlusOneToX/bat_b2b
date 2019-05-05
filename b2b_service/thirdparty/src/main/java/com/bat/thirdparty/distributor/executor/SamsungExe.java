package com.bat.thirdparty.distributor.executor;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.CustomerRpcQry;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.promotion.api.PromotionServiceCustomerRpc;
import com.bat.dubboapi.promotion.dto.CouponCustomerRpcQry;
import com.bat.dubboapi.promotion.dto.data.CouponCustomerRpcDTO;
import com.bat.thirdparty.distributor.api.Response.SamsungQualifiedResponse;
import com.bat.thirdparty.distributor.api.dto.SamsungAccountQry;
import com.bat.thirdparty.distributor.api.dto.SamsungAccountReq;
import com.bat.thirdparty.distributor.api.dto.SamsungCheckTokenQry;
import com.bat.thirdparty.distributor.api.request.SamsungTokenRequest;
import com.bat.thirdparty.distributor.config.SamsungConfig;
import com.bat.thirdparty.distributor.enumtype.SamsungResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 11:54
 */
@Component
@Slf4j
public class SamsungExe {

    @Resource
    private HttpUtil httpUtil;

    @Resource
    private SamsungConfig samsungConfig;

    @DubboReference(check = false, timeout = 5000)
    private CustomerServiceRpc customerServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceCustomerRpc promotionServiceCustomerRpc;

    /**
     * 账号判断是否资质领券
     *
     * @param qry
     * @return
     */
    public Short couponQualified(SamsungAccountQry qry) {
        try {
            System.out.println(qry);
            log.info("账号判断是否资质领券请求数据：" + qry);
            if (qry == null || StringUtils.isBlank(qry.getAccountId())) {
                throw ThirdPartyException.buildException(ErrorCode.B_SAMSUNG_ERROR);
            }
            log.info("账户：{} bat侧判断是否有领券资质", qry.getAccountId());
            SamsungAccountReq accountQry = new SamsungAccountReq();
            accountQry.setAccountId(qry.getAccountId());
            accountQry.setImei("imei");
            accountQry.setModelName("modelName");
            SamsungQualifiedResponse response = httpUtil.requestFor(samsungConfig.getQualifiedUrl(), HttpMethod.POST,
                accountQry, SamsungQualifiedResponse.class);
            log.info("result：" + response.getResult());
            log.info("statusCode：" + response.getStatusCode());
            log.info("message：" + response.getMessage());
            //如果接收的结果为空 或者 statusCode的结果不等于SA_0000
            if (response == null || StringUtils.isBlank(response.getStatusCode())
                || !response.getStatusCode().equals(SamsungResponseStatus.SUCCESS.getName())) {
                throw ThirdPartyException.buildException(ErrorCode.B_SAMSUNG_ERROR,
                    //返回获取领券资格失败
                    qry.getAccountId() + MessageUtils.get(ErrorCode.B_SAMSUNG_ERROR));
            }
            //定义一个状态码0
            short status = 0;
            //如果statusCode的结果等于SA_0000，则通过
            if (response.getStatusCode().equals(SamsungResponseStatus.SUCCESS.getName())) {
                status = 1;
            }
            //最终返回给的结果
            log.info("最终返回给的结果：" + status);
            return status;
        } catch (Exception e) {
            log.error("异常：" + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param qry
     * @return 0 没有领过券 1 领过券
     */
    public Short couponReceived(SamsungAccountQry qry) {
        if (qry == null || StringUtils.isBlank(qry.getAccountId()) || StringUtils.isBlank(qry.getCouponCode())) {
            throw ThirdPartyException.buildException("20001", "账户ID或优惠券编号不能为空");
        }
        log.info(" 账户：{} 优惠券码：{} 侧判断是否有领券资质", qry.getAccountId(), qry.getCouponCode());
        CustomerRpcQry rpcQry = new CustomerRpcQry();
        rpcQry.setOtherUid(qry.getAccountId());
        rpcQry.setOpenId(qry.getAuthenticateUserID());
        rpcQry.setPlatform(samsungConfig.getPlatform());
        rpcQry.setDistributorId(Integer.valueOf(samsungConfig.getDistributorId()));
        Response<List<CustomerRpcDTO>> response = customerServiceRpc.listCustomer(rpcQry);
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<CustomerRpcDTO> data = response.getData();
        if (CollectionUtils.isEmpty(data)) {
            log.info("不存在账户 默认可以领券 账户id： + qry.getAccountId() ");
            return 0;
            // throw ThirdPartyException.buildException("不存在账户 账户id：" + qry.getAccountId());
        }
        List<Integer> customerIds = data.stream().map(CustomerRpcDTO::getId).collect(Collectors.toList());
        CouponCustomerRpcQry customerRpcQry = new CouponCustomerRpcQry();
        customerRpcQry.setCustomerIds(customerIds);
        customerRpcQry.setCouponCode(qry.getCouponCode());
        // 优惠券 进行中
        customerRpcQry.setCouponStatus((short)2);
        com.bat.dubboapi.promotion.common.Response<List<CouponCustomerRpcDTO>> listResponse =
            promotionServiceCustomerRpc.listCouponCustomer(customerRpcQry);
        if (!listResponse.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<CouponCustomerRpcDTO> couponCustomerRpcs = listResponse.getData();
        if (!CollectionUtils.isEmpty(couponCustomerRpcs)) {
            return 1;
        }
        return 0;
    }

    /**
     * token验证
     * 
     * @param qry
     * @return
     */
    public String checkToken(SamsungCheckTokenQry qry) {
        SamsungTokenRequest tokenRequest = new SamsungTokenRequest();
        tokenRequest.setAuthToken(qry.getAuthToken());
        HttpHeaders httpHeaders = getHttpHeaders(qry);
        return httpUtil.requestFor(samsungConfig.getCheckTokenUrl() + qry.getAuthToken(), HttpMethod.POST, httpHeaders,
            tokenRequest, String.class);
    }

    public static HttpHeaders getHttpHeaders(SamsungCheckTokenQry qry) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("authorization", qry.getAuthorization());
        httpHeaders.add("x-osp-appId", qry.getXOspAppId());
        httpHeaders.add("x-osp-clientosversion", qry.getXOspClientosversion());
        httpHeaders.add("x-osp-clientmodel", qry.getXOspClientmodel());
        httpHeaders.add("x-osp-packagename", qry.getXOspPackagename());
        httpHeaders.add("x-osp-packageversion", qry.getXOspPackageversion());
        return httpHeaders;
    }
}
