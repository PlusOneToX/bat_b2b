package com.bat.thirdparty.xxljob.service.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.xxljob.api.response.dto.XxlJobDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.thirdparty.xxljob.dto.XxlJobRpcCmd;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.api.request.XxlJobAddRequest;
import com.bat.thirdparty.xxljob.api.request.XxlJobListRequest;
import com.bat.thirdparty.xxljob.api.request.XxlJobLoginRequest;
import com.bat.thirdparty.xxljob.api.request.XxlJobStartRequest;
import com.bat.thirdparty.xxljob.api.response.XxlJobAddResponse;
import com.bat.thirdparty.xxljob.api.response.XxlJobListResponse;
import com.bat.thirdparty.xxljob.api.response.XxlJobResponse;
import com.bat.thirdparty.xxljob.api.response.dto.PromotionDTO;
import com.bat.thirdparty.xxljob.config.XxlJobConfig;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/2 8:54
 */
@Component
public class PromotionExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionExe.class);

    @DubboReference(check = false, timeout = 10000)
    private PromotionServiceDistributorRpc promotionServiceDistributorRpc;

    @Resource
    private XxlJobConfig xxlJobConfig;

    @Resource
    HttpUtil httpUtil;

    @CreateCache()
    private Cache<String, String> xxlJobCache;

    /**
     * 促销活动开始执行器
     */
    public void promotionStart(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.promotionStart(ids);
    }

    /**
     * 促销活动结束执行器
     */
    public void promotionStop(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.promotionStop(ids);
    }

    /**
     * 促销活动定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     *
     * @throws Exception
     */
    public void promotionStartStop() throws Exception {
        promotionServiceDistributorRpc.promotionStartStop();
    }

    /**
     * 拼团秒杀开始执行器
     */
    public void groupSeckillStart(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.groupSeckillStart(ids);
    }

    /**
     * 拼团秒杀结束执行器
     */
    public void groupSeckillStop(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.groupSeckillStop(ids);
    }

    /**
     * 拼团秒杀定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     *
     * @throws Exception
     */
    public void groupSeckillStartStop() throws Exception {
        promotionServiceDistributorRpc.groupSeckillStartStop();
    }

    /**
     * 优惠券开始执行器
     */
    public void couponStart(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.couponStart(ids);
    }

    /**
     * 优惠券结束执行器
     */
    public void couponStop(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.couponStop(ids);
    }

    /**
     * 优惠券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     *
     * @throws Exception
     */
    public void couponStartStop() throws Exception {
        promotionServiceDistributorRpc.couponStartStop();
    }

    /**
     * 返利代金券开始执行器
     */
    public void rebateVoucherStart(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.rebateVoucherStart(ids);
    }

    /**
     * 返利代金券结束执行器
     */
    public void rebateVoucherStop(List<Integer> ids) throws Exception {
        promotionServiceDistributorRpc.rebateVoucherStop(ids);
    }

    /**
     * 返利代金券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     *
     * @throws Exception
     */
    public void rebateVoucherStartStop() throws Exception {
        promotionServiceDistributorRpc.rebateVoucherStartStop();
    }

    /**
     * 创建定时任务
     *
     * @param cmds
     * @return
     */
    public void xxlJobAdd(List<XxlJobRpcCmd> cmds) {
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                // 先按条件查询，判断任务是否已经创建过
                XxlJobListRequest listRequest = new XxlJobListRequest();
                BeanUtils.copyProperties(cmd, listRequest);
                HttpHeaders headers = getHttpHeaders();
                XxlJobListResponse listResponse =
                    httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_LIST_URI(),
                        HttpMethod.POST, headers, listRequest, XxlJobListResponse.class);
                if (listResponse == null) {
                    xxlJobCache.remove(TenantContext.getTenantNo() + ":" + "xxlJobCookie");
                    xxlJobLogin(headers);
                    listResponse = httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_LIST_URI(),
                        HttpMethod.POST, headers, listRequest, XxlJobListResponse.class);
                }
                if (listResponse != null) {
                    if (listResponse.getRecordsTotal() != null && listResponse.getRecordsTotal() == 0) {
                        addJob(cmd, headers);
                    } else if (listResponse.getData() != null && cmd.getOverwrite()) {
                        for (XxlJobDTO dto : listResponse.getData()) {
                            XxlJobStartRequest startRequest = new XxlJobStartRequest();
                            startRequest.setId(dto.getId());
                            XxlJobResponse deleteResponse =
                                httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_DELETE_URI(),
                                    HttpMethod.POST, headers, startRequest, XxlJobResponse.class);
                        }
                        addJob(cmd, headers);
                    }
                }
            });
        }
    }

    private void addJob(XxlJobRpcCmd cmd, HttpHeaders headers) {
        XxlJobAddRequest addRequest = getXxlJobAddRequest(cmd);
        XxlJobAddResponse addResponse =
            httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_ADD_URI(), HttpMethod.POST,
                headers, addRequest, XxlJobAddResponse.class);
        // if (addResponse.getCode().intValue() != 200) {
        // throw ThirdPartyException.buildException(B_PROMOTION_JOB_ADD_ERROR,
        // MessageUtils.get("B_PROMOTION_JOB_ADD_ERROR") + cmd.getJobDesc());
        // }
        String jobId = addResponse.getContent();
        if (StringUtils.isNotBlank(jobId)) {
            XxlJobStartRequest startRequest = new XxlJobStartRequest();
            startRequest.setId(Integer.valueOf(jobId));
            httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_START_URI(), HttpMethod.POST,
                headers, startRequest, XxlJobResponse.class);
        }
    }

    /**
     * 组装定时任务参数
     * 
     * @param cmd
     * @return
     */
    public XxlJobAddRequest getXxlJobAddRequest(XxlJobRpcCmd cmd) {
        XxlJobAddRequest addRequest = new XxlJobAddRequest();
        BeanUtils.copyProperties(cmd, addRequest);
        PromotionDTO dto = new PromotionDTO();
        dto.setIds(cmd.getExecutorParam());
        dto.setTenantNo(TenantContext.getTenantNo());
        addRequest.setExecutorParam(JSON.toJSONString(dto));
        if (StringUtils.isBlank(cmd.getScheduleType())) {
            // 如果是数字则 判断为 固定速率类型 其他为cron 表达式（cron没有校验）
            if (StringUtils.isNumeric(cmd.getScheduleConf())) {
                addRequest.setScheduleType(XxlJobRpcCmd.SCHEDULE_TYPE_FIX_RATE);
            } else {
                addRequest.setScheduleType(XxlJobRpcCmd.SCHEDULE_TYPE_CRON);
            }
        }
        addRequest.setGlueType(xxlJobConfig.getGlueType());
        addRequest.setExecutorRouteStrategy(xxlJobConfig.getExecutorRouteStrategy());
        addRequest.setMisfireStrategy(xxlJobConfig.getMisfireStrategy());
        addRequest.setExecutorBlockStrategy(xxlJobConfig.getExecutorBlockStrategy());
        addRequest.setExecutorTimeout(xxlJobConfig.getExecutorTimeout());
        addRequest.setExecutorFailRetryCount(xxlJobConfig.getExecutorFailRetryCount());
        return addRequest;
    }

    public void xxlJobLogin(HttpHeaders headers) {
        XxlJobLoginRequest loginRequest = new XxlJobLoginRequest();
        loginRequest.setUserName(xxlJobConfig.getUserName());
        loginRequest.setPassword(xxlJobConfig.getPassword());
        loginRequest.setIfRemember(xxlJobConfig.getIfRemember());
        String cookie = httpUtil.requestForCookie(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_LOGIN_URI(),
            HttpMethod.POST, headers, loginRequest, String.class);
        if (StringUtils.isBlank(cookie)) {
            throw ThirdPartyException.buildException(ErrorCode.B_PROMOTION_JOB_LOGIN_ERROR);
        }
        xxlJobCache.put(TenantContext.getTenantNo() + ":" + "xxlJobCookie", cookie);
    }

    /**
     * 获取请求header
     *
     * @return
     */
    public HttpHeaders getHttpHeaders() {
        String cookie = xxlJobCache.get(TenantContext.getTenantNo() + ":" + "xxlJobCookie");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");
        if (StringUtils.isNotBlank(cookie)) {
            headers.add("Cookie", cookie);
        }
        return headers;
    }

    public void removeXxlJobCookie(String xxlJobCookie) {
        xxlJobCache.remove(TenantContext.getTenantNo() + ":" + xxlJobCookie);
    }
}
