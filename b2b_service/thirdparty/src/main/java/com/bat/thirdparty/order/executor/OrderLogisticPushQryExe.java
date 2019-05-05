package com.bat.thirdparty.order.executor;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.Sha1Handler;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.order.api.dto.jiuji.JiujiLogisticQueryDTO;
import com.bat.thirdparty.order.response.JiujiuLogisticResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class OrderLogisticPushQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLogisticPushQryExe.class);

    @Autowired
    private HttpUtil httpUtil;



    public ResponseBaseBean push(DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO, OrderLogistics logistics){
        HttpHeaders httpHeaders = httpUtil.getHttpHeaders(JSON.toJSONString(logistics), distributorPlatformApiRpcDTO.getAppId(),distributorPlatformApiRpcDTO.getAppKey());
        String url = distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri();
        LOGGER.info("开始同步物流信息url:"+url+"==={}"+JSON.toJSONString(logistics)+"、请求头{}"+JSON.toJSONString(httpHeaders));
        ResponseBaseBean responseBaseBean = httpUtil.requestFor(url, HttpMethod.POST,httpHeaders, logistics, ResponseBaseBean.class);
        LOGGER.info("开始同步物流信息、返回{}:",JSON.toJSONString(responseBaseBean));
        //判断是否要处理回调查询
        return responseBaseBean;
    }

    /**
     * 开发的物流回调查询接口
     * @param orderLogistics
     * @param appKey
     * @param appKey
     * @param jiujiLogisticQueryUrl
     */
    public JiujiuLogisticResponse jiujiLogisticQuery(OrderLogistics orderLogistics, String appKey, String appId, String jiujiLogisticQueryUrl)  {
        JiujiLogisticQueryDTO jiujiLogisticQueryDTO = new JiujiLogisticQueryDTO();
        jiujiLogisticQueryDTO.setDistributorId(orderLogistics.getDistributorId());
        jiujiLogisticQueryDTO.setOtherOrderNo(orderLogistics.getOtherOrderNo());
        jiujiLogisticQueryDTO.setUserId(orderLogistics.getUserId());
        jiujiLogisticQueryDTO.setExpressNo(orderLogistics.getExpressNo());
        String param = JSON.toJSONString(jiujiLogisticQueryDTO);
        LOGGER.info("系统物流同步查询接口查询参数："+param);
        //  JiujiuLogisticResponse jiujiuLogisticResponse = HttpUtil.postJsonWithHeader(jiujiLogisticQueryUrl,param,headerMap, JiujiuLogisticResponse.class);
        HttpHeaders httpHeaders = HttpUtil.getHttpHeaders(param, appId, appKey);
        JiujiuLogisticResponse jiujiuLogisticResponse = httpUtil.requestFor(jiujiLogisticQueryUrl,HttpMethod.POST,httpHeaders,jiujiLogisticQueryDTO,JiujiuLogisticResponse.class);
        LOGGER.info("系统物流同步查询接口返回结果："+JSON.toJSONString(jiujiuLogisticResponse));
        return jiujiuLogisticResponse;
    }

    private void addOrderLogisticsLog(OrderLogistics orderLogistics, JiujiuLogisticResponse jiujiuLogisticResponse,Short type) {
        /*OrderLogisticsFailLog log = new OrderLogisticsFailLog();
        log.setCreateTime(new Date());
        log.setExpressNo(orderLogistics.getExpressNo());
        log.setOrderId(Long.parseLong(orderLogistics.getOtherOrderNo()));
        log.setParamMsg(JSON.toJSONString(orderLogistics));
        log.setResponseMsg(JSON.toJSONString(jiujiuLogisticResponse));
        log.setType(type);
        orderLogisticsFailLogMapper.save(log);*/
    }

    public static void main(String[] args) {
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setDistributorId(752);
        System.out.println(Sha1Handler.encryption(JSON.toJSONString(orderLogistics)));
    }
}
