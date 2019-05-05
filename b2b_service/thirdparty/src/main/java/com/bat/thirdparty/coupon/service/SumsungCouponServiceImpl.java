package com.bat.thirdparty.coupon.service;

import static com.bat.thirdparty.Sumsung.constants.Constant.SA_ERROR_CODE_SUCCESS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.promotion.api.PromotionServiceCustomerRpc;
import com.bat.dubboapi.promotion.dto.data.CouponCustomerRpcDTO;
import com.bat.thirdparty.Sumsung.CouponHttp;
import com.bat.thirdparty.Sumsung.request.CouponRequest;
import com.bat.thirdparty.Sumsung.response.BaseResponse;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.coupon.api.SumsungCouponService;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.utils.CommonUtil;

@Service
public class SumsungCouponServiceImpl implements SumsungCouponService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SumsungCouponServiceImpl.class);

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private CustomerServiceRpc customerServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private PromotionServiceCustomerRpc promotionServiceCustomerRpc;

    private Short postReason = 1;

    @Value("${sumsung.couponDetailUrl}")
    private String couponDetailUrl;

    @Value("${sumsung.couponUrl}")
    private String couponUrl;

    @Resource
    private CouponHttp couponHttp;

    @Autowired
    private OrderBusinessLogServiceI orderReceiveLogServiceI;

    @Override
    public void syn(String userIds) throws Exception {
        // 日志输出
        Gson gson = new GsonBuilder().create();
        // 找出生活助手的用户
        String[] splits = userIds.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String split : splits) {
            ids.add(Integer.valueOf(split));
        }
        List<CustomerRpcDTO> customerRpcDTOS = customerServiceRpc.listByIds(ids).getData();
        for (CustomerRpcDTO user : customerRpcDTOS) {
            if (user != null && StringUtils.isNotBlank(user.getOtherId())) {
                CouponRequest couponRequest = getCouponRequest(user, gson);
                if (couponRequest == null) {
                    return;
                }
                String requestJson = gson.toJson(couponRequest);
                BaseResponse baseResponse = couponHttp.sysCoupon(couponRequest, couponUrl);
                String responseJson = gson.toJson(baseResponse);
                if (baseResponse != null && baseResponse.getStatusCode().equals(SA_ERROR_CODE_SUCCESS)) {
                    sendLoger(user.getDistributor().getId(), user.getPlatform(), requestJson,
                        ThirdCommonConstant.API_REQUEST_SUCCESS, "", responseJson);
                } else {
                    sendLoger(user.getDistributor().getId(), user.getPlatform(), requestJson,
                        ThirdCommonConstant.API_REQUEST_FAIL, baseResponse.getMessage(), responseJson);
                }
            }
        }
        return;

    }

    private CouponRequest getCouponRequest(CustomerRpcDTO user, Gson gson) {
        List<CouponCustomerRpcDTO> couponUserBeans =
            promotionServiceCustomerRpc.listCouponCustomerByCustomerId(user.getId()).getData();
        CouponRequest request = new CouponRequest();
        request.setPostReason(postReason);
        List<CouponRequest.CouponData> couponDataList = new ArrayList<>();
        for (CouponCustomerRpcDTO couponUserBean : couponUserBeans) {
            CouponRequest.CouponData couponData = new CouponRequest.CouponData();
            if (StringUtils.isEmpty(user.getOtherId())) {
                continue;
            }
            couponData.setId(couponUserBean.getId().longValue());
            couponData.setUid(user.getOtherId());
            if (couponUserBean.getCouponMethod() == 1) {// 满减
                couponData.setPrice(couponUserBean.getReduction().doubleValue());
            } else if (couponUserBean.getCouponMethod() == 2) {// 满折
                continue;
            } else if (couponUserBean.getCouponMethod() == 3) {// 兑换
                continue;
            }
            // 0 未发布,1 未开始, 2 进行中,
            // 3 已过期,4 提前结束 5已作废
            // 6 已使用
            if (couponUserBean.getCouponStatus() == 1 || couponUserBean.getCouponStatus() == 2) {
                couponData.setStatus((short)0);
            } else if (couponUserBean.getCouponStatus() == 6) {
                couponData.setStatus((short)1);
            } else {
                continue;
            }
            couponData
                .setStartTime(CommonUtil.formatDate(couponUserBean.getStartTime().getTime(), "yyyy-MM-dd HH:mm:ss"));
            couponData.setEndTime(CommonUtil.formatDate(couponUserBean.getEndTime().getTime(), "yyyy-MM-dd HH:mm:ss"));
            couponData.setUrl(couponDetailUrl);
            couponData.setDetailInformation(couponUserBean.getCouponName());
            couponDataList.add(couponData);
        }
        if (couponDataList.size() == 0) {
            return null;
        }
//        LOGGER.info("同步优惠券数据：{}", JSON.toJSONString(couponDataList));
        request.setCouponData(CommonUtil.base64(gson.toJson(couponDataList)));
        return request;
    }

    private void sendLoger(Integer distributorId, String platform, String requestParamJson, short status,
        String errorMsg, String businessData) {
        try {
            OrderBusinessLogDO pushLogDO = new OrderBusinessLogDO();
            pushLogDO.setLogType(OrderBusinessLogEnum.PUSH_TO_SUMSUNG.getLogType());
            pushLogDO.setTowardType(OrderBusinessLogEnum.PUSH_TO_SUMSUNG.getTowardType());
            pushLogDO.setDistributorId(distributorId);
            pushLogDO.setPlatform(platform);
            pushLogDO.setRequestParamJson(requestParamJson);
            pushLogDO.setCreateTime(new Date());
            pushLogDO.setStatus(status);
            pushLogDO.setErrorMsg(errorMsg);
            pushLogDO.setBusinessData(businessData);
            orderReceiveLogServiceI.create(pushLogDO);
        } catch (Exception e) {
            LOGGER.error("插入第三方日志出现异常:{}", e);
        }
    }
}
