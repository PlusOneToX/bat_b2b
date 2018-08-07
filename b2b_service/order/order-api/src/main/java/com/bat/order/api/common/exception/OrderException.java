package com.bat.order.api.common.exception;

import static com.bat.order.api.basic.ErrorCode.ROW_NUM;

import java.text.MessageFormat;

import com.alibaba.excel.util.StringUtils;
import com.bat.order.api.common.utils.MessageUtils;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/2 10:32
 */
@Data
public class OrderException extends RuntimeException {

    private String code;
    private String msg;

    public static OrderException buildException(String errCode) {
        OrderException exception = new OrderException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            exception.setMsg(message);
        }
        return exception;
    }

    public static OrderException buildLineException(String errCode, Integer line) {
        OrderException exception = new OrderException();
        exception.setCode(errCode);
        String message = MessageUtils.get(errCode);
        if (message != null && !"".equals(message)) {
            if (line != null) {
                String msg = MessageUtils.get(ROW_NUM);
                if (StringUtils.isNotBlank(msg)) {
                    exception.setMsg(MessageFormat.format(msg, line) + "," + message);
                } else {
                    exception.setMsg("第" + line + "行" + "," + message);
                }
            } else {
                exception.setMsg(message);
            }
        }
        return exception;
    }

    public static OrderException buildException(String errCode, String msg) {
        OrderException exception = new OrderException();
        exception.setCode(errCode);
        exception.setMsg(msg);
        return exception;
    }

    public static OrderException buildLineException(String errCode, String message, Integer line) {
        OrderException exception = new OrderException();
        exception.setCode(errCode);
        if (line != null) {
            String msg = MessageUtils.get(ROW_NUM);
            if (StringUtils.isNotBlank(msg)) {
                exception.setMsg(MessageFormat.format(msg, line) + "," + message);
            } else {
                exception.setMsg("第" + line + "行" + "," + message);
            }
        } else {
            exception.setMsg(message);
        }
        return exception;
    }

}
