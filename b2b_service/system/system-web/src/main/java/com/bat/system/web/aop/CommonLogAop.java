package com.bat.system.web.aop;

import java.util.Date;

import javax.annotation.Resource;

import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.system.Tenant.TenantContext;
import com.bat.system.api.base.SystemException;
import com.bat.system.api.message.MessageSendServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * 沙漠
 */
@Aspect
@Component
@Slf4j
public class CommonLogAop extends BaseController {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    private MessageSendServiceI sendService;

    @Pointcut("@annotation(com.bat.system.web.annotation.SysLog)")
    public void commonLog() {}

    /**
     * 公用日志切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("commonLog()&& @annotation(sysLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, SysLog sysLog) throws Throwable {
        Object response = joinPoint.proceed();
        try {
            Response proceed = JSONObject.parseObject(JSONObject.toJSONString(response), Response.class);
            // 线程池外 才能获取http request
            String userIdStr = getUserId();
            Integer userId = null;
            if (StringUtils.isNotBlank(userIdStr)) {
                userId = Integer.valueOf(userIdStr);
            }
            String platform = getPlatform();
            String userName = getUserName();
            Integer finalUserId = userId;
            // 开启子线程需指定租户编码
            String tenantNo = TenantContext.getTenantNo();
            threadPoolTaskScheduler.submit(() -> {
                TenantContext.setTenantNo(tenantNo);
                // 定义请求数据
                String operateData = "[]";
                String jsonStr = "{}";
                try {
                    Object[] args = joinPoint.getArgs();
                    if (args != null) {
                        operateData = JSON.toJSONString(args);
                        if (args.length == 1) {
                            jsonStr = JSON.toJSONString(args[0]);
                        }
                    }
                } catch (Exception e) {
                    log.error("获取请求参数失败！{}", e.getMessage());
                }
                // 定义业务id
                Integer businessId = null;
                String operateDes = "操作成功";
                if (!proceed.isSuccess()) {
                    operateDes = "操作失败！错误码:" + proceed.getErrCode() + ",错误信息:" + proceed.getErrMessage();
                }
                try {
                    JSONObject json = JSONObject.parseObject(jsonStr);
                    Object id = json.get("id");
                    if (id != null && StringUtils.isNotBlank(id.toString())) {
                        businessId = Integer.valueOf(id.toString());
                    } else {
                        if (proceed.getData() != null) {
                            json = JSONObject.parseObject(JSONObject.toJSONString(proceed.getData()));
                            id = json.get("id");
                            if (StringUtils.isNotBlank(id.toString())) {
                                businessId = Integer.valueOf(id.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("获取业务id失败！{}", e.getMessage());
                }
                sendService.commonLog("order-service", sysLog.businessFunction(), businessId, platform, finalUserId,
                    userName, sysLog.value(), operateDes, operateData, new Date());
                TenantContext.removeTenantNo();
            });
        } catch (Exception e) {
            log.error("添加日志失败！{}", e.getMessage());
        }
        return response;
    }

    /**
     * 方法执行后 并抛出异常
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value = "commonLog() && @annotation(sysLog)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, SysLog sysLog, Exception ex) {
        try {
            log.info("请求：{},出现异常{}", sysLog.value(), ex.getMessage());
            if (ex == null) {
                return;
            }
            if (ex instanceof BindException) {
                log.info("参数校验异常；不给予记录");
                return;
            }
            // 线程池外 才能获取http request
            String userIdStr = getUserId();
            Integer userId = null;
            if (StringUtils.isNotBlank(userIdStr)) {
                userId = Integer.valueOf(userIdStr);
            }
            String platform = getPlatform();
            String userName = getUserName();
            Integer finalUserId = userId;
            // 开启子线程需指定租户编码
            String tenantNo = TenantContext.getTenantNo();
            threadPoolTaskScheduler.submit(() -> {
                TenantContext.setTenantNo(tenantNo);
                // 定义请求数据
                String operateData = "{}";
                String jsonStr = "{}";
                try {
                    Object[] args = joinPoint.getArgs();
                    if (args != null) {
                        operateData = JSON.toJSONString(args);
                        if (args.length == 1) {
                            jsonStr = JSON.toJSONString(args[0]);
                        }
                    }
                } catch (Exception e) {
                    log.error("获取请求参数失败！{}", e.getMessage());
                }
                String code = "";
                String msg = ex.getMessage();
                if (ex instanceof SystemException) {
                    SystemException systemException = (SystemException)ex;
                    code = systemException.getCode();
                    msg = systemException.getMsg();
                }

                String operateDes = "操作失败！错误码:" + code + ",错误信息:" + msg;

                // 定义业务id
                Integer businessId = null;
                try {
                    JSONObject json = JSONObject.parseObject(jsonStr);
                    Object id = json.get("id");
                    if (id != null && StringUtils.isNotBlank(id.toString())) {
                        businessId = Integer.valueOf(id.toString());
                    }
                } catch (Exception e) {
                    log.error("获取业务id失败！{}", e.getMessage());
                }
                sendService.commonLog("system-service", sysLog.businessFunction(), businessId, platform, finalUserId,
                    userName, sysLog.value(), operateDes, operateData, new Date());
                TenantContext.removeTenantNo();
            });
        } catch (Exception e) {
            log.error("添加异常日志失败！{}", e.getMessage());
        }
    }
}
