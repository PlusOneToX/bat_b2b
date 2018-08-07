package com.bat.distributor.global;

import static com.bat.distributor.common.ErrorCode.P_NOTNULL;
import static com.bat.distributor.common.ErrorCode.SYSTEM_DATA_ERROR;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.common.ErrorCode;
import com.bat.distributor.db.service.executor.TenantDBCmd;
import com.bat.distributor.service.common.MessageUtils;
import com.bat.distributor.tenant.Tenant;
import com.bat.distributor.tenant.TenantContext;

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
     * 分销商服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DistributorException.class)
    public Response distributorHandler(DistributorException e) {
        e.printStackTrace();
        //e.printStackTrace();
        logger.error("错误码：{}，错误信息：{}",e.getCode(),e.getMsg(),e);
        String errorCode = e.getCode();
        if (StringUtils.isBlank(errorCode)) {
            errorCode = ErrorCode.SYSTEM_EXCEPTION;
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        }
        String msg = e.getMsg();
        if (StringUtils.isNotBlank(msg)) {
            return Response.buildFailure(errorCode, msg);
        }
        msg = MessageUtils.get(errorCode);
        if (StringUtils.isNotBlank(msg)) {
            logger.error(msg);
            return Response.buildFailure(errorCode, MessageUtils.get(errorCode));
        } else {
            logger.error(errorCode);
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
        return Response.buildFailure(ErrorCode.SYSTEM_EXCEPTION, MessageUtils.get(ErrorCode.SYSTEM_EXCEPTION));
    }
}
