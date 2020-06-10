package com.bat.flexible.global;

import static com.bat.flexible.common.ErrorCode.SYSTEM_DATA_ERROR;

import javax.annotation.Resource;

import com.bat.flexible.Tenant.Tenant;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alicp.jetcache.CacheException;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.db.service.executor.TenantDBCmd;

@RestControllerAdvice
public class GlobalExceptionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @Resource
    private TenantDBCmd tenantDBCmd;

    /**
     * 系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response exceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof MyBatisSystemException) {
            Tenant tenant = TenantContext.tenantInfoMap.get(TenantContext.getTenantNo());
            if (tenant != null) {
                tenantDBCmd.tenantDBDelete(tenant.getTenantNo(), tenant.getConfigMap().get("dbName"));
            }
            return Response.buildFailure(SYSTEM_DATA_ERROR, MessageUtils.get(SYSTEM_DATA_ERROR));
        }
        String errorCode = e.getMessage();
        if (StringUtils.isNotBlank(errorCode) && errorCode.indexOf("WarehouseDubboApiException:") > -1) {
            // 柔性定制的异常提示、截取第一行
            String[] msgArr = errorCode.split("\r\n");
            String messageCode = msgArr[0].split("WarehouseDubboApiException:")[1];
            return Response.buildFailure(messageCode, MessageUtils.get(messageCode));
        }
        if (StringUtils.isBlank(errorCode) || errorCode.length() > 100) {
            errorCode = GlobalErrorCode.SYSTEM_EXCEPTION;
            LOGGER.error(e.getMessage(), e);
        } else {
            LOGGER.error(MessageUtils.get(e.getMessage()), e);
        }
        return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
    }

    /**
     * 处理jetcache异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CacheException.class)
    public Response cacheExceptionHandler(CacheException e) {
        e.printStackTrace();
        LOGGER.error("CacheException{}", e);
        return Response.buildFailure(GlobalErrorCode.JETCACHE_EXCEPTION,
            MessageUtils.get(GlobalErrorCode.JETCACHE_EXCEPTION));
    }

    /**
     * 当请求参数不为空校验失败情况
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Response validExceptionHandler(Exception e) {
        e.printStackTrace();
        String msg = MessageUtils.get(GlobalErrorCode.PARAM_VALID_ERROR);
        if (e instanceof MethodArgumentNotValidException) {
            msg = MessageUtils
                .get((((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage()));
        } else if (e instanceof BindException) {
            msg = MessageUtils.get(((BindException)e).getBindingResult().getFieldError().getDefaultMessage());
        } else {
            return Response.buildFailure(GlobalErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(GlobalErrorCode.SYSTEM_EXCEPTION));
        }
        LOGGER.error(msg);
        return Response.buildFailure(GlobalErrorCode.PARAM_VALID_ERROR, msg);
    }

    /**
     * 柔性定制异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(FlexibleCustomException.class)
    public Response flexibleDiyHandler(FlexibleCustomException e) {
        e.printStackTrace();
        String errorCode = e.getMessage();
        if (StringUtils.isBlank(errorCode) || errorCode.length() > 1000) {
            errorCode = GlobalErrorCode.SYSTEM_EXCEPTION;
            LOGGER.error(e.getMessage(), e);
        } else {
            LOGGER.error(MessageUtils.get(e.getMessage()), e);
        }
        return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
    }

}
