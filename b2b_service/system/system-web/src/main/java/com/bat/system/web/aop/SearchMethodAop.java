package com.bat.system.web.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.system.api.base.BaseSearchQry;
import com.bat.system.api.base.ErrorCode;
import com.bat.system.api.base.SystemException;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/8 17:20
 */
@Aspect
@Component
public class SearchMethodAop {
    @Pointcut("@annotation(com.bat.system.web.annotation.SearchMethod)")
    public void searchAspect() {}

    /**
     * 此段代码有局限性 复用要慎用
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("searchAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object[] newArgs = Arrays.stream(args).map(arg -> {
            if (arg instanceof BaseSearchQry) {
                Map<Short, String> attributeMapper = ((BaseSearchQry)arg).getAttributeMapper();
                Short contentType = ((BaseSearchQry)arg).getContentType();
                String content = ((BaseSearchQry)arg).getContent();
                if (contentType != null && StringUtils.isNotBlank(content)) {
                    String attributeName = attributeMapper.get(contentType);
                    Method[] methods = arg.getClass().getMethods();
                    Method method1 = Arrays.stream(methods).filter(method -> method.getName().equals(attributeName))
                        .findFirst().orElseThrow(SystemException::new);
                    try {
                        Object o = arg.getClass().newInstance();
                        BeanUtils.copyProperties(arg, o);
                        // 因为不知道set参数类型 所以又反射构造
                        Class<?> parameterTypes = method1.getParameterTypes()[0];
                        Constructor<?> constructor = parameterTypes.getConstructor(String.class);
                        method1.invoke(o, constructor.newInstance(content));
                        return o;
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException
                        | NoSuchMethodException e) {
                        e.printStackTrace();
                        throw SystemException.buildException(ErrorCode.B_AOP_EXCEPTION);
                    }
                }
            }
            return arg;
        }).toArray();
        return joinPoint.proceed(newArgs);
    }

}
