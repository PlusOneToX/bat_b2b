package com.bat.warehouse.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SysLog {

    String businessFunction() default "";

    String value() default "";

}
