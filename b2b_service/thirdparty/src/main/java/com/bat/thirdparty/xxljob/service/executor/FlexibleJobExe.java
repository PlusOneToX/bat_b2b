package com.bat.thirdparty.xxljob.service.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.api.request.XxlJobListRequest;
import com.bat.thirdparty.xxljob.api.request.XxlJobStartRequest;
import com.bat.thirdparty.xxljob.api.response.XxlJobAddResponse;
import com.bat.thirdparty.xxljob.api.response.XxlJobListResponse;
import com.bat.thirdparty.xxljob.api.response.XxlJobResponse;
import com.bat.thirdparty.xxljob.api.response.dto.TenantDTO;
import com.bat.thirdparty.xxljob.api.response.dto.XxlJobDTO;
import com.bat.thirdparty.xxljob.config.XxlJobConfig;

@Component
public class FlexibleJobExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleJobExe.class);

    @Resource
    private XxlJobConfig xxlJobConfig;

    @Resource
    private HttpUtil httpUtil;

    @Value("${xxl.job.actuator.flexible.id}")
    private Integer flexibleActuatorId;

    @Autowired
    private PromotionExe promotionExe;

    /**
     * 保存同步工厂定时任务
     * 
     * @param timeList
     */
    public void saveSyncFactoryXxlJob(List<String> timeList) {
        LOGGER.info("设置柔性订单同步工厂{}", JSON.toJSONString(timeList));
        if (timeList == null || timeList.size() == 0) {
            return;
        }

        // 同步B2B订单到工厂的所以主键id列表（用于编辑删除、原来三条、后面变成2条、删除1条）
        List<Integer> allIdList = null;
        // 前缀、不可修改
        String pre = "同步B2B订单到工厂每天";
        XxlJobListRequest listRequest = new XxlJobListRequest();
        listRequest.setJobDesc(pre);
        listRequest.setJobGroup(flexibleActuatorId);
        listRequest.setTriggerStatus(-1);
        HttpHeaders httpHeaders = promotionExe.getHttpHeaders();
        XxlJobListResponse listResponse =
            httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_LIST_URI(), HttpMethod.POST,
                httpHeaders, listRequest, XxlJobListResponse.class);
        LOGGER.info("条件查询同步工厂定时器、返回{}", JSON.toJSONString(listResponse));

        if (listResponse == null) {
            promotionExe.removeXxlJobCookie("xxlJobCookie");
            promotionExe.xxlJobLogin(httpHeaders);
            listResponse = httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_LIST_URI(),
                HttpMethod.POST, httpHeaders, listRequest, XxlJobListResponse.class);
        }
        if (listResponse != null && listResponse.getData() != null && listResponse.getData().size() > 0) {
            // 原来所有的id列表
            allIdList = listResponse.getData().stream().map(XxlJobDTO::getId).collect(Collectors.toList());
        } else {
            allIdList = new ArrayList<>();
        }
        for (int x = 0; x < timeList.size(); x++) {
            XxlJobDTO xxlJobDTO = new XxlJobDTO();
            xxlJobDTO.setJobGroup(flexibleActuatorId);
            xxlJobDTO.setJobDesc(pre + "第" + (x + 1) + "次");
            xxlJobDTO.setAuthor("thirdparty");
            xxlJobDTO.setScheduleType(xxlJobConfig.getScheduleType());
            xxlJobDTO.setGlueType(xxlJobConfig.getGlueType());
            String cron = getCronByTime(timeList.get(x));
            xxlJobDTO.setSchedule_conf_CRON(cron);
            xxlJobDTO.setScheduleConf(cron);
            xxlJobDTO.setCronGen_display(cron);
            // 设置路由策略
            xxlJobDTO.setExecutorRouteStrategy(xxlJobConfig.getExecutorRouteStrategy());
            /* requestInfo.put("scheduleConf","0 0 0/2 * * ? ");
            requestInfo.put("cronGen_display","0 0 0/2 * * ? ");
            requestInfo.put("schedule_conf_CRON","0 0 0/2 * * ? ");*/
            xxlJobDTO.setExecutorHandler("diyOrderSyncToFactory");
            // requestInfo.put("executorHandler","warehouseErpSyncB2BJobHandler");
            xxlJobDTO.setExecutorBlockStrategy(xxlJobConfig.getExecutorBlockStrategy());
            // requestInfo.put("executorRouteStrategy","FIRST");
            xxlJobDTO.setMisfireStrategy(xxlJobConfig.getMisfireStrategy());
            // requestInfo.put("misfireStrategy","DO_NOTHING");
            xxlJobDTO.setExecutorBlockStrategy(xxlJobConfig.getExecutorBlockStrategy());
            // requestInfo.put("executorBlockStrategy","SERIAL_EXECUTION");
            // requestInfo.put("executorTimeout",0);
            xxlJobDTO.setExecutorTimeout(0);
            // 不重试
            xxlJobDTO.setExecutorFailRetryCount(xxlJobConfig.getExecutorFailRetryCount());
            // 设置租户编码参数
            TenantDTO dto = new TenantDTO();
            dto.setTenantNo(TenantContext.getTenantNo());
            xxlJobDTO.setExecutorParam(JSON.toJSONString(dto));
            // requestInfo.put("executorFailRetryCount",3);
            if (x < allIdList.size()) {
                // 修改(前面先修改)
                xxlJobDTO.setId(allIdList.get(x));
                LOGGER.info("修改同步工厂定时器、参数{}", JSON.toJSONString(xxlJobDTO));
                XxlJobListResponse response =
                    httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_UPDATE_URI(),
                        HttpMethod.POST, httpHeaders, xxlJobDTO, XxlJobListResponse.class);
                LOGGER.info("修改同步工厂定时器、返回{}", JSON.toJSONString(response));
            } else {
                // 新增
                LOGGER.info("新增同步工厂定时器、参数{}", JSON.toJSONString(xxlJobDTO));
                XxlJobAddResponse addResponse =
                    httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_ADD_URI(), HttpMethod.POST,
                        httpHeaders, xxlJobDTO, XxlJobAddResponse.class);
                LOGGER.info("新增同步工厂定时器、返回{}", JSON.toJSONString(addResponse));
                xxlJobDTO.setId(Integer.parseInt(addResponse.getContent()));
            }
            XxlJobStartRequest startRequest = new XxlJobStartRequest();
            startRequest.setId(xxlJobDTO.getId());
            String startUrl = xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_START_URI();
            XxlJobResponse startResponse =
                httpUtil.requestFor(startUrl, HttpMethod.POST, httpHeaders, startRequest, XxlJobResponse.class);
            LOGGER.info("启用定时器、返回{}", JSON.toJSONString(startResponse));
        }
        if (allIdList.size() > timeList.size()) {
            // 有的记录被删除
            for (int y = timeList.size(); y < allIdList.size(); y++) {
                XxlJobStartRequest startRequest = new XxlJobStartRequest();
                startRequest.setId(allIdList.get(y));
                LOGGER.info("删除连接URL,{}", xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_DELETE_URI());
                XxlJobResponse deleteResponse =
                    httpUtil.requestFor(xxlJobConfig.getBaseUri() + xxlJobConfig.getJOB_INFO_DELETE_URI(),
                        HttpMethod.POST, httpHeaders, startRequest, XxlJobResponse.class);
                LOGGER.info("删除定时任务、响应{}", JSON.toJSONString(deleteResponse));
            }
        }
    }

    private static String getCronByTime(String time) {
        String cron = "0 0 0/2 * * ? ";
        // 只要时分秒数据
        String hours = "";
        String minutes = "";
        String seconds = "";
        // 去掉年月日
        if (time.indexOf(" ") > -1) {
            time = time.split(" ")[1];
        }
        String[] arr = time.split(":");
        hours = hours + arr[0] + ",";
        minutes = minutes + arr[1] + ",";
        seconds = seconds + arr[2] + ",";
        // 去掉后面的逗号
        hours = hours.substring(0, hours.length() - 1);
        minutes = minutes.substring(0, minutes.length() - 1);
        seconds = seconds.substring(0, seconds.length() - 1);
        return seconds + " " + minutes + " " + hours + " * * ? ";
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("2019-07-10 12:00:16");
        list.add("2019-07-10 12:10:36");
        String cronByTimeList = getCronByTime(list.get(1));
        System.out.println(cronByTimeList);
    }
}
