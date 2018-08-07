package com.bat.order.global;

import static com.bat.order.common.ErrorCode.P_NOTNULL;
import static com.bat.order.common.ErrorCode.SYSTEM_DATA_ERROR;

import javax.annotation.Resource;

import com.bat.order.db.service.executor.TenantDBCmd;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.common.ErrorCode;
import com.bat.order.tenant.Tenant;
import com.bat.order.tenant.TenantContext;

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
        e.printStackTrace();
        String msg = null;
        if (e instanceof MethodArgumentNotValidException) {
            msg = MessageUtils
                .get(((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage());
        } else if (e instanceof BindException) {
            msg = MessageUtils.get(((BindException)e).getBindingResult().getFieldError().getDefaultMessage());
        } else {
            return Response.buildFailure(ErrorCode.SYSTEM_EXCEPTION, MessageUtils.get(ErrorCode.SYSTEM_EXCEPTION));
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
    @ExceptionHandler(OrderException.class)
    public Response baseHandler(OrderException e) {
        e.printStackTrace();
        String errorCode = e.getCode();
        if (StringUtils.isBlank(errorCode)) {
            errorCode = ErrorCode.SYSTEM_EXCEPTION;
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        }
        String msg = e.getMsg();
        if (StringUtils.isNotBlank(msg)) {
            logger.error("OrderException:", msg);
            return Response.buildFailure(errorCode, msg);
        }
        msg = MessageUtils.get(errorCode);
        if (StringUtils.isNotBlank(msg)) {
            logger.error("OrderException:" + msg);
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        } else {
            logger.error("OrderException:" + errorCode);
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
            Tenant tenant = TenantContext.tenantInfoMap.get(TenantContext.getTenantNo());
            if (tenant != null) {
                tenantDBCmd.tenantDBDelete(tenant.getTenantNo(), tenant.getConfigMap().get("dbName"));
            }
            return Response.buildFailure(SYSTEM_DATA_ERROR, MessageUtils.get(SYSTEM_DATA_ERROR));
        }
        String message = e.getMessage();
        System.out.println(message);
        if (StringUtils.isNotBlank(message) && message.indexOf("FlexibleCustomException:") > -1) {
            // 柔性定制的异常提示、截取第一行
            String[] msgArr = message.split("\r\n");
            String messageCode = msgArr[0].split("FlexibleCustomException:")[1];
            return Response.buildFailure(messageCode, MessageUtils.get(messageCode));
        }
        return Response.buildFailure(ErrorCode.SYSTEM_EXCEPTION, MessageUtils.get(ErrorCode.SYSTEM_EXCEPTION));
    }

}
