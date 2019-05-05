package com.bat.thirdparty.common.config;

import static com.bat.thirdparty.common.CommonErrorCode.P_NOTNULL;
import static com.bat.thirdparty.common.CommonErrorCode.SYSTEM_DATA_ERROR;

import javax.annotation.Resource;

import com.bat.thirdparty.tenant.Tenant;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.CommonErrorCode;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyBaseException;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.db.service.executor.TenantDBCmd;
import com.bat.thirdparty.common.util.MessageUtils;

@RestControllerAdvice
public class GlobalExceptionConfig {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @Resource
    private TenantDBCmd tenantDBCmd;

    /**
     * 当请求参数不为空校验失败情况
     *
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Response bindHandler(Exception e) {
        String msg = null;
        if (e instanceof MethodArgumentNotValidException) {
            msg = MessageUtils
                .get(((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage());
        } else if (e instanceof BindException) {
            msg = MessageUtils.get(((BindException)e).getBindingResult().getFieldError().getDefaultMessage());
        } else {
            return Response.buildFailure(CommonErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(CommonErrorCode.SYSTEM_EXCEPTION));
        }
        logger.error(msg);
        if (StringUtils.isNotBlank(msg)) {
            return Response.buildFailure(P_NOTNULL, msg);
        } else {
            return Response.buildFailure(P_NOTNULL, P_NOTNULL);
        }
    }

    /**
     * 服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ThirdPartyException.class)
    public Response goodsHandler(ThirdPartyException e) {
        e.printStackTrace();
        String errorCode = e.getCode();
        logger.error(errorCode);
        if (StringUtils.isBlank(errorCode)) {
            errorCode = CommonErrorCode.SYSTEM_EXCEPTION;
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        }
        String msg = e.getMsg();
        if (StringUtils.isNotBlank(msg)) {
            logger.error("ThirdPartyException,{}", msg);
            return Response.buildFailure(errorCode, msg);
        }
        msg = MessageUtils.get(errorCode);
        if (StringUtils.isNotBlank(msg)) {
            logger.error("ThirdPartyException,{}", msg);
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        } else {
            logger.error("ThirdPartyException,errorCode{}", errorCode);
            return Response.buildFailure(errorCode, errorCode);
        }
    }

    /**
     * 未捕获的全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response exceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof MyBatisSystemException) {
            // 清除租户数据库问题
            Tenant tenant = TenantContext.tenantInfoMap.get(TenantContext.getTenantNo());
            if (tenant != null) {
                tenantDBCmd.tenantDBDelete(tenant.getTenantNo(), tenant.getConfigMap().get("dbName"));
            }
            return Response.buildFailure(SYSTEM_DATA_ERROR, MessageUtils.get(SYSTEM_DATA_ERROR));
        }
        if (e instanceof UncategorizedMongoDbException) {
            // 清除mongodb记录
            Tenant tenant = TenantContext.tenantInfoMap.get(TenantContext.getTenantNo());
            if (tenant != null && StringUtils.isNotBlank(tenant.getConfigMap().get("mongodbUri"))) {
                TenantContext.mongoDFactoryMap.remove(tenant.getConfigMap().get("mongodbUri"));
            }
            TenantContext.tenantInfoMap.remove(TenantContext.getTenantNo());
            return Response.buildFailure(SYSTEM_DATA_ERROR, MessageUtils.get(SYSTEM_DATA_ERROR));
        }
        return Response.buildFailure(CommonErrorCode.SYSTEM_EXCEPTION,
            MessageUtils.get(CommonErrorCode.SYSTEM_EXCEPTION));
    }

    /**
     * 服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ThirdPartyOpenApiException.class)
    public ResponseBaseBean openApiHandler(ThirdPartyOpenApiException e) {
        String errorMessage = e.getMessage();
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = CommonErrorCode.SYSTEM_EXCEPTION;
            logger.error("ThirdPartyOpenApiException:", e.getMessage());
            return ResponseBaseBean.responseBean(20020, MessageUtils.get(errorMessage));
        }
        String msg = MessageUtils.get(errorMessage);
        if (StringUtils.isNotBlank(msg)) {
            logger.error("ThirdPartyOpenApiException:{}", msg);
            return ResponseBaseBean.responseBean(20020, msg);
        } else {
            logger.error("ThirdPartyOpenApiException:{}", errorMessage);
            return ResponseBaseBean.responseBean(20020, errorMessage);
        }
    }

    /**
     * 指定错误编码（Integer）服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ThirdPartyBaseException.class)
    public ResponseBaseBean baseExceptionHandler(ThirdPartyBaseException e) {
        String errorMessage = e.getMessage();
        Integer errorCode = e.getCode();
        if (errorCode == null) {
            errorCode = 20020;
        }
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = CommonErrorCode.SYSTEM_EXCEPTION;
            logger.error("ThirdPartyOpenApiException:", e.getMessage());
            return ResponseBaseBean.responseBean(errorCode, MessageUtils.get(errorMessage));
        }
        String msg = MessageUtils.get(errorMessage);
        if (StringUtils.isNotBlank(msg)) {
            logger.error("ThirdPartyOpenApiException:{}", msg);
            return ResponseBaseBean.responseBean(errorCode, msg);
        } else {
            logger.error("ThirdPartyOpenApiException:{}", errorMessage);
            return ResponseBaseBean.responseBean(errorCode, errorMessage);
        }
    }
}
