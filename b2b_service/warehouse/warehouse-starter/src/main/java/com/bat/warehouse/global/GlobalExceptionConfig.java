package com.bat.warehouse.global;

import javax.annotation.Resource;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.common.ErrorCode;
import com.bat.warehouse.db.service.executor.TenantDBCmd;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bat.warehouse.Tenant.Tenant;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.MessageUtils;

@RestControllerAdvice
public class GlobalExceptionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @Resource
    private TenantDBCmd tenantDBCmd;

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
     * 仓库库存异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(WarehouseException.class)
    public Response warehouseHandler(WarehouseException e) {
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
            return Response.buildFailure(ErrorCode.SYSTEM_DATA_ERROR, MessageUtils.get(ErrorCode.SYSTEM_DATA_ERROR));
        }
        String errorCode = e.getMessage();
        if (StringUtils.isBlank(errorCode) || errorCode.length() > 100) {
            errorCode = GlobalErrorCode.SYSTEM_EXCEPTION;
            LOGGER.error(e.getMessage(), e);
        } else {
            LOGGER.error(MessageUtils.get(e.getMessage()), e);
        }
        return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
    }
}
