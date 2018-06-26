package com.hyj.annotation;

import java.lang.annotation.*;

/**
 * Created by houyunjuan on 2018/6/26.
 */
@Target({ElementType.METHOD,ElementType.TYPE})//注解的作用域:CONSTRUCTOR FIELD LOCAL_VARIABLE METHOD PACKAGE PARAMETER TYPE
@Retention(RetentionPolicy.RUNTIME)//注解的声明周期:SOURCE CLASS RUNTIME
@Inherited//标识性注解：允许子注解来继承
public @interface Description {
    String value();
    int age() default 18;
}
